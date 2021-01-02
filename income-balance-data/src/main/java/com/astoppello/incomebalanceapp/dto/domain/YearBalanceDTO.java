package com.astoppello.incomebalanceapp.dto.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/** Created by @author stopp on 16/11/2020 */
@Data
public class YearBalanceDTO {
  private Long id;
  private Integer year;
  private BigDecimal salary;
  private BigDecimal expenses;
  private BigDecimal incomes;
  private BigDecimal result;
  @JsonProperty("monthBalances")
  private List<MonthBalanceDTO> monthBalances;
}
