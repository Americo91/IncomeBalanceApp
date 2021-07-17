package com.astoppello.incomebalanceapp.dto.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.lang.Nullable;

import java.math.BigDecimal;

/**
 * Created by @author stopp on 28/11/2020
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BankBalanceDTO extends BalanceDTO implements Comparable {
    private Long id;
    @Nullable
    private BankDTO bank;

    @Nullable
    private Long monthBalanceId;
    @Nullable
    private Long yearBalanceId;

    @Override
    public int compareTo(Object o) {
        return Long.compare(id, ((BankBalanceDTO) o).getId());
    }
}
