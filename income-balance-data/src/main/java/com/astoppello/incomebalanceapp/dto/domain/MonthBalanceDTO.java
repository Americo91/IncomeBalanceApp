package com.astoppello.incomebalanceapp.dto.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.lang.Nullable;

import java.math.BigDecimal;
import java.util.List;

/** Created by @author stopp on 20/12/2020 */
@Data
public class MonthBalanceDTO {
  private Long id;
  @Nullable private String month;
  @Nullable private BigDecimal salary;
  @Nullable private BigDecimal expenses;
  @Nullable private BigDecimal incomes;
  @Nullable private BigDecimal result;
  @JsonProperty("bankBalances")
  @Nullable private List<BankBalanceDTO> bankBalances;
  @Nullable private Long yearBalanceId;
}
