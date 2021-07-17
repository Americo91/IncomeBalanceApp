package com.astoppello.incomebalanceapp.dto.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.lang.Nullable;

import javax.persistence.MappedSuperclass;
import java.math.BigDecimal;

/**
 * Created by @author stopp on 17/07/2021
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
@SuperBuilder
public abstract class BalanceDTO {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Nullable
    private BigDecimal incomes;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Nullable
    private BigDecimal expenses;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Nullable
    private BigDecimal result;
}
