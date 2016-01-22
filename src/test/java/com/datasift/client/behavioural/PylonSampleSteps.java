package com.datasift.client.behavioural;

import com.datasift.client.pylon.PylonRecording;
import com.datasift.client.pylon.PylonSampleRequest;
import com.fasterxml.jackson.databind.JsonNode;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.higgs.core.ObjectFactory;
import com.datasift.client.pylon.PylonSample;
import static org.junit.Assert.assertEquals;

import static org.junit.Assert.assertTrue;

/**
 * Created by agnieszka on 21/01/16.
 */
public class PylonSampleSteps extends CucumberBase {
    protected PylonSample sample = null;

    @Given("^a sample mock exists$")
    public void aSampleMockExists() throws Throwable {
        mock.registerObjectFactory(new ObjectFactory(mock) {
            public Object newInstance(Class<?> klass) {
                return wrapper;
            }

            public boolean canCreateInstanceOf(Class<?> klass) {
                return true;
            }
        });
    }

    @Given("^returns sample body and status code \"([^\"]*)\" at the path \"([^\"]*)\"$")
    public void returnsSampleBodyAndStatusCodeAtThePath(String statusCode, String path, String body) throws Throwable {
        wrapper.response(body).statusCode(statusCode);
    }

    @Given("^returns sample error \"([^\"]*)\" and status code \"([^\"]*)\"$")
    public void returnsSampleErrorAndStatusCode(String error, String statusCode) throws Throwable {
        wrapper.response(error).statusCode(statusCode);
    }

    @Then("^the sample response status code should be \"([^\"]*)\"$")
    public void theSampleResponseStatusCodeShouldBe(String statusCode) throws Throwable {
        assertEquals(Integer.parseInt(statusCode), sample.getResponse().status());
    }

    @Then("^the sample response body contains the JSON data$")
    public void theSampleResponseBodyContainsTheJSONData(String body) throws Throwable {
        JsonNode expected = mapper.readTree(body);
        JsonNode actual = mapper.readTree(sample.getResponse().data());
        assertTrue(expected.equals(actual));
    }

    @When("^a sample request is made with recording_id \"([^\"]*)\" and count \"([^\"]*)\"$")
    public void aSampleRequestIsMadeWithRecording_idAndCount(String recording_id, String count) throws Throwable {
        PylonRecording.PylonRecordingId pylonRecordingId = new PylonRecording.PylonRecordingId(recording_id);
        PylonSampleRequest pylonSampleRequest = new PylonSampleRequest(pylonRecordingId, Integer.parseInt(count));
        sample = client.pylon().sample(pylonSampleRequest).sync();
    }

    @When("^a sample request is made with no recording_id and count \"([^\"]*)\"$")
    public void aSampleRequestIsMadeWithNoRecording_idAndCount(String count) throws Throwable {
        PylonSampleRequest pylonSampleRequest = new PylonSampleRequest(null, Integer.parseInt(count));
        sample = client.pylon().sample(pylonSampleRequest).sync();
    }
}
