package com.astoppello.incomebalanceapp.utils;

import com.astoppello.incomebalanceapp.model.BankBalance;

import java.math.BigDecimal;

/**
 * Created by @author stopp on 15/02/2021
 */
public class BankBalanceUtils {
    public static BigDecimal computeResult(BankBalance bankBalance) {
        return bankBalance.getIncomes().subtract(bankBalance.getExpenses());
    }
}
