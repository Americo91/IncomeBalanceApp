package com.astoppello.incomebalanceapp.model;

import lombok.*;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * Created by @author stopp on 28/11/2020
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "bankBalances")
public class BankBalance extends AbstractBalanceEntity {

    @NonNull
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "bank")
    private Bank bank;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "monthBalance_id")
    private MonthBalance monthBalance;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "yearBalance_id")
    private YearBalance yearBalance;

    @Builder
    public BankBalance(
            Long id,
            BigDecimal expenses,
            BigDecimal incomes,
            BigDecimal result,
            Bank bank) {
        super(id, expenses, incomes, result);
        this.bank = bank;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof BankBalance))
            return false;
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode());
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", BankBalance.class.getSimpleName() + "[", "]")
                .merge(super.getStringJoiner())
                .add("bank=" + bank)
                .add("monthBalanceId:" + monthBalance.getId())
                .add("yearBalanceId" + yearBalance.getId())
                .toString();
    }
}
