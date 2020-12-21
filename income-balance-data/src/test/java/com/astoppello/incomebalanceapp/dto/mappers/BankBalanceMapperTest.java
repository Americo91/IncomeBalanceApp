package com.astoppello.incomebalanceapp.dto.mappers;

import com.astoppello.incomebalanceapp.dto.domain.BankBalanceDTO;
import com.astoppello.incomebalanceapp.dto.domain.BankDTO;
import com.astoppello.incomebalanceapp.model.Bank;
import com.astoppello.incomebalanceapp.model.BankBalance;
import com.astoppello.incomebalanceapp.utils.ModelEqualUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = {BankBalanceMapperImpl.class, BankMapperImpl.class})
public class BankBalanceMapperTest {

    @Autowired
    private BankBalanceMapper bankBalanceMapper;
    private static final Long ID = 1L;
    private final BigDecimal expenses = BigDecimal.valueOf(100);
    private final BigDecimal salary = BigDecimal.valueOf(150);
    private final BigDecimal incomes = BigDecimal.valueOf(170);
    private final BigDecimal result = BigDecimal.valueOf(190);


    @Test
    void bankBalanceDtoToBankBalance() {
        BankBalanceDTO bankBalanceDTO = createBankBalanceDto();
        BankBalance bankBalance = bankBalanceMapper.bankBalanceDtoToBankBalance(bankBalanceDTO);
        assertNotNull(bankBalance);
        ModelEqualUtils.assertBankBalanceAndDtoAreEqual(bankBalance, bankBalanceDTO);
    }

    @Test
    void bankBalanceToBankBalanceDto() {
        BankBalance bankBalance = createBankBalance();
        BankBalanceDTO bankBalanceDTO = bankBalanceMapper.bankBalanceToBankBalanceDTO(bankBalance);
        assertNotNull(bankBalanceDTO);
        ModelEqualUtils.assertBankBalanceAndDtoAreEqual(bankBalance, bankBalanceDTO);
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