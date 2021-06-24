package com.astoppello.incomebalanceapp.dto.mappers;

import com.astoppello.incomebalanceapp.dto.domain.BankDTO;
import com.astoppello.incomebalanceapp.model.Bank;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.astoppello.incomebalanceapp.utils.ModelEqualUtils.assertBankAndBankDtoAreEquals;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(classes = {BankMapperImpl.class})
public class BankMapperTest {

    @Autowired
    private BankMapper mapper;
    public static final Long ID = 1L;
    public static final String NAME = "Revolut";

    @Test
    void bankDtoToBank() {
        BankDTO bankDTO = new BankDTO();
        bankDTO.setName(NAME);
        bankDTO.setId(ID);

        Bank bank = mapper.toEntity(bankDTO);
        assertNotNull(bank);
        assertBankAndBankDtoAreEquals(bank, bankDTO);
    }

    @Test
    void bankToBankDto() {
        Bank bank = Bank.builder().id(ID).name(NAME).build();
        BankDTO bankDTO = mapper.toDto(bank);
        assertNotNull(bankDTO);
        assertBankAndBankDtoAreEquals(bank, bankDTO);
    }
}