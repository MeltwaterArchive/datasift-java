package com.datasift.client.behavioural;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

/**
 * Created by agnieszka on 19/01/16.
 */
public class TestRest {
    @Given("^that the request body is valid JSON$")
    public void thatTheRequestBodyIsValidJSON(String arg1) throws Throwable {
    }

    @When("^I make a \"([^\"]*)\" request to \"([^\"]*)\"$")
    public void iMakeARequestTo(String arg1, String arg2) throws Throwable {

    }

    @Then("^the response status code should be \"([^\"]*)\"$")
    public void theResponseStatusCodeShouldBe(String arg1) throws Throwable {

    }

    @Then("^the response body contains the JSON data$")
    public void theResponseBodyContainsTheJSONData(String arg1) throws Throwable {

    }

    @Given("^a mock exists at \"([^\"]*)\" it should return \"([^\"]*)\" with the body:$")
    public void aMockExistsAtItShouldReturnWithTheBody(String arg1, String arg2, String arg3) throws Throwable {

    }

    @Given("^the mocks are created$")
    public void theMocksAreCreated() throws Throwable {

    }

}
