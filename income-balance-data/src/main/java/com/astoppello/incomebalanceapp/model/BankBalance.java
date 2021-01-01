package com.astoppello.incomebalanceapp.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
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
@Table(name = "bankBalances")
public class BankBalance extends AbstractBalanceEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank")
    private Bank bank;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "monthBalance_id")
  private MonthBalance monthBalance;

    @Builder
    public BankBalance(Long id, BigDecimal salary, BigDecimal expenses, BigDecimal incomes, BigDecimal result,
                       Bank bank) {
        super(id, salary, expenses, incomes, result);
        this.bank = bank;
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
        return Objects.equals(bank, that.bank) && Objects.equals(monthBalance, that.monthBalance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), bank, monthBalance);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", BankBalance.class.getSimpleName() + "[", "]").merge(super.getStringJoiner())
                .add("bank=" + bank)
                .toString();
    }
}
