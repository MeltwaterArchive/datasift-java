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

public class PylonGetSteps extends CucumberBase {
    protected PylonRecording recording = null;

    @Given("^a mock exists$")
    public void aMockExists() throws Throwable {
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

    @Then("^the get response status code should be \"([^\"]*)\"$")
    public void theGetResponseStatusCodeShouldBe(String statusCode) throws Throwable {
        assertEquals(Integer.parseInt(statusCode), recording.getResponse().status());
    }


    @Then("^the get response body contains the JSON data$")
    public void theGetResponseBodyContainsTheJSONData(String body) throws Throwable {
        JsonNode expected = mapper.readTree(body);
        JsonNode actual = mapper.readTree(recording.getResponse().data());
        assertTrue(expected.equals(actual));
        assertEquals("1234", recording.getRecordingId().getId());
    }

    @When("^a get request is made with page \"([^\"]*)\" and per_page \"([^\"]*)\" and no body$")
    public void aGetRequestIsMadeWithPageAndPer_pageAndNoBody(String page, String perPage) throws Throwable {
        int sentPage = Integer.parseInt(page);
        int sentPerPage = Integer.parseInt(perPage);
        PylonRecordingList pylonRecordingList = client.pylon().get(sentPage, sentPerPage).sync();
        recording = pylonRecordingList.getSubscriptions().get(0);
        assertEquals(sentPage, pylonRecordingList.getPage());
        assertEquals(sentPerPage, pylonRecordingList.getPerPage());
    }

    @Given("^returns error \"([^\"]*)\" and status code \"([^\"]*)\" when no query string at the path \"([^\"]*)\"$")
    public void returnsErrorAndStatusCodeWhenNoQueryStringAtThePath(String errorMessage, String statusCode, String path) throws Throwable {
        wrapper.response(errorMessage).statusCode(statusCode);
    }

    @When("^a get request is made without recording_id and no body$")
    public void aGetRequestIsMadeWithoutRecording_idAndNoBody() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        try {
            recording = client.pylon().get(null).sync();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @When("^a get request is made with page \"([^\"]*)\" and no per_page and no body$")
    public void aGetRequestIsMadeWithPageAndNoPer_pageAndNoBody(String page) throws Throwable {
        int sentPage = Integer.parseInt(page);
        PylonRecordingList pylonRecordingList = client.pylon().get(sentPage).sync();
        recording = pylonRecordingList.getSubscriptions().get(0);
        assertEquals(sentPage, pylonRecordingList.getPage());
    }
}
