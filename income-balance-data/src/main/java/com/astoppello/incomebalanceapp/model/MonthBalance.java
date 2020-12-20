package com.astoppello.incomebalanceapp.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * Created by @author stopp on 20/12/2020
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "MonthBalance")
public class MonthBalance extends AbstractBalanceEntity {

    private String month;

    @Builder
    public MonthBalance(Long id, BigDecimal salary, BigDecimal expenses, BigDecimal incomes, BigDecimal result,
                        String month) {
        super(id, salary, expenses, incomes, result);
        this.month = month;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof MonthBalance))
            return false;
        if (!super.equals(o))
            return false;
        MonthBalance that = (MonthBalance) o;
        return Objects.equals(month, that.month);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), month);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", MonthBalance.class.getSimpleName() + "[", "]").merge(super.getStringJoiner())
                .add("month=" + month)
                .toString();
    }
}
