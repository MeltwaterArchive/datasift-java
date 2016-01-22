package com.datasift.client.behavioural;

import com.datasift.client.pylon.*;
import com.fasterxml.jackson.databind.JsonNode;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.higgs.core.ObjectFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by agnieszka on 20/01/16.
 */
public class PylonAnalyzeSteps extends CucumberBase {
    protected PylonResult result = null;

    @Given("^an analysis mock exists$")
    public void anAnalysisMockExists() throws Throwable {
        mock.registerObjectFactory(new ObjectFactory(mock) {
            public Object newInstance(Class<?> klass) {
                return wrapper;
            }

            public boolean canCreateInstanceOf(Class<?> klass) {
                return true;
            }
        });
    }

    @Given("^returns this body and status code \"([^\"]*)\" at the path \"([^\"]*)\"$")
    public void returnsThisBodyAndStatusCodeAtThePath(String statusCode, String path, String body) throws Throwable {
        wrapper.response(body).statusCode(statusCode);
    }

    @Given("^returns error \"([^\"]*)\" and status code \"([^\"]*)\"$")
    public void returnsErrorAndStatusCode(String error, String statusCode) throws Throwable {
        wrapper.response(error).statusCode(statusCode);
    }

    @When("^an analysis request is made with recording id \"([^\"]*)\", analysisType \"([^\"]*)\", parameters interval \"([^\"]*)\" and target \"([^\"]*)\"$")
    public void anAnalysisRequestIsMadeWithRecordingIdAnalysisTypeParametersIntervalAndTarget(String recordingId, String anaylsisType, String interval, String target) throws Throwable {
        PylonQueryParameters pylonQueryParameters = new PylonQueryParameters(anaylsisType, new PylonParametersData(interval, null, null, target));
        PylonQuery pylonQuery = new PylonQuery(new PylonRecording.PylonRecordingId(recordingId), pylonQueryParameters);
        result = client.pylon().analyze(pylonQuery).sync();
    }

    @When("^an analysis request is made with no recording id, analysisType \"([^\"]*)\", parameters interval \"([^\"]*)\" and target \"([^\"]*)\"$")
    public void anAnalysisRequestIsMadeWithNoRecordingIdAnalysisTypeParametersIntervalAndTarget(String anaylsisType, String interval, String target) throws Throwable {
        PylonQueryParameters pylonQueryParameters = new PylonQueryParameters(anaylsisType, new PylonParametersData(interval, null, null, target));
        PylonQuery pylonQuery = new PylonQuery(null, pylonQueryParameters);
            result = client.pylon().analyze(pylonQuery).sync();
    }

    @Then("^the analyze response status code should be \"([^\"]*)\"$")
    public void theAnalyzeResponseStatusCodeShouldBe(String statusCode) throws Throwable {
        assertEquals(Integer.parseInt(statusCode), result.getResponse().status());

    }

    @Then("^the analyze response body contains the JSON data$")
    public void theAnalyzeResponseBodyContainsTheJSONData(String body) throws Throwable {
        JsonNode expected = mapper.readTree(body);
        JsonNode actual = mapper.readTree(result.getResponse().data());
        assertTrue(expected.equals(actual));
    }
}