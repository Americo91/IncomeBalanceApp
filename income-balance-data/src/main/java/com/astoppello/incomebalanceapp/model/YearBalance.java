package com.astoppello.incomebalanceapp.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * Created by @author stopp on 15/11/2020
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "YearBalance")
public class YearBalance extends AbstractBalanceEntity {
    private Integer year;

    @Builder
    public YearBalance(Long id, BigDecimal salary, BigDecimal expenses, BigDecimal incomes, BigDecimal result,
                       Integer year) {
        super(id, salary, expenses, incomes, result);
        this.year = year;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof YearBalance))
            return false;
        if (!super.equals(o))
            return false;
        YearBalance that = (YearBalance) o;
        return Objects.equals(year, that.year);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), year);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", YearBalance.class.getSimpleName() + "[", "]")
                .merge(super.getStringJoiner())
                .add("year=" + year)
                .toString();
    }
}
