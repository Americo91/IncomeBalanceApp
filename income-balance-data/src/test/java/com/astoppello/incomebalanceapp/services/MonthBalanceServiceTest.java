package com.astoppello.incomebalanceapp.services;

import com.astoppello.incomebalanceapp.dto.domain.MonthBalanceDTO;
import com.astoppello.incomebalanceapp.dto.mappers.BankBalanceMapperImpl;
import com.astoppello.incomebalanceapp.dto.mappers.BankMapperImpl;
import com.astoppello.incomebalanceapp.dto.mappers.MonthBalanceMapper;
import com.astoppello.incomebalanceapp.dto.mappers.MonthBalanceMapperImpl;
import com.astoppello.incomebalanceapp.model.Bank;
import com.astoppello.incomebalanceapp.model.BankBalance;
import com.astoppello.incomebalanceapp.model.MonthBalance;
import com.astoppello.incomebalanceapp.model.YearBalance;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(
    classes = {BankBalanceMapperImpl.class, BankMapperImpl.class, MonthBalanceMapperImpl.class})
class MonthBalanceServiceTest {

  public static final BigDecimal EXPENSES = BigDecimal.valueOf(100);
  public static final BigDecimal SALARY = BigDecimal.valueOf(200);
  public static final String REVOLUT = "Revolut";
  private static final Long ID = 1L;
  private static final String MONTH = "September";
  @Mock MonthBalanceRepository monthBalanceRepository;
  MonthBalance monthBalance;
  MonthBalanceService monthBalanceService;
  @Autowired MonthBalanceMapper monthBalanceMapper;
  @Mock private YearBalanceRepository yearBalanceRepository;
  private YearBalance yearBalance;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    monthBalanceService =
        new MonthBalanceServiceImpl(
            monthBalanceRepository, monthBalanceMapper, yearBalanceRepository);
    monthBalance = MonthBalance.builder().id(ID).month(MONTH).build();
    yearBalance = YearBalance.builder().id(1L).build().addMonthBalance(monthBalance);
  }

  /*
  @Test
  void findByMonth() {
    when(yearBalanceRepository.findById(anyLong())).thenReturn(Optional.of(yearBalance));
    MonthBalanceDTO monthBalanceDTO = monthBalanceService.findByMonth(ID, MONTH);
    assertNotNull(monthBalanceDTO);
    assertEquals(ID, monthBalanceDTO.getId());
    assertEquals(MONTH, monthBalanceDTO.getMonth());
    verify(yearBalanceRepository, times(1)).findById(anyLong());
  }
   */

  @Test
  void findById() {
    when(yearBalanceRepository.findById(anyLong())).thenReturn(Optional.of(yearBalance));
    MonthBalanceDTO monthBalanceDTO = monthBalanceService.findMonthOfYearById(ID, ID);
    assertNotNull(monthBalanceDTO);
    ModelEqualUtils.assertMonthBalanceAndDtoAreEqual(monthBalance, monthBalanceDTO);
    verify(yearBalanceRepository, times(1)).findById(anyLong());
  }

  @Test
  void findAll() {
    when(yearBalanceRepository.findById(anyLong())).thenReturn(Optional.ofNullable(yearBalance));
    assertEquals(yearBalance.getMonthBalanceList().size(), monthBalanceService.findAllById(ID).size());
    verify(yearBalanceRepository, times(1)).findById(anyLong());
  }

  @Test
  void createNewMonthBalance() {
    //when
    MonthBalanceDTO monthBalanceDTO = new MonthBalanceDTO();
    monthBalanceDTO.setSalary(SALARY);
    monthBalanceDTO.setMonth(MONTH);
    monthBalanceDTO.setExpenses(EXPENSES);
    yearBalance.addMonthBalance(
        MonthBalance.builder().month(MONTH).expenses(EXPENSES).salary(SALARY).build());

    //given
    when(monthBalanceRepository.save(any((MonthBalance.class))))
        .thenReturn(monthBalanceMapper.monthBalanceDtoToMonthBalance(monthBalanceDTO));
    when(yearBalanceRepository.findById(anyLong())).thenReturn(Optional.ofNullable(yearBalance));
    when(yearBalanceRepository.save(any(YearBalance.class))).thenReturn(yearBalance);

    //then
    MonthBalanceDTO savedMonthBalance =
        monthBalanceService.createNewMonthBalanceById(yearBalance.getId(), monthBalanceDTO);
    assertNotNull(savedMonthBalance);
    //assertNotNull(savedMonthBalance.getId());
    assertNotNull(savedMonthBalance.getYearBalanceId());
    assertEquals(savedMonthBalance.getExpenses(), EXPENSES);
    assertEquals(savedMonthBalance.getMonth(), MONTH);
    assertEquals(savedMonthBalance.getYearBalanceId(), ID);
    assertEquals(savedMonthBalance.getSalary(), SALARY);
    verify(monthBalanceRepository, times(1)).save(any(MonthBalance.class));
    verify(yearBalanceRepository, times(1)).findById(anyLong());
    verify(yearBalanceRepository, times(1)).save(any(YearBalance.class));
  }

  private YearBalance buildYearBalanceForTest() {
    return YearBalance.builder()
        .id(ID)
        .year(2020)
        .expenses(EXPENSES)
        .salary(SALARY)
        .build()
        .addMonthBalance(
            MonthBalance.builder()
                .id(ID)
                .month(MONTH)
                .expenses(EXPENSES)
                .salary(SALARY)
                .build()
                .addBankBalance(
                    BankBalance.builder()
                        .id(ID)
                        .bank(Bank.builder().id(ID).name(REVOLUT).build())
                        .expenses(EXPENSES)
                        .salary(SALARY)
                        .build()));
  }
}
