package com.datasift.client.core;

import com.datasift.client.FutureData;
import com.datasift.client.FutureResponse;
import com.datasift.client.Settings;
import com.datasift.client.TestUtil;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Covers the core API which includes
 * /validate
 * /compile
 * /usage
 * /dpu
 * /balance
 *
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public class CoreAPITestIT extends TestUtil {

    @Test(timeout = Settings.TIMEOUT)
    public void validate() throws InterruptedException {
        //validate using a valid CSDL
        FutureData<Validation> csdl = datasift.core().validate(csdlValid);
        csdl.onData(new FutureResponse<Validation>() {
            public void apply(Validation data) {
                successful(data);
                assertTrue(data.isValid());
            }
        });
        //validate using an invalid CSDL
        FutureData<Validation> invalidValidation = datasift.core().validate(csdlInvalid);
        invalidValidation.onData(new FutureResponse<Validation>() {
            public void apply(Validation data) {
                successful(data);
                assertFalse(data.isValid());
            }
        });
    }

    @Test
    public void validateSynchronously() throws InterruptedException {
        Validation csdl = datasift.core().validate(csdlValid).sync();
        successful(csdl);
        assertTrue(csdl.isValid());
        Validation invalidValidation = datasift.core().validate(csdlInvalid).sync();
        assertFalse(invalidValidation.isValid());
    }

    @Test(timeout = Settings.TIMEOUT)
    public void compile() throws InterruptedException {
        datasift.core().compile(csdlValid).onData(new FutureResponse<Stream>() {
            public void apply(Stream data) {
                successful(data);
                assertNotNull(data.hash());
                assertNotEquals(data.hash(), "", "Stream hash should never be empty");
            }
        });
    }

    @Test
    public void compileSync() throws InterruptedException {
        Stream data = datasift.core().compile(csdlValid).sync();
        successful(data);
        assertNotNull(data.hash());
        assertNotEquals(data.hash(), "", "Stream hash should never be empty");
    }

    @Test(timeout = Settings.TIMEOUT)
    public void compileInvalidCSDL() throws InterruptedException {
        datasift.core().compile(csdlInvalid).onData(new FutureResponse<Stream>() {
            public void apply(Stream data) {
                assertFalse(data.isSuccessful());
                assertNotEquals(data.getError(), "", "Error message shouldn't be empty");
            }
        });
    }

    @Test
    public void compileInvalidCSDLSync() throws InterruptedException {
        Stream data = datasift.core().compile(csdlInvalid).sync();
        assertNotEquals(data.getError(), "", "Error message shouldn't be empty");
    }

    @Test(timeout = Settings.TIMEOUT)
    public void dpus() throws InterruptedException {
        FutureData<Stream> data = datasift.core().compile(csdlValid);
        //dpus can be fetched from a stream future
        FutureData<Dpu> dpusFromFuture = datasift.core().dpu(data);
        //or from an already fetched stream
        Dpu dpus = datasift.core().dpu(data.sync()).sync();
        successful(dpus);
        //Minimum DPU is 1
        assertNotEquals(dpus.getDpu(), 0d);
        assertNotNull("Details of the DPU breakdown should be given", dpus.getDetail());
        //async validation
        dpusFromFuture.onData(new FutureResponse<Dpu>() {
            public void apply(Dpu data) {
                assertNotEquals(data.getDpu(), 0d);
                assertNotNull("Details of the DPU breakdown should be given", data.getDetail());
            }
        });
    }

    @Test(timeout = Settings.TIMEOUT)
    public void balance() {
        datasift.core().balance().onData(new FutureResponse<Balance>() {
            public void apply(Balance data) {
                successful(data);
                assertNotNull("Balance data must be returned", data);
            }
        });
    }

//    @Test
//    public void balanceSync() throws InterruptedException {
//        Balance data = datasift.core().balance().sync();
//        successful(data);
//        assertNotNull("Balance data must be returned", data);
//    }

    @Test(timeout = Settings.TIMEOUT)
    public void usage() {
        datasift.core().usage().onData(new FutureResponse<Usage>() {
            public void apply(Usage data) {
                successful(data);
                assertNotNull(data.getStreams());
            }
        });
    }

//    @Test
//    public void usageSync() throws InterruptedException {
//        Usage data = datasift.core().usage().sync();
//        successful(data);
//        assertNotNull(data.getStreams());
//    }
}
