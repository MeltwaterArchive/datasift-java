package com.datasift.client.behavioural;

import com.datasift.client.DataSiftResult;
import com.datasift.client.pylon.PylonRecording;
import com.datasift.client.pylon.PylonStream;
import com.fasterxml.jackson.databind.JsonNode;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.higgs.core.ObjectFactory;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by agnieszka on 22/01/16.
 */
public class PylonUpdateSteps extends CucumberBase {
    private DataSiftResult result;

    @Given("^a update mock exists$")
    public void aUpdateMockExists() throws Throwable {
        mock.registerObjectFactory(new ObjectFactory(mock) {
            public Object newInstance(Class<?> klass) {
                return wrapper;
            }

            public boolean canCreateInstanceOf(Class<?> klass) {
                return true;
            }
        });
    }

    @Given("^returns status code \"([^\"]*)\" when query string \"([^\"]*)\" at the path \"([^\"]*)\"$")
    public void returnsStatusCodeWhenQueryStringAtThePath(String statusCode, String queryString, String path) throws Throwable {
        final String[] queryParametersArray = queryString.split("&");

        wrapper.matchQueryStringParams(new HashMap<String, String>() {
            {
                for (String queryParameter : queryParametersArray) {
                    String[] params = queryParameter.split("=");
                    put(params[0], params[1]);
                }
            }
        }).statusCode(statusCode);
    }

    @When("^an update request is made with recording_id \"([^\"]*)\", hash \"([^\"]*)\" and name \"([^\"]*)\"$")
    public void anUpdateRequestIsMadeWithRecording_idHashAndName(String recordingId, String hash, String name) throws Throwable {
        PylonStream stream = PylonStream.fromString(hash);
        result = client.pylon().update(new PylonRecording.PylonRecordingId(recordingId), stream, name).sync();
    }

    @Then("^the response status code should be \"([^\"]*)\"$")
    public void theResponseStatusCodeShouldBe(String statusCode) throws Throwable {
        assertEquals(Integer.parseInt(statusCode), result.getResponse().status());
    }

    @Given("^returns update error \"([^\"]*)\" and status code \"([^\"]*)\"$")
    public void returnsUpdateErrorAndStatusCode(String error, String statusCode) throws Throwable {
        wrapper.response(error).statusCode(statusCode);
    }

    @When("^a update request is made with no recording_id, hash \"([^\"]*)\" and name \"([^\"]*)\"$")
    public void aUpdateRequestIsMadeWithNoRecording_idHashAndName(String hash, String name) throws Throwable {
        PylonStream stream = PylonStream.fromString(hash);
        client.pylon().update(null, stream, name).sync();
    }

    @Then("^the update response status code should be \"([^\"]*)\"$")
    public void theUpdateResponseStatusCodeShouldBe(String statusCode) throws Throwable {
        assertEquals(Integer.parseInt(statusCode), result.getResponse().status());
    }

    @Then("^the update response body contains the JSON data$")
    public void theUpdateResponseBodyContainsTheJSONData(String body) throws Throwable {
        JsonNode expected = mapper.readTree(body);
        JsonNode actual = mapper.readTree(result.getResponse().data());
        assertTrue(expected.equals(actual));
    }

    @When("^an update request is made with recording_id \"([^\"]*)\" and name \"([^\"]*)\"$")
    public void anUpdateRequestIsMadeWithRecording_idAndName(String recordingId, String name) throws Throwable {
        result = client.pylon().update(new PylonRecording.PylonRecordingId(recordingId), name).sync();
    }

    @When("^an update request is made with recording_id \"([^\"]*)\" and hash \"([^\"]*)\"$")
    public void anUpdateRequestIsMadeWithRecording_idAndHash(String recordingId, String hash) throws Throwable {
        PylonStream stream = PylonStream.fromString(hash);
        result = client.pylon().update(new PylonRecording.PylonRecordingId(recordingId), stream).sync();
    }

    @When("^an update request is made with recording_id \"([^\"]*)\", no hash or name$")
    public void anUpdateRequestIsMadeWithRecording_idNoHashOrName(String recordingId) throws Throwable {
        result = client.pylon().update(new PylonRecording.PylonRecordingId(recordingId), new PylonStream()).sync();
    }

}
