package com.astoppello.incomebalanceapp.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * Created by @author stopp on 20/12/2020
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "monthBalances")
public class MonthBalance extends AbstractBalanceEntity {

    @Nullable
    private String month;

    @Nullable
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "monthBalance")
    private List<BankBalance> bankBalanceList = new LinkedList<>();

    @Nullable
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
            String month,
            List<BankBalance> bankBalanceList) {
        super(id, expenses, incomes, result);
        this.month = month;
        this.salary = salary;
        if (CollectionUtils.isNotEmpty(bankBalanceList)) {
            this.bankBalanceList = bankBalanceList;
        }
    }

    public MonthBalance addBankBalance(BankBalance bankBalance) {
        bankBalance.setMonthBalance(this);
        bankBalanceList.add(bankBalance);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MonthBalance)) return false;
        if (!super.equals(o)) return false;
        MonthBalance that = (MonthBalance) o;
        return Objects.equals(month, that.month)
                && Objects.equals(salary, that.salary)
                && Objects.equals(bankBalanceList, that.bankBalanceList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), month, salary, bankBalanceList);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", MonthBalance.class.getSimpleName() + "[", "]")
                .merge(super.getStringJoiner())
                .add("yearBalanceId= " + ((yearBalance != null) ? yearBalance.getId() : "null"))
                .add("bankBalanceList= " + (CollectionUtils.isNotEmpty(bankBalanceList) ? bankBalanceList
                        .toString() : "null"))
                .add("month= " + month)
                .add("salary= " + salary)
                .toString();
    }
}
