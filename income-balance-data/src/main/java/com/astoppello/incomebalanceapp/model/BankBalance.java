package com.astoppello.incomebalanceapp.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.StringJoiner;

/** Created by @author stopp on 28/11/2020 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "bankBalances")
public class BankBalance extends AbstractBalanceEntity {

  @Nullable
  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "bank")
  private Bank bank;

  @Nullable
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "monthBalance_id")
  private MonthBalance monthBalance;

  @Nullable
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
    if (this == o) return true;
    if (!(o instanceof BankBalance)) return false;
    if (!super.equals(o)) return false;
    BankBalance that = (BankBalance) o;
    return Objects.equals(bank, that.bank)
        && Objects.equals(monthBalance, that.monthBalance)
        && Objects.equals(yearBalance, that.yearBalance);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), bank, monthBalance, yearBalance);
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", BankBalance.class.getSimpleName() + "[", "]")
        .merge(super.getStringJoiner())
        .add("bank=" + bank)
        .add(
            "monthBalanceId:"
                + (monthBalance != null && monthBalance.getId() != null
                    ? monthBalance.getId()
                    : "null"))
        .add(
            "yearBalanceId"
                + ((yearBalance != null && yearBalance.getId() != null)
                    ? yearBalance.getId()
                    : "null"))
        .toString();
  }
}
