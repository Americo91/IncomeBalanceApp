package com.astoppello.incomebalanceapp.dto.mappers;

import com.astoppello.incomebalanceapp.dto.domain.BankBalanceDTO;
import com.astoppello.incomebalanceapp.dto.domain.BankDTO;
import com.astoppello.incomebalanceapp.dto.domain.MonthBalanceDTO;
import com.astoppello.incomebalanceapp.model.Bank;
import com.astoppello.incomebalanceapp.model.BankBalance;
import com.astoppello.incomebalanceapp.model.MonthBalance;
import com.astoppello.incomebalanceapp.model.YearBalance;
import com.astoppello.incomebalanceapp.utils.ModelEqualUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static com.astoppello.incomebalanceapp.utils.ModelEqualUtils.assertMonthBalanceAndDtoAreEqual;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by @author stopp on 20/12/2020
 */
@SpringBootTest(classes = {MonthBalanceMapperImpl.class, BankBalanceMapperImpl.class, BankMapperImpl.class})
public class MonthBalanceMapperTest {
    @Autowired
    MonthBalanceMapper monthBalanceMapper;
    private static final Long ID = 1L;
    private static final String MONTH = "September";
    private final BigDecimal expenses = BigDecimal.valueOf(100);
    private final BigDecimal salary = BigDecimal.valueOf(150);
    private final BigDecimal incomes = BigDecimal.valueOf(170);
    private final BigDecimal result = BigDecimal.valueOf(190);


    @Test
    void monthBalanceDtoToMonthBalance() {
        MonthBalanceDTO monthBalanceDTO = createMonthBalanceDto();
        MonthBalance monthBalance = monthBalanceMapper.monthBalanceDtoToMonthBalance(monthBalanceDTO);
        assertMonthBalanceAndDtoAreEqual(monthBalance, monthBalanceDTO);
    }

    @Test
    void monthBalanceToMonthBalanceDto() {
        MonthBalance monthBalance = createMonthBalance();
        monthBalance.setYearBalance(YearBalance.builder().id(3L).build());
        MonthBalanceDTO monthBalanceDTO = monthBalanceMapper.monthBalanceToMonthBalanceDto(monthBalance);
        assertMonthBalanceAndDtoAreEqual(monthBalance, monthBalanceDTO);
        assertEquals(3L, monthBalanceDTO.getYearBalanceId());
    }

    private MonthBalance createMonthBalance() {
        return MonthBalance.builder()
                           .id(ID)
                           .month(MONTH)
                           .expenses(expenses)
                           .incomes(incomes)
                           .result(result)
                           .salary(salary)
                           .build()
                           .addBankBalance(BankBalance.builder()
                                                      .id(1L)
                                                      .expenses(new BigDecimal("200"))
                                                      .bank(Bank.builder().id(1L).name("Revolut").build())
                                                      .build());
    }

    private MonthBalanceDTO createMonthBalanceDto() {
        MonthBalanceDTO monthBalanceDTO = new MonthBalanceDTO();
        monthBalanceDTO.setMonth(MONTH);
        monthBalanceDTO.setExpenses(expenses);
        monthBalanceDTO.setSalary(salary);
        monthBalanceDTO.setId(ID);
        monthBalanceDTO.setIncomes(incomes);
        monthBalanceDTO.setResult(result);
        monthBalanceDTO.setBankBalances(List.of(createBankBalanceDto()));
        return monthBalanceDTO;
    }

    private BankBalanceDTO createBankBalanceDto() {
        BankDTO bankDto = new BankDTO();
        bankDto.setName(BankMapperTest.NAME);
        bankDto.setId(ID);

        BankBalanceDTO bankBalanceDTO = new BankBalanceDTO();
        bankBalanceDTO.setBank(bankDto);
        bankBalanceDTO.setExpenses(expenses);
        bankBalanceDTO.setId(ID);
        bankBalanceDTO.setIncomes(incomes);
        bankBalanceDTO.setResult(result);
        return bankBalanceDTO;
    }
}
