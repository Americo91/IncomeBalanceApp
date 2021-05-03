package com.astoppello.incomebalanceapp.utils;

import com.astoppello.incomebalanceapp.model.BankBalance;
import com.astoppello.incomebalanceapp.model.MonthBalance;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MonthBalanceUtilsTest {

    MonthBalance monthBalance;
    BankBalance bankBalanceRevolut;
    BankBalance bankBalanceMediolanum;
    BankBalance bankBalanceBoursorama;

    @BeforeAll
    void setup() {
        bankBalanceMediolanum = BankBalance.builder().id(1L).expenses(new BigDecimal("1105.02")).incomes(new BigDecimal(
                "2478.14")).build();
        bankBalanceBoursorama = BankBalance.builder().id(3L).expenses(new BigDecimal("224.28")).incomes(new BigDecimal(
                "130")).build();
        bankBalanceRevolut = BankBalance.builder().id(2L).expenses(new BigDecimal("186.44")).incomes(new BigDecimal(
                "0.38")).build();
        monthBalance = MonthBalance.builder().id(1L).month("January").bankBalanceSet(Set.of(
                bankBalanceMediolanum,
                bankBalanceRevolut,
                bankBalanceBoursorama)).build();
    }


    @Test
    void computeMontlyAmountTest() {
        MonthBalanceUtils.computeMontlyAmount(monthBalance);
        assertEquals(new BigDecimal("1515.74"), monthBalance.getExpenses());
    }
}