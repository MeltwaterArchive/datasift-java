package com.datasift.client.behavioural;

import com.datasift.client.pylon.PylonRecording;
import com.fasterxml.jackson.databind.JsonNode;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import io.higgs.core.ObjectFactory;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PylonGet extends CucumberBase {
    @Given("^a mock exists$")
    public void aMockExistsAt() throws Throwable {
        startMock();
        mock.registerObjectFactory(new ObjectFactory(mock) {
            public Object newInstance(Class<?> klass) {
                return wrapper;
            }

            public boolean canCreateInstanceOf(Class<?> klass) {
                return true;
            }
        });
    }

    @And("^returns this body when the query string \"([^\"]*)\" is \"([^\"]*)\" at the path \"([^\"]*)\"$")
    public void returnsThisBodyWhenTheQueryStringIsProvided(final String field, final String value, String path
            , String body) throws Throwable {
        wrapper.response(body)
                .mathQueryStringParams(new HashMap<String, String>() {
                    {
                        put(field, value);
                    }
                });
    }

    @And("^returns this body when there is no query strings$")
    public void returnsThisBodyWhenThereIsNoQueryStrings(String body) throws Throwable {
        wrapper.mathQueryStringParams(null); //no query strings to match
        wrapper.response(body); //return the list of results from the scenario
    }

    @When("^a query is made with the recording_id \"([^\"]*)\" it should return \"([^\"]*)\" with the body$")
    public void aQueryIsMadeWithTheRecording_idItShouldReturnWithTheBody(String recordingId, String statusCode
            , String body) throws Throwable {
        PylonRecording recording = client.pylon().get(new PylonRecording.PylonRecordingId(recordingId)).sync();
        assertEquals(Integer.parseInt(statusCode), recording.getResponse().status());
        JsonNode expected = mapper.readTree(body);
        JsonNode actual = mapper.readTree(recording.getResponse().data());
        //the equals method of JsonNode actually does a deep comparison of the JSON fields
        assertTrue(expected.equals(actual));
// TODO: do verifications on the recording object as well to ensure the JSON is mapped to the class' fields properly
        assertEquals("1234", recording.getRecordingId().getId());
//        Map<String, Object> expected = mapper.readValue(body, new TypeReference<Map<String, Object>>() {
//        });
//        Map<String, Object> subscriptions = (Map<String, Object>) expected.get("subscriptions");
//        assertNotNull(subscriptions);
    }
}
