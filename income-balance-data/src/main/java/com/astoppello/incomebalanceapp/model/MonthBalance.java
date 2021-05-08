package com.astoppello.incomebalanceapp.model;

import lombok.*;
import org.apache.commons.collections4.CollectionUtils;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Month;
import java.util.*;

/**
 * Created by @author stopp on 20/12/2020
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "monthBalances")
public class MonthBalance extends AbstractBalanceEntity {

    @NonNull
    private Month month;

    @NonNull
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "monthBalance")
    private Set<BankBalance> bankBalanceSet = new HashSet<>();

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "yearBalance_id")
    private YearBalance yearBalance;

    private BigDecimal salary;

    @Builder
    public MonthBalance(
            Long id,
            BigDecimal salary,
            BigDecimal expenses,
            BigDecimal incomes,
            BigDecimal result,
            Month month,
            Set<BankBalance> bankBalanceSet) {
        super(id, expenses, incomes, result);
        this.month = month;
        this.salary = salary;
        if (CollectionUtils.isNotEmpty(bankBalanceSet)) {
            this.bankBalanceSet = bankBalanceSet;
        }
    }

    public MonthBalance addBankBalance(@NonNull BankBalance bankBalance) {
        bankBalance.setMonthBalance(this);
        bankBalanceSet.add(bankBalance);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MonthBalance)) return false;
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode());
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", MonthBalance.class.getSimpleName() + "[", "]")
                .merge(super.getStringJoiner())
                .add("yearBalanceId= " + yearBalance.getId())
                .add("bankBalanceList= " + (CollectionUtils.isNotEmpty(bankBalanceSet) ? bankBalanceSet
                        .toString() : "null"))
                .add("month= " + month)
                .add("salary= " + salary)
                .toString();
    }
}
