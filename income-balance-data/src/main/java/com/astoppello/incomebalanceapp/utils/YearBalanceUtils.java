package com.astoppello.incomebalanceapp.utils;

import com.astoppello.incomebalanceapp.model.BankBalance;
import com.astoppello.incomebalanceapp.model.MonthBalance;
import com.astoppello.incomebalanceapp.model.YearBalance;
import org.apache.commons.collections4.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.Objects;

public class YearBalanceUtils {
    public static void computeYearlyAmount(YearBalance yearBalance) {
        yearBalance.setIncomes(computeIncomes(yearBalance));
        yearBalance.setExpenses(computeExpenses(yearBalance));
        yearBalance.setSalary(computeSalaries(yearBalance));
        yearBalance.setResult(computeResults(yearBalance));
        yearBalance.setSavings(computeSavings(yearBalance.getResult(), yearBalance.getIncomes()));
    }

    private static String computeSavings(BigDecimal result, BigDecimal incomes) {
        if (incomes.compareTo(BigDecimal.ZERO) == 0)
            return "NA";
        NumberFormat percentage = NumberFormat.getPercentInstance();
        return percentage.format(result.divide(incomes, RoundingMode.HALF_UP));
    }

    private static BigDecimal computeSalaries(YearBalance yearBalance) {
        return CollectionUtils
                .emptyIfNull(yearBalance.getMonthBalanceSet())
                .stream()
                .filter(Objects::nonNull)
                .map(MonthBalance::getSalary)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private static BigDecimal computeResults(YearBalance yearBalance) {
        return CollectionUtils
                .emptyIfNull(yearBalance.getMonthBalanceSet())
                .stream()
                .map(MonthBalance::getBankBalanceSet)
                .flatMap(Collection::stream)
                .filter(Objects::nonNull)
                .map(BankBalance::getResult)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private static BigDecimal computeExpenses(YearBalance yearBalance) {
        return CollectionUtils
                .emptyIfNull(yearBalance.getMonthBalanceSet())
                .stream()
                .map(MonthBalance::getBankBalanceSet)
                .flatMap(Collection::stream)
                .filter(Objects::nonNull)
                .map(BankBalance::getExpenses)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private static BigDecimal computeIncomes(YearBalance yearBalance) {
        return CollectionUtils
                .emptyIfNull(yearBalance.getMonthBalanceSet())
                .stream()
                .map(MonthBalance::getBankBalanceSet)
                .flatMap(Collection::stream)
                .filter(Objects::nonNull)
                .map(BankBalance::getIncomes)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
