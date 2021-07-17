package com.astoppello.incomebalanceapp.model;

import lombok.*;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by @author stopp on 15/11/2020
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "yearbalances")
public class YearBalance extends AbstractBalanceEntity {

    @NonNull
    private Integer year;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "yearBalance")
    private Set<MonthBalance> monthBalanceSet = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "yearBalance")
    private Set<BankBalance> bankBalanceSet = new HashSet<>();

    private BigDecimal salary;

    @Builder
    public YearBalance(
            Long id,
            BigDecimal salary,
            BigDecimal expenses,
            BigDecimal incomes,
            BigDecimal result,
            Integer year) {
        super(id, expenses, incomes, result);
        this.year = year;
        this.salary = salary;
    }

    /**
     * Add MonthBalance and its BankBalance to YearBalance
     * @param monthBalance
     * @return
     */
    public YearBalance addMonthBalance(@NonNull MonthBalance monthBalance) {
        monthBalance.setYearBalance(this);
        monthBalanceSet.add(monthBalance);
        CollectionUtils.emptyIfNull(monthBalance.getBankBalanceSet()).forEach(this::addBankBalance);
        return this;
    }

    public YearBalance addBankBalance(@NonNull BankBalance bankBalance) {
        bankBalance.setYearBalance(this);
        bankBalanceSet.add(bankBalance);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof YearBalance)) return false;
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode());
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", YearBalance.class.getSimpleName() + "[", "]")
                .merge(super.getStringJoiner())
                .add(
                        "monthBalanceSet="
                                + (CollectionUtils.isNotEmpty(monthBalanceSet)
                                ? monthBalanceSet.toString()
                                : "null"))
                .add(
                        "bankBalanceSet="
                                + (CollectionUtils.isNotEmpty(bankBalanceSet)
                                ? monthBalanceSet.toString()
                                : "null"))
                .add("year=" + year)
                .add("salary= "+salary)
                .toString();
    }
}
