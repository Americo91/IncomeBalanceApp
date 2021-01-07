package com.astoppello.incomebalanceapp.services;

import com.astoppello.incomebalanceapp.dto.domain.BankBalanceDTO;
import com.astoppello.incomebalanceapp.dto.mappers.BankBalanceMapper;
import com.astoppello.incomebalanceapp.dto.mappers.BankBalanceMapperImpl;
import com.astoppello.incomebalanceapp.dto.mappers.BankMapperImpl;
import com.astoppello.incomebalanceapp.model.Bank;
import com.astoppello.incomebalanceapp.model.BankBalance;
import com.astoppello.incomebalanceapp.model.MonthBalance;
import com.astoppello.incomebalanceapp.model.YearBalance;
import com.astoppello.incomebalanceapp.repositories.BankBalanceRepository;
import com.astoppello.incomebalanceapp.repositories.MonthBalanceRepository;
import com.astoppello.incomebalanceapp.repositories.YearBalanceRepository;
import com.astoppello.incomebalanceapp.utils.ModelEqualUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
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
  @Mock BankBalanceRepository bankBalanceRepository;
  @Mock MonthBalanceRepository monthBalanceRepository;
  private YearBalance yearBalance;
  private MonthBalance monthBalance;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    service =
        new BankBalanceServiceImpl(
            bankBalanceRepository,
            bankBalanceMapper,
            monthBalanceRepository,
            yearBalanceRepository);
    monthBalance = MonthBalance.builder().id(ID).build();
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
            .addMonthBalance(monthBalance.addBankBalance(bankBalance));
  }

  @Test
  void findAllById() {
    when(yearBalanceRepository.findById(anyLong())).thenReturn(Optional.ofNullable(yearBalance));
    assertEquals(1, service.findAllById(ID, ID).size());
    verify(yearBalanceRepository, times(1)).findById(anyLong());
  }

  @Test
  void findAll() {
    when(bankBalanceRepository.findAll()).thenReturn(List.of(bankBalance));
    assertEquals(1, service.findAll().size());
    verify(bankBalanceRepository, times(1)).findAll();
  }

  @Test
  void findById() {
    when(bankBalanceRepository.findById(anyLong())).thenReturn(Optional.of(bankBalance));
    BankBalanceDTO bankBalanceDTO = service.findById(ID);
    assertNotNull(bankBalanceDTO);
    assertEquals(ID, bankBalanceDTO.getId());
    assertEquals(EXPENSES, bankBalanceDTO.getExpenses());
    assertEquals(SALARY, bankBalanceDTO.getSalary());
    assertEquals(ID, bankBalanceDTO.getBank().getId());
    assertEquals(REVOLUT, bankBalanceDTO.getBank().getName());
    verify(bankBalanceRepository, times(1)).findById(anyLong());
  }

  @Test
  void findByBankName() {
    when(bankBalanceRepository.findAll()).thenReturn(List.of(bankBalance));
    BankBalanceDTO bankBalanceDTO = service.findByBankName(REVOLUT).get(0);
    assertNotNull(bankBalanceDTO);
    assertEquals(ID, bankBalanceDTO.getId());
    assertEquals(EXPENSES, bankBalanceDTO.getExpenses());
    assertEquals(SALARY, bankBalanceDTO.getSalary());
    assertEquals(ID, bankBalanceDTO.getBank().getId());
    assertEquals(REVOLUT, bankBalanceDTO.getBank().getName());
    verify(bankBalanceRepository, times(1)).findAll();
  }

  @Test
  void createNewBankBalance() {
    BankBalanceDTO bankBalanceDTO = bankBalanceMapper.bankBalanceToBankBalanceDTO(bankBalance);
    bankBalanceDTO.setMonthBalanceId(monthBalance.getId());
    when(bankBalanceRepository.save(any(BankBalance.class))).thenReturn(bankBalance);
    when(monthBalanceRepository.findById(anyLong())).thenReturn(Optional.ofNullable(monthBalance));
    BankBalanceDTO savedBankBalanceDto = service.createNewBankBalance(bankBalanceDTO);
    assertNotNull(savedBankBalanceDto);
    assertEquals(ID, bankBalanceDTO.getId());
    ModelEqualUtils.assertBankBalanceAndDtoAreEqual(bankBalance, savedBankBalanceDto);
    verify(bankBalanceRepository, times(1)).save(any(BankBalance.class));
    assertNotNull(savedBankBalanceDto.getMonthBalanceId());
    verify(monthBalanceRepository).findById(anyLong());
    verify(monthBalanceRepository).save(any(MonthBalance.class));
  }
}
