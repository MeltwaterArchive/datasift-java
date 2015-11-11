package com.datasift.client.behavioural;

import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static org.junit.Assert.*;

public class ValidateCSDLSteps {
    @Given("^CSDL to validate like \"([^\"]*)\"$")
    public void givenThis(String csdl) throws Throwable {
        assertEquals("interaction.content contains \"moo\"", csdl);
    }

    @When("^I call the validate endpoint$")
    public void i_call_the_validate_endpoint() throws Throwable {
    }

    @Then("^I should get a validation with a dpu cost$")
    public void i_should_get_a_validation_with_a_dpu_cost() throws Throwable {
    }

    @Given("^CSDL to validate like /interaction.content contains \\"([^\"]*)\"/$")
    public void CSDL_to_validate_like_interaction_content_contains_(String arg1) throws Throwable {
        // Express the Regexp above with the code you wish you had
        throw new PendingException();
    }
}
