package com.astoppello.incomebalanceapp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.StringJoiner;

/** Created by @author stopp on 28/11/2020 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class AbstractBalanceEntity extends AbstractBaseEntity {

  private BigDecimal salary;
  private BigDecimal expenses;
  private BigDecimal incomes;
  private BigDecimal result;

  public AbstractBalanceEntity(
      Long id, BigDecimal salary, BigDecimal expenses, BigDecimal incomes, BigDecimal result) {
    super(id);
    this.salary = salary;
    this.expenses = expenses;
    this.incomes = incomes;
    this.result = result;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof AbstractBalanceEntity)) return false;
    if (!super.equals(o)) return false;
    AbstractBalanceEntity that = (AbstractBalanceEntity) o;
    return Objects.equals(salary, that.salary)
        && Objects.equals(expenses, that.expenses)
        && Objects.equals(incomes, that.incomes)
        && Objects.equals(result, that.result);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), salary, expenses, incomes, result);
  }

  @Override
  public StringJoiner getStringJoiner() {
    return new StringJoiner(", ")
        .add("salary=" + salary)
        .add("expenses=" + expenses)
        .add("incomes=" + incomes)
        .add("result=" + result)
        .merge(super.getStringJoiner());
  }
}
