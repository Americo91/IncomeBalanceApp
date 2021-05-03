package com.astoppello.incomebalanceapp.utils;

import com.astoppello.incomebalanceapp.model.BankBalance;
import com.astoppello.incomebalanceapp.model.MonthBalance;
import com.astoppello.incomebalanceapp.model.YearBalance;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class YearBalanceUtilsTest {

    YearBalance yearBalance = new YearBalance();

    @BeforeAll
    void setup() {
        Set<BankBalance> bankBalanceList =
                Set.of(
                        BankBalance.builder()
                                   .id(1L)
                                   .incomes(new BigDecimal("200.54"))
                                   .expenses(new BigDecimal("130.40"))
                                   .result(new BigDecimal("50.93"))
                                   .build(),
                        BankBalance.builder()
                                   .id(2L)
                                   .incomes(new BigDecimal("145.79"))
                                   .expenses(new BigDecimal("130.40"))
                                   .result(new BigDecimal("-20.45"))
                                   .build());
        MonthBalance monthBalance = MonthBalance.builder()
                                                .id(1L)
                                                .salary(new BigDecimal("2498.04"))
                                                .build();
        monthBalance.setBankBalanceSet(bankBalanceList);
        MonthBalanceUtils.computeMontlyAmount(monthBalance);
        Set<MonthBalance> monthBalanceList = Set.of(monthBalance, MonthBalance.builder()
                                                                              .id(2L)
                                                                              .salary(new BigDecimal(
                                                                                      "1340.67"))
                                                                              .build());
        yearBalance.setBankBalanceSet(bankBalanceList);
        yearBalance.setMonthBalanceSet(monthBalanceList);
    }

    @Test
    void computeYearlyAmount() {
        YearBalanceUtils.computeYearlyAmount(yearBalance);
        assertEquals("3838.71", yearBalance.getSalary()
                                           .toString());
        assertEquals("346.33", yearBalance.getIncomes()
                                          .toString());
        assertEquals("260.80", yearBalance.getExpenses()
                                          .toString());
        assertEquals("30.48", yearBalance.getResult()
                                         .toString());
    }
}