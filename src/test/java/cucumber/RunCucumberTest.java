package cucumber;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

/**
 * This class is the entry point for running the Cucumber tests.
 * It specifies the location of the feature files and the step definitions.
 */
@RunWith(Cucumber.class)
@CucumberOptions(features = "src/main/resources/features", glue = "cucumber.stepdefs")
public class RunCucumberTest {
}