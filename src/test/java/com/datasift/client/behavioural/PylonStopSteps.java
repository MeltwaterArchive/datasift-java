package com.datasift.client.behavioural;

import com.datasift.client.DataSiftResult;
import com.datasift.client.pylon.PylonRecording;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.higgs.core.ObjectFactory;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

/**
 * Created by agnieszka on 22/01/16.
 */
//CHECKSTYLE:OFF
public class PylonStopSteps extends CucumberBase {
    protected DataSiftResult result;

    @Given("^a stop mock exists$")
    public void aStopMockExists() throws Throwable {
        mock.registerObjectFactory(new ObjectFactory(mock) {
            public Object newInstance(Class<?> klass) {
                return wrapper;
            }

            public boolean canCreateInstanceOf(Class<?> klass) {
                return true;
            }
        });
    }

    @Given("^returns status code \"([^\"]*)\" when recording_id \"([^\"]*)\" at the path \"([^\"]*)\"$")
    public void returnsStatusCodeWhenRecording_idAtThePath(String statusCode, final String recordingId, String path) throws Throwable {
        wrapper.matchQueryStringParams(new HashMap<String, String>() {
            {
                put("id", recordingId);
            }
        }).statusCode(statusCode);
    }

    @When("^a stop request is made with recording_id \"([^\"]*)\"$")
    public void aStopRequestIsMadeWithRecording_id(String recordingId) throws Throwable {
        result = client.pylon().stop(new PylonRecording.PylonRecordingId(recordingId)).sync();
    }

    @Then("^the stop response status code should be \"([^\"]*)\"$")
    public void theStopResponseStatusCodeShouldBe(String statusCode) throws Throwable {
        assertEquals(Integer.parseInt(statusCode), result.getResponse().status());
    }
}
//CHECKSTYLE:ON
