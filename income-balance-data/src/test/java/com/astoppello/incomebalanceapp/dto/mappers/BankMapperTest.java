package com.astoppello.incomebalanceapp.dto.mappers;

import com.astoppello.incomebalanceapp.dto.domain.BankDTO;
import com.astoppello.incomebalanceapp.model.Bank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BankMapperTest {

    private BankMapper mapper;
    public static final Long ID = 1L;
    public static final String NAME = "Revolut";

    @BeforeEach
    void setUp() {
        mapper = BankMapper.INSTANCE;
    }

    @Test
    void bankDtoToBank() {
        BankDTO bankDTO = new BankDTO();
        bankDTO.setId(ID);
        bankDTO.setName(NAME);

        Bank bank = mapper.bankDtoToBank(bankDTO);
        assertNotNull(bank);
        assertEquals(ID, bank.getId());
        assertEquals(NAME, bank.getName());

    }

    @Test
    void bankToBankDto() {
        Bank bank = Bank.builder().id(ID).name(NAME).build();
        BankDTO bankDTO = mapper.bankToBankDto(bank);
        assertNotNull(bankDTO);
        assertEquals(ID, bankDTO.getId());
        assertEquals(NAME, bankDTO.getName());
    }
}