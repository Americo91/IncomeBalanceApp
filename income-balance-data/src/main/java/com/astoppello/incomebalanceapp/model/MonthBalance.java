package com.astoppello.incomebalanceapp.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

/** Created by @author stopp on 20/12/2020 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "monthBalances")
public class MonthBalance extends AbstractBalanceEntity {

  private String month;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "monthBalance")
  private List<BankBalance> bankBalanceList = new LinkedList<>();

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "yearBalance_id")
  private YearBalance yearBalance;

  @Builder
  public MonthBalance(
      Long id,
      BigDecimal salary,
      BigDecimal expenses,
      BigDecimal incomes,
      BigDecimal result,
      String month,
      List<BankBalance> bankBalanceList) {
    super(id, salary, expenses, incomes, result);
    this.month = month;
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
        && Objects.equals(bankBalanceList, that.bankBalanceList);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), month, bankBalanceList);
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", MonthBalance.class.getSimpleName() + "[", "]")
        .merge(super.getStringJoiner())
        .add("bankBalanceLis=" + (CollectionUtils.isNotEmpty(bankBalanceList) ? bankBalanceList.toString() : "null"))
        .add("month=" + month)
        .toString();
  }
}
