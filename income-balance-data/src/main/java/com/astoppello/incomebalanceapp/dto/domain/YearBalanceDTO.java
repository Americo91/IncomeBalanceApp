package com.astoppello.incomebalanceapp.dto.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.Set;
import java.util.TreeSet;

/**
 * Created by @author stopp on 16/11/2020
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class YearBalanceDTO implements Comparable {
    private Long id;
    @NonNull
    private Integer year;
    @Nullable
    private String salary;
    @Nullable
    private String incomes;
    @Nullable
    private String expenses;
    @Nullable
    private String result;

    @JsonProperty("monthBalances")
    @Nullable
    private TreeSet<MonthBalanceDTO> monthBalances;

    @JsonProperty("bankBalances")
    @Nullable
    private Set<BankBalanceDTO> bankBalances;

    @Override
    public int compareTo(Object o) {
        return Integer.compare(year, ((YearBalanceDTO) o).getYear()) * -1;
    }
}
