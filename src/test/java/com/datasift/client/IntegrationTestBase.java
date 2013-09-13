package com.datasift.client;

import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Properties;

import static org.junit.Assert.fail;

/**
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public class IntegrationTestBase {
    protected Settings settings = new Settings();
    protected Logger log = LoggerFactory.getLogger(getClass());
    protected DataSiftConfig config = new DataSiftConfig();
    protected DataSiftClient dataSift;

    /**
     * Initializes the {@link #settings} object from a settings.properties file taken from the root of the class path
     * Without this being successful many or all integration tests will fail.
     *
     * @throws IOException            if settings.properties cannot be loaded
     * @throws IllegalAccessException if an attempt is made to access a field that doesn't exist...shouldn't happen
     */
    @Before
    public void setup() throws IOException, IllegalAccessException {
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
        dataSift = new DataSiftClient(config);
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

}
