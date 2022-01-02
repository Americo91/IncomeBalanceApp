package com.astoppello.incomebalanceapp.cucumber;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

/** Created by @author stopp on 19/12/2021 */
@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/resources/features",
    plugin = {"pretty", "html:target/cucumber/IncomeBalance.html"})
public class IncomeBalanceAppIntegrationTest {}
