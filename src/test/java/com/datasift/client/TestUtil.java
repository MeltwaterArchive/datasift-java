package com.datasift.client;

import com.datasift.client.core.Stream;
import com.datasift.client.historics.PreparedHistoricsQuery;
import com.datasift.client.push.PushConnectors;
import com.datasift.client.push.PushSubscription;
import com.datasift.client.push.PushValidation;
import com.datasift.client.push.connectors.S3;
import org.junit.Assert;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public class TestUtil {
    protected final String csdlValid = "interaction.content contains \"test\"";
    protected final String csdlInvalid = csdlValid + " random string";
    protected Settings settings = new Settings();
    protected Logger log = LoggerFactory.getLogger(getClass());
    protected DataSiftConfig config = new DataSiftConfig();
    protected DataSiftClient datasift;

    /**
     * Initializes the {@link #settings} object from a settings.properties file taken from the root of the class path
     * Without this being successful many or all integration tests will fail.
     *
     * @throws IOException            if settings.properties cannot be loaded
     * @throws IllegalAccessException if an attempt is made to access a field that doesn't exist...shouldn't happen
     */
    @Before
    public void setup() throws IOException, IllegalAccessException, Exception {
        InputStream in = IntegrationTestBase.class.getResourceAsStream("/settings.properties");
        Properties properties = new Properties();
        properties.load(in);
        Class<Settings> klass = Settings.class;

        for (String name : properties.stringPropertyNames()) {
            try {
                Field field = klass.getDeclaredField(name);
                field.setAccessible(true);

                Object value = properties.getProperty(name);

                if (field.getType().isAssignableFrom(int.class) || field.getType().isAssignableFrom(Integer.class)) {
                    value = Integer.parseInt(String.valueOf(value));
                }

                if (field.getType().isAssignableFrom(boolean.class) || field.getType().isAssignableFrom(Boolean
                        .class)) {
                    value = Integer.parseInt(String.valueOf(value));
                }

                field.set(settings, value);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
                log.warn(String.format("Unknown setting. '%s' doesn't match a known setting,ignored", name));
            }
        }
        config.urlEncodingFormat(settings.getUrlEncodeWith())
                .auth(settings.getUsername(), settings.getApiKey());
        datasift = new DataSiftClient(config);
    }

    public void doWait() {
        synchronized (this) {
            try {
                wait();
            } catch (InterruptedException e) {
                fail("Unable to wait for any interactions");
            }
        }
    }

    public void doNotify() {
        synchronized (this) {
            notify();
        }
    }

    /**
     * All results extend {@link BaseDataSiftResult} which provides some methods for checking the state of every API
     * response and some basic info such as rate limit and remaining rate limit
     */
    public void successful(DataSiftResult data) {
        if (!data.isSuccessful() && data.failureCause() != null) {
            throw new RuntimeException(data.failureCause());
        }
        assertTrue(data.isSuccessful());
    }

    public <T extends BaseDataSiftResult> void successful(FutureData<T> resultFutureData) {
        resultFutureData.onData(new FutureResponse<T>() {
            public void apply(T data) {
                successful(data);
            }
        });
    }

    public PushSubscription createPushSubscription(Stream stream, FutureData<PreparedHistoricsQuery> query)
            throws InterruptedException {
        S3 s3 = PushConnectors
                .s3()
                .accessKey(settings.getS3AccessKey())
                .secretKey(settings.getS3SecretKey())
                .bucket("apitests")
                .directory("java-client")
                .acl("private")
                .deliveryFrequency(0)
                .maxSize(10485760)
                .filePrefix("DataSiftJava-");
        PushValidation pv = datasift.push().validate(s3).sync();
        successful(pv);
        String name = "Java push name", updatedName = name += " updated";
        PushSubscription subscription =
                stream != null ? datasift.push().create(s3, stream, name).sync()
                        : datasift.push().create(s3, query, name).sync();
        successful(subscription);
        Assert.assertTrue(subscription.status().isActive());
        //test update
        PushSubscription updatedSubscription = datasift.push().update(subscription.getId(), s3, updatedName).sync();
        successful(updatedSubscription);
        assertEquals(updatedName, updatedSubscription.name());
        return subscription;
    }

    public Stream createStream() throws InterruptedException {
        //create a stream to use
        return datasift.compile(csdlValid).sync();
    }

}
