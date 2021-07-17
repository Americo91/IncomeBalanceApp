package com.astoppello.incomebalanceapp.dto.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.math.BigDecimal;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by @author stopp on 16/11/2020
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class YearBalanceDTO extends BalanceDTO implements Comparable {
    private Long id;
    @NonNull
    private Integer year;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Nullable
    private BigDecimal salary;

    @JsonProperty("monthBalances")
    @Nullable
    private TreeSet<MonthBalanceDTO> monthBalances;

    @JsonProperty("bankBalances")
    @Nullable
    private Set<BankBalanceDTO> bankBalances;
    @Nullable
    private String savings;

    @Override
    public int compareTo(Object o) {
        return Integer.compare(year, ((YearBalanceDTO) o).getYear()) * -1;
    }
}
