package com.astoppello.incomebalanceapp.dto.domain;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by @author stopp on 16/11/2020
 */
@Data
public class YearBalanceDTO {
    private Long id;
    private Integer year;
    private BigDecimal salary;
    private BigDecimal expenses;
    private BigDecimal incomes;
    private BigDecimal result;
}
