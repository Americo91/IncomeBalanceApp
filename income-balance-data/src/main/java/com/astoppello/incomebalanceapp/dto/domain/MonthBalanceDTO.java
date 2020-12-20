package com.astoppello.incomebalanceapp.dto.domain;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by @author stopp on 20/12/2020
 */
@Data
public class MonthBalanceDTO {
    private Long id;
    private String month;
    private BigDecimal salary;
    private BigDecimal expenses;
    private BigDecimal incomes;
    private BigDecimal result;
}
