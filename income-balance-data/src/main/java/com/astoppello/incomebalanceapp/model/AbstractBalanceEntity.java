package com.astoppello.incomebalanceapp.model;

import lombok.*;

import javax.persistence.MappedSuperclass;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.StringJoiner;

/** Created by @author stopp on 28/11/2020 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class AbstractBalanceEntity extends AbstractBaseEntity {

  private BigDecimal expenses;
  private BigDecimal incomes;
  private BigDecimal result;

  public AbstractBalanceEntity(
      Long id, BigDecimal expenses, BigDecimal incomes, BigDecimal result) {
    super(id);
    this.expenses = expenses;
    this.incomes = incomes;
    this.result = result;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof AbstractBalanceEntity)) return false;
    return super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode());
  }

  @Override
  public StringJoiner getStringJoiner() {
    return new StringJoiner(", ")
        .add("expenses=" + expenses)
        .add("incomes=" + incomes)
        .add("result=" + result)
        .merge(super.getStringJoiner());
  }
}
