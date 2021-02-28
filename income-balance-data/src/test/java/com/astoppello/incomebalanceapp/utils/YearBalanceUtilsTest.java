package com.astoppello.incomebalanceapp.utils;

import com.astoppello.incomebalanceapp.model.BankBalance;
import com.astoppello.incomebalanceapp.model.MonthBalance;
import com.astoppello.incomebalanceapp.model.YearBalance;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class YearBalanceUtilsTest {

    YearBalance yearBalance = new YearBalance();


    @BeforeAll
    void setup() {
        List<BankBalance> bankBalanceList =
                List.of(
                        BankBalance.builder().incomes(new BigDecimal("200.54"))
                                   .expenses(new BigDecimal("130.40")).result(new BigDecimal("50.93")).build(),
                        BankBalance.builder().incomes(new BigDecimal("145.79"))
                                   .expenses(new BigDecimal("130.40")).result(new BigDecimal("-20.45")).build()
                );
        MonthBalance monthBalance = MonthBalance.builder().salary(new BigDecimal("2498.04")).build();
        monthBalance.setBankBalanceList(bankBalanceList);
        MonthBalanceUtils.computeMontlyAmount(monthBalance);
        List<MonthBalance> monthBalanceList = List.of(
                monthBalance,
                MonthBalance.builder().salary(new BigDecimal("1340.67")).build()
        );
        yearBalance.setBankBalanceList(bankBalanceList);
        yearBalance.setMonthBalanceList(monthBalanceList);
    }

    @Test
    void computeYearlyAmount() {
        YearBalanceUtils.computeYearlyAmount(yearBalance);
        assertEquals("3838.71", yearBalance.getSalary().toString());
        assertEquals("346.33", yearBalance.getIncomes().toString());
        assertEquals("260.80", yearBalance.getExpenses().toString());
        assertEquals("30.48", yearBalance.getResult().toString());
    }
}