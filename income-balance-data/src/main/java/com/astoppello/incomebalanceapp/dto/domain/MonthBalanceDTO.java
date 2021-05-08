package com.astoppello.incomebalanceapp.dto.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.lang.Nullable;

import java.time.Month;
import java.util.Set;

/**
 * Created by @author stopp on 20/12/2020
 */
@Data
public class MonthBalanceDTO implements Comparable {
    private Long id;
    @Nullable
    private Month month;
    @Nullable
    private String salary;
    @Nullable
    private String incomes;
    @Nullable
    private String expenses;
    @Nullable
    private String result;
    @JsonProperty("bankBalances")
    @Nullable
    private Set<BankBalanceDTO> bankBalances;
    @Nullable
    private Long yearBalanceId;

    @Override
    public int compareTo(Object o) {
        return Integer.compare(month.getValue(),
                ((MonthBalanceDTO) o).getMonth()
                                     .getValue());
    }
}
