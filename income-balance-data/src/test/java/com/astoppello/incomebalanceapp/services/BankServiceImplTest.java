package com.astoppello.incomebalanceapp.services;

import com.astoppello.incomebalanceapp.dto.domain.BankDTO;
import com.astoppello.incomebalanceapp.dto.mappers.BankMapper;
import com.astoppello.incomebalanceapp.dto.mappers.BankMapperTest;
import com.astoppello.incomebalanceapp.model.Bank;
import com.astoppello.incomebalanceapp.repositories.BankRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BankServiceImplTest {

    @Mock
    BankRepository bankRepository;
    Bank bank;
    BankService bankService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        bankService = new BankServiceImpl(bankRepository, BankMapper.INSTANCE);
        bank = Bank.builder().id(BankMapperTest.ID).name(BankMapperTest.NAME).build();
    }

    @Test
    void findAllBanks() {
        List<Bank> bankList = List.of(bank, Bank.builder().build());
        when(bankRepository.findAll()).thenReturn(bankList);
        assertEquals(bankList.size(), bankService.findAllBanks().size());
        verify(bankRepository).findAll();
    }

    @Test
    void findBankById() {
        when(bankRepository.findById(anyLong())).thenReturn(Optional.of(bank));
        BankDTO bankDTO = bankService.findBankById(anyLong());
        assertNotNull(bankDTO);
        assertEquals(BankMapperTest.ID, bankDTO.getId());
    }

    @Test
    void findBankByName() {
        when(bankRepository.findBankByName(anyString())).thenReturn(bank);
        BankDTO bankDTO = bankService.findBankByName(anyString());
        assertNotNull(bankDTO);
        assertEquals(BankMapperTest.NAME, bankDTO.getName());
    }
}