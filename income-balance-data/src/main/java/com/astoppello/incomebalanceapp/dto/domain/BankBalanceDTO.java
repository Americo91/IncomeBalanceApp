package com.astoppello.incomebalanceapp.dto.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.math.BigDecimal;

/**
 * Created by @author stopp on 28/11/2020
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BankBalanceDTO implements Comparable {
    private Long id;
    @Nullable
    private BankDTO bank;
    @Nullable
    private String incomes;
    @Nullable
    private String expenses;
    @Nullable
    private String result;
    @Nullable
    private Long monthBalanceId;
    @Nullable
    private Long yearBalanceId;

    @Override
    public int compareTo(Object o) {
        return Long.compare(id, ((BankBalanceDTO) o).getId());
    }
}
