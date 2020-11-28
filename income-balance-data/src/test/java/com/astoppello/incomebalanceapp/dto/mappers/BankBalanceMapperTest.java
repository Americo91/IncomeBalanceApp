package com.astoppello.incomebalanceapp.dto.mappers;

import com.astoppello.incomebalanceapp.dto.domain.BankBalanceDTO;
import com.astoppello.incomebalanceapp.dto.domain.BankDTO;
import com.astoppello.incomebalanceapp.model.Bank;
import com.astoppello.incomebalanceapp.model.BankBalance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class BankBalanceMapperTest {

    private BankBalanceMapper bankBalanceMapper;
    private static final Long ID = 1L;
    private final BigDecimal expenses = BigDecimal.valueOf(100);
    private final BigDecimal salary = BigDecimal.valueOf(150);
    private final BigDecimal incomes = BigDecimal.valueOf(170);
    private final BigDecimal result = BigDecimal.valueOf(190);

    @BeforeEach
    void setUp() {
        bankBalanceMapper = BankBalanceMapper.INSTANCE;
    }

    @Test
    void bankBalanceDtoToBankBalance() {
        BankBalanceDTO bankBalanceDTO = createBankBalanceDto();
        BankBalance bankBalance = bankBalanceMapper.bankBalanceDtoToBankBalance(bankBalanceDTO);
        assertNotNull(bankBalance);
        assertEquals(ID, bankBalance.getId());
        assertEquals(result, bankBalance.getResult());
        assertEquals(expenses, bankBalance.getExpenses());
        assertEquals(salary, bankBalance.getSalary());
        assertEquals(incomes, bankBalance.getIncomes());
        assertEquals(BankMapperTest.NAME, bankBalance.getBank()
                                                     .getName());
        assertEquals(ID, bankBalance.getBank()
                                    .getId());
    }

    @Test
    void bankBalanceToBankBalanceDto() {
        BankBalance bankBalance = createBankBalance();
        BankBalanceDTO bankBalanceDTO = bankBalanceMapper.bankBalanceToBankBalanceDTO(bankBalance);
        assertNotNull(bankBalanceDTO);
        assertEquals(ID, bankBalanceDTO.getId());
        assertEquals(result, bankBalanceDTO.getResult());
        assertEquals(expenses, bankBalanceDTO.getExpenses());
        assertEquals(salary, bankBalanceDTO.getSalary());
        assertEquals(incomes, bankBalance.getIncomes());
        assertEquals(BankMapperTest.NAME, bankBalanceDTO.getBank()
                                                     .getName());
        assertEquals(ID, bankBalanceDTO.getBank()
                                    .getId());

    }

    private BankBalance createBankBalance() {
        return BankBalance.builder()
                          .id(ID)
                          .bank(Bank.builder()
                                    .id(ID)
                                    .name(BankMapperTest.NAME)
                                    .build())
                          .expenses(expenses)
                          .incomes(incomes)
                          .salary(salary)
                          .result(result)
                          .build();
    }

    private BankBalanceDTO createBankBalanceDto() {
        BankDTO bankDto = new BankDTO();
        bankDto.setName(BankMapperTest.NAME);
        bankDto.setId(ID);

        BankBalanceDTO bankBalanceDTO = new BankBalanceDTO();
        bankBalanceDTO.setBank(bankDto);
        bankBalanceDTO.setExpenses(expenses);
        bankBalanceDTO.setSalary(salary);
        bankBalanceDTO.setId(ID);
        bankBalanceDTO.setIncomes(incomes);
        bankBalanceDTO.setResult(result);
        return bankBalanceDTO;
    }
}