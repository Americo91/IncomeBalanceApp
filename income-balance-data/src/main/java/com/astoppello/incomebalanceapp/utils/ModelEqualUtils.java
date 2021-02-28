package com.astoppello.incomebalanceapp.utils;

import com.astoppello.incomebalanceapp.dto.domain.BankBalanceDTO;
import com.astoppello.incomebalanceapp.dto.domain.BankDTO;
import com.astoppello.incomebalanceapp.dto.domain.MonthBalanceDTO;
import com.astoppello.incomebalanceapp.dto.domain.YearBalanceDTO;
import com.astoppello.incomebalanceapp.model.Bank;
import com.astoppello.incomebalanceapp.model.BankBalance;
import com.astoppello.incomebalanceapp.model.MonthBalance;
import com.astoppello.incomebalanceapp.model.YearBalance;
import org.apache.commons.lang3.StringUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by @author stopp on 21/12/2020
 */
public class ModelEqualUtils {

    public static void assertYearBalanceAndYearBalanceDtoAreEquals(
            YearBalance yearBalance, YearBalanceDTO yearBalanceDTO) {
        assertEquals(yearBalance.getId(), yearBalanceDTO.getId());
        assertEquals(yearBalance.getYear(), yearBalanceDTO.getYear());
        assertEquals(
                yearBalance.getExpenses() == null ? yearBalance.getExpenses() : yearBalance.getExpenses().toString(),
                StringUtils.isEmpty(yearBalanceDTO.getExpenses()) ? "0" : yearBalanceDTO.getExpenses());
        assertEquals(
                yearBalance.getIncomes() == null ? yearBalance.getIncomes() : yearBalance.getIncomes().toString(),
                StringUtils.isEmpty(yearBalanceDTO.getIncomes()) ? "0" : yearBalanceDTO.getIncomes());
        assertEquals(
                yearBalance.getSalary() == null ? yearBalance.getSalary() : yearBalance.getSalary().toString(),
                StringUtils.isEmpty(yearBalanceDTO.getSalary()) ? "0" : yearBalanceDTO.getSalary());
        assertEquals(
                yearBalance.getResult() == null ? yearBalance.getResult() : yearBalance.getResult().toString(),
                StringUtils.isEmpty(yearBalanceDTO.getResult()) ? "0" : yearBalanceDTO.getResult());
    }

    public static void assertBankAndBankDtoAreEquals(Bank bank, BankDTO bankDTO) {
        assertEquals(bank.getId(), bankDTO.getId());
        assertEquals(bank.getName(), bankDTO.getName());
    }

    public static void assertBankBalanceAndDtoAreEqual(
            BankBalance bankBalance, BankBalanceDTO bankBalanceDTO) {
        assertEquals(bankBalance.getId(), bankBalanceDTO.getId());
        assertEquals(
                bankBalance.getExpenses() == null ? bankBalance.getExpenses() : bankBalance.getExpenses().toString(),
                StringUtils.isEmpty(bankBalanceDTO.getExpenses()) ? "0" : bankBalanceDTO.getExpenses());
        assertEquals(
                bankBalance.getIncomes() == null ? bankBalance.getIncomes() : bankBalance.getIncomes().toString(),
                StringUtils.isEmpty(bankBalanceDTO.getIncomes()) ? "0" : bankBalanceDTO.getIncomes());
        assertEquals(
                bankBalance.getResult() == null ? bankBalance.getResult() : bankBalance.getResult().toString(),
                StringUtils.isEmpty(bankBalanceDTO.getResult()) ? "0" : bankBalanceDTO.getResult());
        if (bankBalance.getYearBalance() != null && bankBalanceDTO.getYearBalanceId() != null) {
            assertEquals(bankBalance.getYearBalance().getId(), bankBalanceDTO.getYearBalanceId());
        }
        if (bankBalance.getMonthBalance() != null) {
            assertEquals(bankBalance.getMonthBalance().getId(), bankBalanceDTO.getMonthBalanceId());
        }
        assertBankAndBankDtoAreEquals(bankBalance.getBank(), bankBalanceDTO.getBank());
    }

    public static void assertMonthBalanceAndDtoAreEqual(
            MonthBalance monthBalance, MonthBalanceDTO monthBalanceDTO) {
        assertEquals(monthBalance.getId(), monthBalanceDTO.getId());
        assertEquals(monthBalance.getMonth(), monthBalanceDTO.getMonth());
        assertEquals(
                monthBalance.getExpenses() == null ? monthBalance.getExpenses() : monthBalance.getExpenses().toString(),
                monthBalanceDTO.getExpenses());
        assertEquals(
                monthBalance.getIncomes() == null ? monthBalance.getIncomes() : monthBalance.getIncomes().toString(),
                monthBalanceDTO.getIncomes());
        assertEquals(
                monthBalance.getResult() == null ? monthBalance.getResult() : monthBalance.getResult().toString(),
                monthBalanceDTO.getResult());
        assertEquals(
                monthBalance.getSalary() == null ? monthBalance.getSalary() : monthBalance.getSalary().toString(),
                monthBalanceDTO.getSalary());
        if (monthBalance.getYearBalance() != null) {
            assertEquals(monthBalance.getYearBalance().getId(), monthBalanceDTO.getYearBalanceId());
        }
    }
}
