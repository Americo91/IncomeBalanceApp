package com.astoppello.incomebalanceapp.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * Created by @author stopp on 28/11/2020
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "BankBalance")
public class BankBalance extends AbstractBalanceEntity {

    private String monthName;

    @Builder
    public BankBalance(Long id, BigDecimal salary, BigDecimal expenses, BigDecimal incomes, BigDecimal result,
                       String monthName) {
        super(id, salary, expenses, incomes, result);
        this.monthName = monthName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof BankBalance))
            return false;
        if (!super.equals(o))
            return false;
        BankBalance that = (BankBalance) o;
        return Objects.equals(monthName, that.monthName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), monthName);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", BankBalance.class.getSimpleName() + "[", "]")
                .merge(super.getStringJoiner())
                .add("monthName=" + monthName)
                .toString();
    }
}
