package com.astoppello.incomebalanceapp.utils;

import com.astoppello.incomebalanceapp.dto.domain.BankBalanceDTO;
import com.astoppello.incomebalanceapp.dto.domain.BankDTO;
import com.astoppello.incomebalanceapp.dto.domain.MonthBalanceDTO;
import com.astoppello.incomebalanceapp.dto.domain.YearBalanceDTO;
import com.astoppello.incomebalanceapp.model.Bank;
import com.astoppello.incomebalanceapp.model.BankBalance;
import com.astoppello.incomebalanceapp.model.MonthBalance;
import com.astoppello.incomebalanceapp.model.YearBalance;
import org.junit.jupiter.api.Assertions;

import static org.junit.jupiter.api.Assertions.assertEquals;

/** Created by @author stopp on 21/12/2020 */
public class ModelEqualUtils {

  public static void assertYearBalanceAndYearBalanceDtoAreEquals(
      YearBalance yearBalance, YearBalanceDTO yearBalanceDTO) {
    Assertions.assertEquals(yearBalance.getId(), yearBalanceDTO.getId());
    Assertions.assertEquals(yearBalance.getYear(), yearBalanceDTO.getYear());
    Assertions.assertEquals(yearBalance.getExpenses(), yearBalanceDTO.getExpenses());
    Assertions.assertEquals(yearBalance.getIncomes(), yearBalanceDTO.getIncomes());
    Assertions.assertEquals(yearBalance.getSalary(), yearBalanceDTO.getSalary());
    Assertions.assertEquals(yearBalance.getResult(), yearBalanceDTO.getResult());
  }

  public static void assertBankAndBankDtoAreEquals(Bank bank, BankDTO bankDTO) {
    Assertions.assertEquals(bank.getId(), bankDTO.getId());
    Assertions.assertEquals(bank.getName(), bankDTO.getName());
  }

  public static void assertBankBalanceAndDtoAreEqual(
      BankBalance bankBalance, BankBalanceDTO bankBalanceDTO) {
    Assertions.assertEquals(bankBalance.getId(), bankBalanceDTO.getId());
    Assertions.assertEquals(bankBalance.getExpenses(), bankBalanceDTO.getExpenses());
    Assertions.assertEquals(bankBalance.getIncomes(), bankBalanceDTO.getIncomes());
    Assertions.assertEquals(bankBalance.getResult(), bankBalanceDTO.getResult());
    Assertions.assertEquals(bankBalance.getSalary(), bankBalanceDTO.getSalary());
    if (bankBalance.getYearBalance() != null && bankBalanceDTO.getYearBalanceId() != null) {
      Assertions.assertEquals(bankBalance.getYearBalance().getId(), bankBalanceDTO.getYearBalanceId());
    }
    if (bankBalance.getMonthBalance() != null) {
      Assertions.assertEquals(bankBalance.getMonthBalance().getId(), bankBalanceDTO.getMonthBalanceId());
    }
    assertBankAndBankDtoAreEquals(bankBalance.getBank(), bankBalanceDTO.getBank());
  }

  public static void assertMonthBalanceAndDtoAreEqual(
      MonthBalance monthBalance, MonthBalanceDTO monthBalanceDTO) {
    Assertions.assertEquals(monthBalance.getId(), monthBalanceDTO.getId());
    Assertions.assertEquals(monthBalance.getMonth(), monthBalanceDTO.getMonth());
    Assertions.assertEquals(monthBalance.getExpenses(), monthBalanceDTO.getExpenses());
    Assertions.assertEquals(monthBalance.getIncomes(), monthBalanceDTO.getIncomes());
    Assertions.assertEquals(monthBalance.getResult(), monthBalanceDTO.getResult());
    Assertions.assertEquals(monthBalance.getSalary(), monthBalanceDTO.getSalary());
    if (monthBalance.getYearBalance() != null) {
      Assertions.assertEquals(monthBalance.getYearBalance().getId(), monthBalanceDTO.getYearBalanceId());
    }
  }
}
