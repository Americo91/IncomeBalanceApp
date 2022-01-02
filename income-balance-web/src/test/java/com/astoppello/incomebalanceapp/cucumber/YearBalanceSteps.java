package com.astoppello.incomebalanceapp.cucumber;

import com.astoppello.incomebalanceapp.dto.domain.YearBalanceDTO;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.ScenarioScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.HttpClientErrorException;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/** Created by @author stopp on 19/12/2021 */
@ScenarioScope
@SpringBootTest
public class YearBalanceSteps {

  @Autowired YearBalanceClient yearBalanceClient;

  @Given("there are {int} yearBalances")
  public void thereAreYearBalances(int n) {
    assertThat(n).isEqualTo(yearBalanceClient.getYearBalances().getYearBalances().size());
  }

  @When("a yearBalance year {int} is added")
  public void aYearBalanceWithYearIsAdded(int year) {
    YearBalanceDTO yearBalanceDTO = YearBalanceDTO.builder().year(year).build();
    YearBalanceDTO savedYB = yearBalanceClient.createYearBalance(yearBalanceDTO);
    assertThat(savedYB).isNotNull();
    assertThat(savedYB.getYear()).isEqualTo(year);
  }

  @Then("yearBalance year {int} has {int} bankBalances")
  public void yearbalanceYearHasBankBalances(int year, int n) {
    YearBalanceDTO yearBalanceDTO = yearBalanceClient.findYearBalanceByYear(year);
    assertThat(yearBalanceDTO.getBankBalances()).isNotNull();
    assertThat(yearBalanceDTO.getBankBalances().size()).isEqualTo(n);
  }

  @Then("yearBalance year {int} has {int} monthBalances")
  public void yearbalanceYearHasMonthBalances(int year, int n) {
    YearBalanceDTO yearBalanceDTO = yearBalanceClient.findYearBalanceByYear(year);
    assertThat(yearBalanceDTO.getMonthBalances()).isNotNull();
    assertThat(yearBalanceDTO.getMonthBalances().size()).isEqualTo(n);
  }

  @Then("yearBalance year {int} has savings {string}, salary {string}, result {string}")
  public void yearbalanceYearHasSavingsSalaryAndResult(
      int year, String savings, String salary, String result) {
    YearBalanceDTO yearBalanceDTO = yearBalanceClient.findYearBalanceByYear(year);
    assertThat(yearBalanceDTO.getSavings()).isEqualTo(savings);
    assertThat(yearBalanceDTO.getSalary()).isEqualTo(new BigDecimal(salary));
    assertThat(yearBalanceDTO.getResult()).isEqualTo(new BigDecimal(result));
  }

  @Then("yearBalance year {int} not exist")
  public void yearbalanceYearNotExist(int year) {
    assertThatThrownBy(() -> yearBalanceClient.findYearBalanceByYear(year))
        .isInstanceOf(HttpClientErrorException.class)
        .hasMessageContaining("404");
  }

  @Then("yearBalance year {int} exists")
  public void yearbalanceYearExists(int year) {
    YearBalanceDTO yearBalanceDTO = yearBalanceClient.findYearBalanceByYear(year);
    assertThat(yearBalanceDTO).isNotNull();
    assertThat(yearBalanceDTO.getYear()).isEqualTo(year);
  }

  @When("Replace yearBalance year {int} with yearBalance year {int}")
  public void replaceYearBalanceYearWithYearBalanceYear(int year, int newYear) {
    YearBalanceDTO ybToReplace = yearBalanceClient.findYearBalanceByYear(year);
    assertThat(ybToReplace).isNotNull();
    assertThat(ybToReplace.getId()).isNotNull();
    YearBalanceDTO yearBalanceDTO = YearBalanceDTO.builder().year(newYear).build();
    yearBalanceClient.putYearBalance(ybToReplace.getId(), yearBalanceDTO);
  }
}
