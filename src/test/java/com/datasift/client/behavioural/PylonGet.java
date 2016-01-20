package com.datasift.client.behavioural;

import com.datasift.client.pylon.PylonRecording;
import com.datasift.client.pylon.PylonRecordingList;
import com.fasterxml.jackson.databind.JsonNode;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.higgs.core.ObjectFactory;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PylonGet extends CucumberBase {
    protected PylonRecording recording = null;

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

    @Given("^returns this body and status code \"([^\"]*)\" when the query string \"([^\"]*)\" at the path \"([^\"]*)\"$")
    public void returnsThisBodyAndStatusCodeWhenTheQueryStringAtThePath(String statusCode, String queryString, String path, String body) throws Throwable {
        final String[] queryParametersArray = queryString.split("&");

        wrapper.response(body)
                .matchQueryStringParams(new HashMap<String, String>() {
                    {
                        for (String queryParameter : queryParametersArray) {
                            String[] params = queryParameter.split("=");
                            put(params[0], params[1]);
                        }
                    }
                }).statusCode(statusCode);
    }

    @When("^a get request is made with a recording_id \"([^\"]*)\" and no body$")
    public void aGetRequestIsMadeWithARecording_idAndNoBody(String recordingId) throws Throwable {
        recording = client.pylon().get(new PylonRecording.PylonRecordingId(recordingId)).sync();
        assertEquals(recordingId, recording.getRecordingId().getId());
    }

    @Then("^the response status code should be \"([^\"]*)\"$")
    public void theResponseStatusCodeShouldBe(String statusCode) throws Throwable {
        assertEquals(Integer.parseInt(statusCode), recording.getResponse().status());
    }


    @Then("^the response body contains the JSON data$")
    public void theResponseBodyContainsTheJSONData(String body) throws Throwable {
        JsonNode expected = mapper.readTree(body);
        JsonNode actual = mapper.readTree(recording.getResponse().data());
        assertTrue(expected.equals(actual));
        assertEquals("1234", recording.getRecordingId().getId());
    }

    @When("^a get request is made with page \"([^\"]*)\" and per_page \"([^\"]*)\" and no body$")
    public void aGetRequestIsMadeWithPageAndPer_pageAndNoBody(String page, String per_page) throws Throwable {
        int sentPage = Integer.parseInt(page);
        int sentPerPage = Integer.parseInt(per_page);
        PylonRecordingList pylonRecordingList = client.pylon().get(sentPage, sentPerPage).sync();
        recording = pylonRecordingList.getData().get(0);
        assertEquals(sentPage, pylonRecordingList.getPage());
        assertEquals(sentPerPage, pylonRecordingList.getPage());
    }






//    @And("^returns this body when there is no query strings$")
//    public void returnsThisBodyWhenThereIsNoQueryStrings(String body) throws Throwable {
//        wrapper.matchQueryStringParams(null); //no query strings to match
//        wrapper.response(body); //return the list of results from the scenario
//    }
//
//    @When("^a query is made with the recording_id \"([^\"]*)\" it should return \"([^\"]*)\" with the body$")
//    public void aQueryIsMadeWithTheRecording_idItShouldReturnWithTheBody(String recordingId, String statusCode
//            , String body) throws Throwable {
//        PylonRecording recording = client.pylon().get(new PylonRecording.PylonRecordingId(recordingId)).sync();
//        assertEquals(Integer.parseInt(statusCode), recording.getResponse().status());
//        JsonNode expected = mapper.readTree(body);
//        JsonNode actual = mapper.readTree(recording.getResponse().data());
//        //the equals method of JsonNode actually does a deep comparison of the JSON fields
//        assertTrue(expected.equals(actual));
//// TODO: do verifications on the recording object as well to ensure the JSON is mapped to the class' fields properly
//        assertEquals(recordingId, recording.getRecordingId().getId());
////        Map<String, Object> expected = mapper.readValue(body, new TypeReference<Map<String, Object>>() {
////        });
////        Map<String, Object> subscriptions = (Map<String, Object>) expected.get("subscriptions");
////        assertNotNull(subscriptions);
//    }


}
