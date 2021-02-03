package com.astoppello.incomebalanceapp.utils;

import com.astoppello.incomebalanceapp.model.BankBalance;
import com.astoppello.incomebalanceapp.model.MonthBalance;
import org.apache.commons.collections4.CollectionUtils;

import java.math.BigDecimal;
import java.util.Objects;

public class MonthBalanceUtils {
    public static void computeMontlyAmount(MonthBalance monthBalance) {
        monthBalance.setIncomes(computeIncomes(monthBalance));
        monthBalance.setExpenses(computeExpenses(monthBalance));
        monthBalance.setResult(computeResults(monthBalance));
    }

    private static BigDecimal computeResults(MonthBalance monthBalance) {
        return CollectionUtils.emptyIfNull(monthBalance.getBankBalanceList())
                              .stream()
                              .filter(Objects::nonNull)
                              .map(BankBalance::getResult)
                              .filter(Objects::nonNull)
                              .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private static BigDecimal computeExpenses(MonthBalance monthBalance) {
        return CollectionUtils.emptyIfNull(monthBalance.getBankBalanceList())
                              .stream()
                              .filter(Objects::nonNull)
                              .map(BankBalance::getExpenses)
                              .filter(Objects::nonNull)
                              .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private static BigDecimal computeIncomes(MonthBalance monthBalance) {
        return CollectionUtils.emptyIfNull(monthBalance.getBankBalanceList())
                              .stream()
                              .filter(Objects::nonNull)
                              .map(BankBalance::getIncomes)
                              .filter(Objects::nonNull)
                              .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
