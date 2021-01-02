package com.astoppello.incomebalanceapp.utils;

import com.astoppello.incomebalanceapp.dto.domain.BankBalanceDTO;
import com.astoppello.incomebalanceapp.dto.domain.BankDTO;
import com.astoppello.incomebalanceapp.dto.domain.MonthBalanceDTO;
import com.astoppello.incomebalanceapp.dto.domain.YearBalanceDTO;
import com.astoppello.incomebalanceapp.model.Bank;
import com.astoppello.incomebalanceapp.model.BankBalance;
import com.astoppello.incomebalanceapp.model.MonthBalance;
import com.astoppello.incomebalanceapp.model.YearBalance;

import static org.junit.jupiter.api.Assertions.assertEquals;

/** Created by @author stopp on 21/12/2020 */
public class ModelEqualUtils {

  public static void assertYearBalanceAndYearBalanceDtoAreEquals(
      YearBalance yearBalance, YearBalanceDTO yearBalanceDTO) {
    assertEquals(yearBalance.getId(), yearBalanceDTO.getId());
    assertEquals(yearBalance.getYear(), yearBalanceDTO.getYear());
    assertEquals(yearBalance.getExpenses(), yearBalanceDTO.getExpenses());
    assertEquals(yearBalance.getIncomes(), yearBalanceDTO.getIncomes());
    assertEquals(yearBalance.getSalary(), yearBalanceDTO.getSalary());
    assertEquals(yearBalance.getResult(), yearBalanceDTO.getResult());
  }

  public static void assertBankAndBankDtoAreEquals(Bank bank, BankDTO bankDTO) {
    assertEquals(bank.getId(), bankDTO.getId());
    assertEquals(bank.getName(), bankDTO.getName());
  }

  public static void assertBankBalanceAndDtoAreEqual(
      BankBalance bankBalance, BankBalanceDTO bankBalanceDTO) {
    assertEquals(bankBalance.getId(), bankBalanceDTO.getId());
    assertEquals(bankBalance.getExpenses(), bankBalanceDTO.getExpenses());
    assertEquals(bankBalance.getIncomes(), bankBalanceDTO.getIncomes());
    assertEquals(bankBalance.getResult(), bankBalanceDTO.getResult());
    assertEquals(bankBalance.getSalary(), bankBalanceDTO.getSalary());
    assertBankAndBankDtoAreEquals(bankBalance.getBank(), bankBalanceDTO.getBank());
  }

  public static void assertMonthBalanceAndDtoAreEqual(
      MonthBalance monthBalance, MonthBalanceDTO monthBalanceDTO) {
    assertEquals(monthBalance.getId(), monthBalanceDTO.getId());
    assertEquals(monthBalance.getMonth(), monthBalanceDTO.getMonth());
    assertEquals(monthBalance.getExpenses(), monthBalanceDTO.getExpenses());
    assertEquals(monthBalance.getIncomes(), monthBalanceDTO.getIncomes());
    assertEquals(monthBalance.getResult(), monthBalanceDTO.getResult());
    assertEquals(monthBalance.getSalary(), monthBalanceDTO.getSalary());
    for (int i = 0; i < monthBalance.getBankBalanceList().size(); i++) {
      assertBankBalanceAndDtoAreEqual(
          monthBalance.getBankBalanceList().get(i), monthBalanceDTO.getBankBalances().get(i));
    }
  }
}
