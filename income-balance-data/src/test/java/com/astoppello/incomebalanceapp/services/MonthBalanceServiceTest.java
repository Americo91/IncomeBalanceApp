package com.astoppello.incomebalanceapp.services;

import com.astoppello.incomebalanceapp.dto.domain.MonthBalanceDTO;
import com.astoppello.incomebalanceapp.dto.mappers.*;
import com.astoppello.incomebalanceapp.exceptions.ResourceNotFoundException;
import com.astoppello.incomebalanceapp.model.Bank;
import com.astoppello.incomebalanceapp.model.BankBalance;
import com.astoppello.incomebalanceapp.model.MonthBalance;
import com.astoppello.incomebalanceapp.model.YearBalance;
import com.astoppello.incomebalanceapp.repositories.MonthBalanceRepository;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(
        classes = {BankBalanceMapperImpl.class, BankMapperImpl.class, MonthBalanceMapperImpl.class})
class MonthBalanceServiceTest {

    final String expenses = "100.00", salary = "200.00", incomes = "200.00";
    final BigDecimal EXPENSES = new BigDecimal(expenses);
    final BigDecimal SALARY = new BigDecimal(salary);
    final BigDecimal INCOMES = new BigDecimal(incomes);
    final String REVOLUT = "Revolut";
    private static final Long ID = 1L;
    private static final Month MONTH = Month.SEPTEMBER;
    @Mock
    MonthBalanceRepository monthBalanceRepository;
    MonthBalance monthBalance;
    MonthBalanceService monthBalanceService;
    @Autowired
    MonthBalanceMapper monthBalanceMapper;
    @Autowired
    BankBalanceMapper bankBalanceMapper;
    @Mock
    private YearBalanceRepository yearBalanceRepository;
    private YearBalance yearBalance;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        monthBalanceService =
                new MonthBalanceServiceImpl(monthBalanceRepository, monthBalanceMapper, yearBalanceRepository);
        monthBalance = MonthBalance.builder()
                                   .id(ID)
                                   .month(MONTH)
                                   .build();
        yearBalance = YearBalance.builder()
                                 .id(ID)
                                 .build()
                                 .addMonthBalance(monthBalance);
    }

    @Test
    void findByMonth() {
        when(monthBalanceRepository.findAll()).thenReturn(List.of(monthBalance));
        List<MonthBalanceDTO> balanceDTOList = monthBalanceService.findByMonth(MONTH.name());
        assertNotNull(balanceDTOList);
        assertNotNull(balanceDTOList.get(0));
        assertEquals(ID, balanceDTOList.get(0)
                                       .getId());
        assertEquals(MONTH, balanceDTOList.get(0)
                                          .getMonth());
        verify(monthBalanceRepository, times(1)).findAll();
    }

    @Test
    void findAll() {
        when(yearBalanceRepository.findById(anyLong())).thenReturn(Optional.ofNullable(yearBalance));
        assertEquals(
                yearBalance.getMonthBalanceSet()
                           .size(), monthBalanceService.findAllById(ID)
                                                       .size());
        verify(yearBalanceRepository, times(1)).findById(anyLong());
    }

    @Test
    void createNewMonthBalance() {
        // when
        MonthBalanceDTO monthBalanceDTO = new MonthBalanceDTO();
        monthBalanceDTO.setId(ID);
        monthBalanceDTO.setSalary(SALARY);
        monthBalanceDTO.setMonth(MONTH);
        monthBalanceDTO.setExpenses(EXPENSES);
        monthBalanceDTO.setYearBalanceId(ID);
        MonthBalance monthBalance = monthBalanceMapper.toEntity(monthBalanceDTO);
        monthBalance.setYearBalance(yearBalance);

        yearBalance.addMonthBalance(
                MonthBalance.builder()
                            .month(MONTH)
                            .expenses(EXPENSES)
                            .salary(SALARY)
                            .build());

        // given
        when(monthBalanceRepository.save(any(MonthBalance.class))).thenReturn(monthBalance);
        when(yearBalanceRepository.findById(anyLong())).thenReturn(Optional.ofNullable(yearBalance));
        when(yearBalanceRepository.save(any(YearBalance.class))).thenReturn(yearBalance);

        // then
        MonthBalanceDTO savedMonthBalance =
                monthBalanceService.createNewMonthBalanceById(ID, monthBalanceDTO);
        assertNotNull(savedMonthBalance);
        assertNotNull(savedMonthBalance.getId());
        assertNotNull(savedMonthBalance.getYearBalanceId());
        assertEquals(savedMonthBalance.getExpenses(), EXPENSES);
        assertEquals(savedMonthBalance.getMonth(), MONTH);
        assertEquals(savedMonthBalance.getYearBalanceId(), ID);
        assertEquals(savedMonthBalance.getSalary(), SALARY);
        verify(monthBalanceRepository, times(1)).save(any(MonthBalance.class));
        verify(yearBalanceRepository, times(1)).findById(anyLong());
        verify(yearBalanceRepository, times(1)).save(any(YearBalance.class));
    }

    @Test
    void testFindAll() {
        when(monthBalanceRepository.findAll()).thenReturn(List.of(monthBalance));
        List<MonthBalanceDTO> monthBalanceList = monthBalanceService.findAll();
        assertNotNull(monthBalanceList);
        assertNotNull(monthBalanceList.get(0));
        assertEquals(ID, monthBalanceList.get(0)
                                         .getId());
        assertEquals(MONTH, monthBalanceList.get(0)
                                            .getMonth());
        verify(monthBalanceRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        when(monthBalanceRepository.findById(anyLong())).thenReturn(Optional.ofNullable(monthBalance));
        MonthBalanceDTO savedMonthBalanceDTO = monthBalanceService.findById(ID);
        assertNotNull(savedMonthBalanceDTO);
        assertEquals(ID, savedMonthBalanceDTO.getId());
        assertEquals(MONTH, savedMonthBalanceDTO.getMonth());
        verify(monthBalanceRepository, times(1)).findById(anyLong());
    }

    @Test
    void saveMonthBalance() {
        MonthBalanceDTO monthBalanceDTO =
                monthBalanceMapper.toDto(monthBalance);

        when(monthBalanceRepository.save(any(MonthBalance.class))).thenReturn(monthBalance);
        MonthBalanceDTO savedMonthBalanceDto =
                monthBalanceService.saveMonthBalance(2L, monthBalanceDTO);
        assertNotNull(savedMonthBalanceDto);

        assertEquals(ID, savedMonthBalanceDto.getId());
        assertEquals(MONTH, savedMonthBalanceDto.getMonth());
        verify(monthBalanceRepository, times(1)).save(any(MonthBalance.class));
        verify(yearBalanceRepository, times(1)).findById(anyLong());
        verify(yearBalanceRepository, times(1)).save(any(YearBalance.class));
    }

    @Test
    void updateMonthBalance() {
        when(monthBalanceRepository.findById(anyLong())).thenReturn(Optional.ofNullable(monthBalance));
        when(monthBalanceRepository.save(any(MonthBalance.class))).thenReturn(monthBalance);
        when(yearBalanceRepository.findById(anyLong())).thenReturn(Optional.ofNullable(yearBalance));

        MonthBalanceDTO monthBalanceDTO =
                monthBalanceMapper.toDto(monthBalance);
        monthBalanceDTO.setMonth(Month.OCTOBER);

        MonthBalanceDTO savedMonthBalanceDto =
                monthBalanceService.updateMonthBalance(ID, monthBalanceDTO);
        assertNotNull(savedMonthBalanceDto);
        assertNotEquals(MONTH, savedMonthBalanceDto.getMonth());
        assertNotNull(savedMonthBalanceDto.getYearBalanceId());
        verify(monthBalanceRepository, times(1)).findById(anyLong());
        verify(monthBalanceRepository, times(1)).save(any(MonthBalance.class));
        verify(yearBalanceRepository, times(1)).findById(anyLong());
        verify(yearBalanceRepository, times(1)).save(any(YearBalance.class));
    }

    @Test
    void delete() {
        when(monthBalanceRepository.findById(anyLong())).thenReturn(Optional.of(monthBalance));
        MonthBalanceDTO monthBalanceDTO = monthBalanceService.delete(monthBalance.getId());
        assertThat(monthBalanceDTO.getId()).isEqualTo(ID);
        verify(monthBalanceRepository, times(1)).findById(anyLong());
        verify(monthBalanceRepository, times(1)).deleteById(anyLong());
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
                                                                 .bank(Bank.builder()
                                                                           .id(ID)
                                                                           .name(REVOLUT)
                                                                           .build())
                                                                 .expenses(EXPENSES)
                                                                 .incomes(INCOMES)
                                                                 .build()));
    }
}
