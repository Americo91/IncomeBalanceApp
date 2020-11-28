package com.astoppello.incomebalanceapp.dto.domain;

import com.astoppello.incomebalanceapp.model.Bank;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by @author stopp on 28/11/2020
 */
@Data
public class BankBalanceDTO {
    private Long id;
    private BankDTO bank;
    private BigDecimal salary;
    private BigDecimal expenses;
    private BigDecimal incomes;
    private BigDecimal result;
}
