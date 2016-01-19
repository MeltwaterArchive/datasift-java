package com.datasift.client.behavioural;

import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

/**
 * Created by agnieszka on 19/01/16.
 */
public class TestRest extends CucumberRunner {
    @Given("^a mock exists at \"([^\"]*)\" it should return \"([^\"]*)\" with the body:$")
    public void a_mock_exists_at_it_should_return_with_the_body(String arg1, String arg2, String arg3) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Given("^that the request body is valid JSON$")
    public void that_the_request_body_is_valid_JSON(String arg1) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Given("^the mocks are created$")
    public void the_mocks_are_created() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @When("^I make a \"([^\"]*)\" request to \"([^\"]*)\"$")
    public void i_make_a_request_to(String arg1, String arg2) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Then("^the response status code should be \"([^\"]*)\"$")
    public void the_response_status_code_should_be(String arg1) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Then("^the response body contains the JSON data$")
    public void the_response_body_contains_the_JSON_data(String arg1) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

}
