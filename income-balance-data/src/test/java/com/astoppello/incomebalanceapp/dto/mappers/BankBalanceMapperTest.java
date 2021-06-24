package com.astoppello.incomebalanceapp.dto.mappers;

import com.astoppello.incomebalanceapp.dto.domain.BankBalanceDTO;
import com.astoppello.incomebalanceapp.dto.domain.BankDTO;
import com.astoppello.incomebalanceapp.model.Bank;
import com.astoppello.incomebalanceapp.model.BankBalance;
import com.astoppello.incomebalanceapp.model.MonthBalance;
import com.astoppello.incomebalanceapp.model.YearBalance;
import com.astoppello.incomebalanceapp.utils.ModelEqualUtils;
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
    private final String expenses = "100.00";
    private final String incomes = "170.00";
    private final String result = "190.00";


    @Test
    void bankBalanceDtoToBankBalance() {
        BankBalanceDTO bankBalanceDTO = createBankBalanceDto();
        BankBalance bankBalance = bankBalanceMapper.toEntity(bankBalanceDTO);
        assertNotNull(bankBalance);
        ModelEqualUtils.assertBankBalanceAndDtoAreEqual(bankBalance, bankBalanceDTO);
    }

    @Test
    void bankBalanceToBankBalanceDto() {
        BankBalance bankBalance = createBankBalance();
        BankBalanceDTO bankBalanceDTO = bankBalanceMapper.toDto(bankBalance);
        assertNotNull(bankBalanceDTO);
        ModelEqualUtils.assertBankBalanceAndDtoAreEqual(bankBalance, bankBalanceDTO);
        assertEquals(1L, bankBalanceDTO.getMonthBalanceId());
    }

    private BankBalance createBankBalance() {
        BankBalance bankBalance = BankBalance.builder()
                .id(ID)
                .bank(Bank.builder()
                              .id(ID)
                              .name(BankMapperTest.NAME)
                              .build())
                .expenses(new BigDecimal(expenses))
                .incomes(new BigDecimal(incomes))
                .result(new BigDecimal(result))
                .build();
        bankBalance.setMonthBalance(MonthBalance.builder().id(ID).build());
        bankBalance.setYearBalance(YearBalance.builder().id(ID).build());
        return bankBalance;
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
        bankBalanceDTO.setMonthBalanceId(ID);
        bankBalanceDTO.setYearBalanceId(ID);
        return bankBalanceDTO;
    }
}