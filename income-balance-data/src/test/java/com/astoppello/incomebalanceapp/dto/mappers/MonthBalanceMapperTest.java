package com.astoppello.incomebalanceapp.dto.mappers;

import com.astoppello.incomebalanceapp.dto.domain.BankBalanceDTO;
import com.astoppello.incomebalanceapp.dto.domain.BankDTO;
import com.astoppello.incomebalanceapp.dto.domain.MonthBalanceDTO;
import com.astoppello.incomebalanceapp.model.Bank;
import com.astoppello.incomebalanceapp.model.BankBalance;
import com.astoppello.incomebalanceapp.model.MonthBalance;
import com.astoppello.incomebalanceapp.model.YearBalance;
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
    String expenses = "100.00", salary = "150.00", incomes = "170.00", result = "190.00";
    private final BigDecimal EXPENSES = new BigDecimal(expenses);
    private final BigDecimal SALARY = new BigDecimal(salary);
    private final BigDecimal INCOMES = new BigDecimal(incomes);
    private final BigDecimal RESULT = new BigDecimal(result);


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
                           .expenses(EXPENSES)
                           .incomes(INCOMES)
                           .result(RESULT)
                           .salary(SALARY)
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
