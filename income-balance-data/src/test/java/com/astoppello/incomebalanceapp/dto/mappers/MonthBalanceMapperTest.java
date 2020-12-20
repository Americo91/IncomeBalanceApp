package com.astoppello.incomebalanceapp.dto.mappers;

import com.astoppello.incomebalanceapp.dto.domain.BankBalanceDTO;
import com.astoppello.incomebalanceapp.dto.domain.MonthBalanceDTO;
import com.astoppello.incomebalanceapp.model.MonthBalance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by @author stopp on 20/12/2020
 */
public class MonthBalanceMapperTest {
    private MonthBalanceMapper monthBalanceMapper;
    private static final Long ID = 1L;
    private static final String MONTH = "September";
    private final BigDecimal expenses = BigDecimal.valueOf(100);
    private final BigDecimal salary = BigDecimal.valueOf(150);
    private final BigDecimal incomes = BigDecimal.valueOf(170);
    private final BigDecimal result = BigDecimal.valueOf(190);

    @BeforeEach
    void setUp() { monthBalanceMapper = MonthBalanceMapper.INSTANCE; }

    @Test
    void monthBalanceDtoToMonthBalance() {
        MonthBalanceDTO monthBalanceDTO = createMonthBalanceDto();
        MonthBalance monthBalance = monthBalanceMapper.monthBalanceDtoToMonthBalance(monthBalanceDTO);
        assertEquals(ID, monthBalanceDTO.getId());
        assertEquals(result, monthBalanceDTO.getResult());
        assertEquals(expenses, monthBalanceDTO.getExpenses());
        assertEquals(salary, monthBalanceDTO.getSalary());
        assertEquals(incomes, monthBalanceDTO.getIncomes());
        assertEquals(MONTH, monthBalanceDTO.getMonth());
    }

    @Test
    void monthBalanceToMonthBalanceDto() {
        MonthBalance monthBalance = createMonthBalance();
        MonthBalanceDTO monthBalanceDTO = monthBalanceMapper.monthBalanceToMonthBalanceDto(monthBalance);
        assertEquals(ID, monthBalance.getId());
        assertEquals(result, monthBalance.getResult());
        assertEquals(expenses, monthBalance.getExpenses());
        assertEquals(salary, monthBalance.getSalary());
        assertEquals(incomes, monthBalance.getIncomes());
        assertEquals(MONTH, monthBalance.getMonth());
    }

    private MonthBalance createMonthBalance() {
        return MonthBalance.builder()
                .id(ID)
                .month(MONTH)
                .expenses(expenses)
                .incomes(incomes)
                .result(result)
                .salary(salary)
                .build();
    }

    private MonthBalanceDTO createMonthBalanceDto() {
        MonthBalanceDTO monthBalanceDTO = new MonthBalanceDTO();
        monthBalanceDTO.setMonth(MONTH);
        monthBalanceDTO.setExpenses(expenses);
        monthBalanceDTO.setSalary(salary);
        monthBalanceDTO.setId(ID);
        monthBalanceDTO.setIncomes(incomes);
        monthBalanceDTO.setResult(result);
        return monthBalanceDTO;
    }
}
