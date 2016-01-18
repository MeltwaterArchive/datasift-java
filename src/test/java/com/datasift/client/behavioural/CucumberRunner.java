package com.datasift.client.behavioural;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

//CHECKSTYLE:OFF
@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"pretty", "html:target/cucumber"}, features = {"."})
//CHECKSTYLE:ON
public class CucumberRunner {
}
