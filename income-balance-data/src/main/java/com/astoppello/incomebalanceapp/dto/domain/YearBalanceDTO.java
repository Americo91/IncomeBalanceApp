package com.astoppello.incomebalanceapp.dto.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.lang.Nullable;

import java.math.BigDecimal;
import java.util.List;

/** Created by @author stopp on 16/11/2020 */
@Data
public class YearBalanceDTO {
  private Long id;
  @Nullable private Integer year;
  @Nullable private String salary;
  @Nullable private String expenses;
  @Nullable private String incomes;
  @Nullable private String result;
  @JsonProperty("monthBalances")
  @Nullable private List<MonthBalanceDTO> monthBalances;
  @JsonProperty("bankBalances")
  @Nullable private List<BankBalanceDTO> bankBalances;
}
