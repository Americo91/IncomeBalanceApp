package com.astoppello.incomebalanceapp.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * Created by @author stopp on 15/11/2020
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "yearbalances")
public class YearBalance extends AbstractBalanceEntity {

    @Nullable
    private Integer year;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "yearBalance")
    private List<MonthBalance> monthBalanceList = new LinkedList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "yearBalance")
    private List<BankBalance> bankBalanceList = new LinkedList<>();

    private BigDecimal salary;

    @Builder
    public YearBalance(
            Long id,
            BigDecimal salary,
            BigDecimal expenses,
            BigDecimal incomes,
            BigDecimal result,
            Integer year,
            List<MonthBalance> monthBalanceList,
            List<BankBalance> bankBalanceList) {
        super(id, expenses, incomes, result);
        this.year = year;
        this.salary = salary;
        if (CollectionUtils.isNotEmpty(monthBalanceList)) {
            this.monthBalanceList = monthBalanceList;
        }
        if (CollectionUtils.isNotEmpty(bankBalanceList)) {
            this.bankBalanceList = bankBalanceList;
        }
    }

    public YearBalance addMonthBalance(@NonNull MonthBalance monthBalance) {
        monthBalance.setYearBalance(this);
        monthBalanceList.add(monthBalance);
        CollectionUtils.emptyIfNull(monthBalance.getBankBalanceList()).stream().filter(Objects::nonNull)
                       .forEach(this::addBankBalance);
        return this;
    }

    public YearBalance addBankBalance(@NonNull BankBalance bankBalance) {
        bankBalance.setYearBalance(this);
        bankBalanceList.add(bankBalance);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof YearBalance)) return false;
        if (!super.equals(o)) return false;
        YearBalance that = (YearBalance) o;
        return Objects.equals(year, that.year)
                && Objects.equals(salary, that.salary)
                && Objects.equals(monthBalanceList, that.monthBalanceList)
                && Objects.equals(bankBalanceList, that.bankBalanceList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), year, monthBalanceList, bankBalanceList);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", YearBalance.class.getSimpleName() + "[", "]")
                .merge(super.getStringJoiner())
                .add(
                        "MonthBalanceList="
                                + (CollectionUtils.isNotEmpty(monthBalanceList)
                                ? monthBalanceList.toString()
                                : "null"))
                .add(
                        "BankBalanceList="
                                + (CollectionUtils.isNotEmpty(bankBalanceList)
                                ? monthBalanceList.toString()
                                : "null"))
                .add("year=" + year)
                .add("salary= "+salary)
                .toString();
    }
}
