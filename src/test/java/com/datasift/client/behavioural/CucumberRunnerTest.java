package com.datasift.client.behavioural;

import cucumber.api.CucumberOptions;
import cucumber.api.SnippetType;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

//CHECKSTYLE:OFF
@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"pretty", "html:cucumber", "junit:cucumber-junit.xml"}
        , snippets = SnippetType.CAMELCASE
        , features = {"."}
        , strict = true)
//CHECKSTYLE:ON
public class CucumberRunnerTest {
}
