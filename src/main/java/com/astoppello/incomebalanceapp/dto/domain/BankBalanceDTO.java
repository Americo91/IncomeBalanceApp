package com.astoppello.incomebalanceapp.dto.domain;

import lombok.Data;
import org.springframework.lang.Nullable;

import java.math.BigDecimal;

/** Created by @author stopp on 28/11/2020 */
@Data
public class BankBalanceDTO {
  private Long id;
  @Nullable private BankDTO bank;
  @Nullable private BigDecimal salary;
  @Nullable private BigDecimal expenses;
  @Nullable private BigDecimal incomes;
  @Nullable private BigDecimal result;
  @Nullable private Long monthBalanceId;
  @Nullable private Long yearBalanceId;
}
