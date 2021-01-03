package com.astoppello.incomebalanceapp.services;

import com.astoppello.incomebalanceapp.dto.domain.BankDTO;
import com.astoppello.incomebalanceapp.dto.mappers.BankMapper;
import com.astoppello.incomebalanceapp.dto.mappers.BankMapperImpl;
import com.astoppello.incomebalanceapp.dto.mappers.BankMapperTest;
import com.astoppello.incomebalanceapp.model.Bank;
import com.astoppello.incomebalanceapp.repositories.BankRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static com.astoppello.incomebalanceapp.dto.mappers.BankMapperTest.ID;
import static com.astoppello.incomebalanceapp.dto.mappers.BankMapperTest.NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = {BankMapperImpl.class})
class BankServiceTest {

  @Mock BankRepository bankRepository;
  Bank bank;
  BankService bankService;
  @Autowired BankMapper bankMapper;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    bankService = new BankServiceImpl(bankRepository, bankMapper);
    bank = Bank.builder().id(ID).name(BankMapperTest.NAME).build();
  }

  @Test
  void findAllBanks() {
    List<Bank> bankList = List.of(bank, Bank.builder().build());
    when(bankRepository.findAll()).thenReturn(bankList);
    assertEquals(bankList.size(), bankService.findAll().size());
    verify(bankRepository).findAll();
  }

  @Test
  void findBankById() {
    when(bankRepository.findById(anyLong())).thenReturn(Optional.of(bank));
    BankDTO bankDTO = bankService.findById(ID);
    assertNotNull(bankDTO);
    assertEquals(ID, bankDTO.getId());
    verify(bankRepository, times(1)).findById(anyLong());
  }

  /*
  @Test
  void findBankByName() {
    when(bankRepository.findBankByName(anyString())).thenReturn(bank);
    BankDTO bankDTO = bankService.findBankByName(NAME);
    assertNotNull(bankDTO);
    assertEquals(BankMapperTest.NAME, bankDTO.getName());
  }
   */

  @Test
  void createNewBank() {
    BankDTO bankDTO = new BankDTO();
    bankDTO.setName(NAME);
    when(bankRepository.save(any(Bank.class))).thenReturn(bank);
    BankDTO savedBankDto = bankService.createNewBank(bankDTO);
    assertNotNull(savedBankDto);
    assertNotNull(savedBankDto.getId());
    assertEquals(savedBankDto.getName(), NAME);
    verify(bankRepository, times(1)).save(any(Bank.class));
  }
}
