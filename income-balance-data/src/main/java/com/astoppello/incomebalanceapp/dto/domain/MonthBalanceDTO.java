package com.astoppello.incomebalanceapp.dto.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.math.BigDecimal;
import java.time.Month;
import java.util.Set;

/**
 * Created by @author stopp on 20/12/2020
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class MonthBalanceDTO extends BalanceDTO implements Comparable {
    private Long id;
    @NonNull
    private Month month;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Nullable
    private BigDecimal salary;
    @JsonProperty("bankBalances")
    @Nullable
    private Set<BankBalanceDTO> bankBalances;
    @Nullable
    private Long yearBalanceId;
    @Nullable
    private Double savingPercentage;

    @Override
    public int compareTo(Object o) {
        return Integer.compare(month.getValue(),
                ((MonthBalanceDTO) o).getMonth()
                                     .getValue());
    }
}
