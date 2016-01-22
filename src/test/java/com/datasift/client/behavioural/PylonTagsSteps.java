package com.datasift.client.behavioural;

import com.datasift.client.pylon.PylonRecording;
import com.datasift.client.pylon.PylonTags;
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
public class PylonTagsSteps extends CucumberBase {
    protected PylonTags tags = null;

    @Given("^a tags mock exists$")
    public void aTagsMockExists() throws Throwable {
        mock.registerObjectFactory(new ObjectFactory(mock) {
            public Object newInstance(Class<?> klass) {
                return wrapper;
            }

            public boolean canCreateInstanceOf(Class<?> klass) {
                return true;
            }
        });
    }

    @Given("^returns tags body and status code \"([^\"]*)\" when recording_id \"([^\"]*)\" at the path \"([^\"]*)\"$")
    public void returnsTagsBodyAndStatusCodeWhenRecording_idAtThePath(String statusCode, final String recordingId, String path, String body) throws Throwable {
        wrapper.response(body).statusCode(statusCode).matchQueryStringParams(new HashMap<String, String>() {
            {
                put("id", recordingId);
            }
        });
    }

    @When("^a tags request is made with recording_id \"([^\"]*)\"$")
    public void aTagsRequestIsMadeWithRecording_id(String recordingId) throws Throwable {
        tags = client.pylon().tags(new PylonRecording.PylonRecordingId(recordingId)).sync();
    }

    @Then("^the tags response status code should be \"([^\"]*)\"$")
    public void theTagsResponseStatusCodeShouldBe(String statusCode) throws Throwable {
        assertEquals(Integer.parseInt(statusCode), tags.getResponse().status());
    }

    @Then("^the tags response body contains the JSON data$")
    public void theTagsResponseBodyContainsTheJSONData(String body) throws Throwable {
        JsonNode expected = mapper.readTree(body);
        JsonNode actual = mapper.readTree(tags.getResponse().data());
        assertTrue(expected.equals(actual));
    }
}