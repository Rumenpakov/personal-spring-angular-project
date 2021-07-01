package CucumberTests;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"pretty"},
        features = {"classpath:features"},
        glue = {"cucumber.api.spring", "CucumberTests.steps"})
public class RunCucumberTest {

}
