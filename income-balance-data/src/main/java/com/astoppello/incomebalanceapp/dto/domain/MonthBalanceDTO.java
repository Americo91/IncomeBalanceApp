package com.astoppello.incomebalanceapp.dto.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.lang.Nullable;

import java.util.Set;

/** Created by @author stopp on 20/12/2020 */
@Data
public class MonthBalanceDTO {
  private Long id;
  @Nullable private String month;
  @Nullable private String salary;
  @Nullable private String expenses;
  @Nullable private String incomes;
  @Nullable private String result;
  @JsonProperty("bankBalances")
  @Nullable private Set<BankBalanceDTO> bankBalances;
  @Nullable private Long yearBalanceId;
}
