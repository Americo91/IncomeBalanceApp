package com.astoppello.incomebalanceapp.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Created by @author stopp on 15/11/2020
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "yearbalance")
public class YearBalance implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer year;
    private BigDecimal salary;
    private BigDecimal expenses;
    private BigDecimal incomes;
    private BigDecimal result;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        YearBalance that = (YearBalance) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "YearBalance{" +
                "id=" + id +
                ", year=" + year +
                ", salary=" + salary +
                ", expenses=" + expenses +
                ", incomes=" + incomes +
                ", result=" + result +
                '}';
    }
}
