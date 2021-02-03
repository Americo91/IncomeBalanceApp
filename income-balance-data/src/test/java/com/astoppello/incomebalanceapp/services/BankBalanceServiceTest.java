package com.astoppello.incomebalanceapp.services;

import com.astoppello.incomebalanceapp.dto.domain.BankBalanceDTO;
import com.astoppello.incomebalanceapp.dto.domain.BankDTO;
import com.astoppello.incomebalanceapp.dto.mappers.BankBalanceMapper;
import com.astoppello.incomebalanceapp.dto.mappers.BankBalanceMapperImpl;
import com.astoppello.incomebalanceapp.dto.mappers.BankMapperImpl;
import com.astoppello.incomebalanceapp.exceptions.ResourceNotFoundException;
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

import static org.junit.jupiter.api.Assertions.*;
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
    assertEquals(1, service.findAllByIds(ID, ID).size());
    verify(yearBalanceRepository, times(1)).findById(anyLong());
  }

  @Test
  void findAllByYearBalanceId() {
    when(yearBalanceRepository.findById(anyLong())).thenReturn(Optional.ofNullable(yearBalance));
    assertEquals(1, service.findAllByYearBalanceId(ID).size());
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
  void createNewBankBalanceById() {
    BankBalanceDTO bankBalanceDTO = createBankBalanceDto();
    bankBalanceDTO.setMonthBalanceId(monthBalance.getId());
    bankBalanceDTO.setYearBalanceId(yearBalance.getId());
    when(bankBalanceRepository.save(any(BankBalance.class))).thenReturn(bankBalance);
    when(monthBalanceRepository.findById(anyLong())).thenReturn(Optional.ofNullable(monthBalance));
    when(yearBalanceRepository.findById(anyLong())).thenReturn(Optional.ofNullable(yearBalance));
    BankBalanceDTO savedBankBalanced = service.createNewBankBalanceById(ID, ID, bankBalanceDTO);
    assertNotNull(savedBankBalanced);
    assertEquals(ID, savedBankBalanced.getId());
    assertEquals(EXPENSES, savedBankBalanced.getExpenses());
    assertEquals(SALARY, savedBankBalanced.getSalary());
    assertEquals(ID, savedBankBalanced.getBank().getId());
    assertEquals(REVOLUT, savedBankBalanced.getBank().getName());
    verify(bankBalanceRepository, times(1)).save(any(BankBalance.class));
    assertNotNull(savedBankBalanced.getMonthBalanceId());
    verify(monthBalanceRepository, times(1)).findById(anyLong());
    verify(monthBalanceRepository, times(1)).save(any(MonthBalance.class));
    assertNotNull(savedBankBalanced.getYearBalanceId());
    verify(yearBalanceRepository, times(1)).findById(anyLong());
    verify(yearBalanceRepository, times(1)).save(any(YearBalance.class));
  }

  @Test
  void createNewBankBalance() {
    BankBalanceDTO bankBalanceDTO = createBankBalanceDto();
    bankBalanceDTO.setMonthBalanceId(monthBalance.getId());

    when(bankBalanceRepository.save(any(BankBalance.class))).thenReturn(bankBalance);
    when(monthBalanceRepository.findById(anyLong())).thenReturn(Optional.ofNullable(monthBalance));

    BankBalanceDTO savedBankBalanceDto = service.createNewBankBalance(bankBalanceDTO);
    assertNotNull(savedBankBalanceDto);
    assertEquals(ID, savedBankBalanceDto.getId());
    assertEquals(EXPENSES, savedBankBalanceDto.getExpenses());
    assertEquals(SALARY, savedBankBalanceDto.getSalary());
    assertEquals(ID, savedBankBalanceDto.getBank().getId());
    assertEquals(REVOLUT, savedBankBalanceDto.getBank().getName());
    verify(bankBalanceRepository, times(1)).save(any(BankBalance.class));
    assertNotNull(savedBankBalanceDto.getMonthBalanceId());
    verify(monthBalanceRepository, times(1)).findById(anyLong());
    verify(monthBalanceRepository, times(1)).save(any(MonthBalance.class));
  }

  @Test
  void saveBankBalance() {
    BankBalanceDTO bankBalanceDTO = createBankBalanceDto();
    bankBalanceDTO.setMonthBalanceId(monthBalance.getId());
    bankBalance.setMonthBalance(monthBalance);
    when(bankBalanceRepository.save(any(BankBalance.class))).thenReturn(bankBalance);
    when(monthBalanceRepository.findById(anyLong())).thenReturn(Optional.ofNullable(monthBalance));
    BankBalanceDTO savedBankBalanceDto = service.saveBankBalance(ID, bankBalanceDTO);
    assertNotNull(savedBankBalanceDto);
    assertEquals(savedBankBalanceDto.getId(), ID);
    verify(bankBalanceRepository, times(1)).save(any(BankBalance.class));
    assertNotNull(savedBankBalanceDto.getMonthBalanceId());
    verify(monthBalanceRepository, times(1)).findById(anyLong());
    verify(monthBalanceRepository, times(1)).save(any(MonthBalance.class));
  }

  @Test
  void updateBankBalance() {
    BankBalanceDTO bankBalanceDTO = createBankBalanceDto();
    final BigDecimal result = new BigDecimal(100);
    bankBalanceDTO.setResult(result);
    when(bankBalanceRepository.findById(anyLong())).thenReturn(Optional.ofNullable(bankBalance));
    when(bankBalanceRepository.save(any(BankBalance.class))).thenReturn(bankBalance);

    BankBalanceDTO savedBankBalanceDto = service.updateBankBalance(ID, bankBalanceDTO);
    assertNotNull(savedBankBalanceDto);
    assertEquals(ID, savedBankBalanceDto.getId());
    assertEquals(EXPENSES, savedBankBalanceDto.getExpenses());
    assertEquals(SALARY, savedBankBalanceDto.getSalary());
    assertEquals(ID, savedBankBalanceDto.getBank().getId());
    assertEquals(REVOLUT, savedBankBalanceDto.getBank().getName());
    assertEquals(result, savedBankBalanceDto.getResult());
    verify(bankBalanceRepository, times(1)).findById(anyLong());
    verify(bankBalanceRepository, times(1)).save(any(BankBalance.class));
  }

  @Test
  void deleteBankBalance() {
    service.deleteBankBalance(ID);
    assertThrows(ResourceNotFoundException.class, () -> service.findById(ID));
    verify(bankBalanceRepository, times(1)).deleteById(anyLong());
  }

  private BankBalanceDTO createBankBalanceDto() {
    BankBalanceDTO bankBalanceDTO = new BankBalanceDTO();
    bankBalanceDTO.setExpenses(EXPENSES);
    bankBalanceDTO.setSalary(SALARY);
    bankBalanceDTO.setId(ID);
    BankDTO bankDTO = new BankDTO();
    bankDTO.setName(REVOLUT);
    bankDTO.setId(ID);
    bankBalanceDTO.setBank(bankDTO);
    return bankBalanceDTO;
  }

}
