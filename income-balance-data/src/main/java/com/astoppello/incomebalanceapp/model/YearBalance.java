package com.astoppello.incomebalanceapp.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;

/**
 * Created by @author stopp on 15/11/2020
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class YearBalance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer year;
    private BigDecimal salary;
    private BigDecimal expenses;
    private BigDecimal incomes;
    private BigDecimal result;
}
