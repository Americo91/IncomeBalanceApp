package com.astoppello.incomebalanceapp.dto.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/** Created by @author stopp on 20/12/2020 */
@Data
public class MonthBalanceDTO {
  private Long id;
  private String month;
  private BigDecimal salary;
  private BigDecimal expenses;
  private BigDecimal incomes;
  private BigDecimal result;
  @JsonProperty("bankBalances")
  private List<BankBalanceDTO> bankBalances;
  private Long yearBalanceId;
}
