package com.astoppello.incomebalanceapp.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

/** Created by @author stopp on 15/11/2020 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "yearbalances")
public class YearBalance extends AbstractBalanceEntity {
  private Integer year;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "yearBalance")
  private List<MonthBalance> monthBalanceList = new LinkedList<>();

  @Builder
  public YearBalance(
      Long id,
      BigDecimal salary,
      BigDecimal expenses,
      BigDecimal incomes,
      BigDecimal result,
      Integer year,
      List<MonthBalance> monthBalanceList) {
    super(id, salary, expenses, incomes, result);
    this.year = year;
    if (CollectionUtils.isNotEmpty(monthBalanceList)) {
      this.monthBalanceList = monthBalanceList;
    }
  }

  public YearBalance addMonthBalance(MonthBalance monthBalance) {
    monthBalance.setYearBalance(this);
    monthBalanceList.add(monthBalance);
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof YearBalance)) return false;
    if (!super.equals(o)) return false;
    YearBalance that = (YearBalance) o;
    return Objects.equals(year, that.year)
        && Objects.equals(monthBalanceList, that.monthBalanceList);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), year, monthBalanceList);
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", YearBalance.class.getSimpleName() + "[", "]")
        .merge(super.getStringJoiner())
        .add("MonthBalanceList=" + monthBalanceList.toString())
        .add("year=" + year)
        .toString();
  }
}
