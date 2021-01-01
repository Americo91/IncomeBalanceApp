package com.astoppello.incomebalanceapp.services;

import com.astoppello.incomebalanceapp.dto.domain.BankBalanceDTO;
import com.astoppello.incomebalanceapp.dto.mappers.BankBalanceMapper;
import com.astoppello.incomebalanceapp.dto.mappers.BankBalanceMapperImpl;
import com.astoppello.incomebalanceapp.dto.mappers.BankMapperImpl;
import com.astoppello.incomebalanceapp.model.Bank;
import com.astoppello.incomebalanceapp.model.BankBalance;
import com.astoppello.incomebalanceapp.model.MonthBalance;
import com.astoppello.incomebalanceapp.model.YearBalance;
import com.astoppello.incomebalanceapp.repositories.YearBalanceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.Month;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = {BankBalanceMapperImpl.class, BankMapperImpl.class})
class BankBalanceServiceTest {

  public static final BigDecimal EXPENSES = BigDecimal.valueOf(100);
  public static final BigDecimal SALARY = BigDecimal.valueOf(200);
  public static final String REVOLUT = "Revolut";
  private static final Long ID = 1L;
  BankBalance bankBalance;
  @Autowired BankBalanceMapper bankBalanceMapper;
  BankBalanceService service;
  @Mock YearBalanceRepository yearBalanceRepository;
  private YearBalance yearBalance;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    service = new BankBalanceServiceImpl(bankBalanceMapper, yearBalanceRepository);
    bankBalance =
        BankBalance.builder()
            .bank(Bank.builder().id(ID).name(REVOLUT).build())
            .expenses(EXPENSES)
            .salary(SALARY)
            .id(ID)
            .build();
    yearBalance =
        YearBalance.builder()
            .id(ID)
            .build()
            .addMonthBalance(MonthBalance.builder().id(ID).build().addBankBalance(bankBalance));
  }

  @Test
  void findAll() {
    when(yearBalanceRepository.findById(anyLong())).thenReturn(Optional.ofNullable(yearBalance));
    assertEquals(1, service.findAll(ID, ID).size());
    verify(yearBalanceRepository, times(1)).findById(anyLong());
  }

  @Test
  void findById() {
    when(yearBalanceRepository.findById(anyLong())).thenReturn(Optional.of(yearBalance));
    BankBalanceDTO bankBalanceDTO = service.findById(ID, ID, ID);
    assertNotNull(bankBalanceDTO);
    assertEquals(ID, bankBalanceDTO.getId());
    assertEquals(EXPENSES, bankBalanceDTO.getExpenses());
    assertEquals(SALARY, bankBalanceDTO.getSalary());
    assertEquals(ID, bankBalanceDTO.getBank().getId());
    assertEquals(REVOLUT, bankBalanceDTO.getBank().getName());
  }

  @Test
  void findByBankName() {
    when(yearBalanceRepository.findById(anyLong())).thenReturn(Optional.of(yearBalance));
    BankBalanceDTO bankBalanceDTO = service.findByBankName(ID, ID, REVOLUT);
    assertNotNull(bankBalanceDTO);
    assertEquals(ID, bankBalanceDTO.getId());
    assertEquals(EXPENSES, bankBalanceDTO.getExpenses());
    assertEquals(SALARY, bankBalanceDTO.getSalary());
    assertEquals(ID, bankBalanceDTO.getBank().getId());
    assertEquals(REVOLUT, bankBalanceDTO.getBank().getName());
  }
}
