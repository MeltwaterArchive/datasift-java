package com.datasift.client.behavioural;

import com.datasift.client.pylon.PylonRecording;
import com.datasift.client.pylon.PylonStream;
import com.fasterxml.jackson.databind.JsonNode;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.higgs.core.ObjectFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by agnieszka on 22/01/16.
 */
public class PylonStartSteps extends CucumberBase {
    protected PylonRecording.PylonRecordingId recording;

    @Given("^a start mock exists$")
    public void aStartMockExists() throws Throwable {
        mock.registerObjectFactory(new ObjectFactory(mock) {
            public Object newInstance(Class<?> klass) {
                return wrapper;
            }

            public boolean canCreateInstanceOf(Class<?> klass) {
                return true;
            }
        });
    }

    @Given("^returns start body and status code \"([^\"]*)\" at the path \"([^\"]*)\"$")
    public void returnsStartBodyAndStatusCodeAtThePath(String statusCode, String path, String body) throws Throwable {
        wrapper.response(body).statusCode(statusCode);
    }

    @When("^a start request is made with hash \"([^\"]*)\" and name \"([^\"]*)\"$")
    public void aStartRequestIsMadeWithHashAndName(String hash, String name) throws Throwable {
        PylonStream stream = PylonStream.fromString(hash);
        recording = client.pylon().start(stream, name).sync();
    }

    @Then("^the start response status code should be \"([^\"]*)\"$")
    public void theStartResponseStatusCodeShouldBe(String statusCode) throws Throwable {
        assertEquals(Integer.parseInt(statusCode), recording.getResponse().status());
    }

    @Then("^the start response body contains the JSON data$")
    public void theStartResponseBodyContainsTheJSONData(String body) throws Throwable {
        JsonNode expected = mapper.readTree(body);
        JsonNode actual = mapper.readTree(recording.getResponse().data());
        assertTrue(expected.equals(actual));
    }
}
