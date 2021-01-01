package com.astoppello.incomebalanceapp.services;

import com.astoppello.incomebalanceapp.dto.domain.MonthBalanceDTO;
import com.astoppello.incomebalanceapp.dto.mappers.BankBalanceMapperImpl;
import com.astoppello.incomebalanceapp.dto.mappers.BankMapperImpl;
import com.astoppello.incomebalanceapp.dto.mappers.MonthBalanceMapper;
import com.astoppello.incomebalanceapp.dto.mappers.MonthBalanceMapperImpl;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = {BankBalanceMapperImpl.class, BankMapperImpl.class, MonthBalanceMapperImpl.class})
class MonthBalanceServiceTest {

    @Mock
    MonthBalanceRepository monthBalanceRepository;
    MonthBalance monthBalance;
    private static final Long ID = 1L;
    private static final String MONTH = "September";
    MonthBalanceService monthBalanceService;
    @Autowired
    MonthBalanceMapper monthBalanceMapper;
    @Mock
    private YearBalanceRepository yearBalanceRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        monthBalanceService = new MonthBalanceServiceImpl(monthBalanceRepository, monthBalanceMapper, yearBalanceRepository);
        monthBalance = MonthBalance.builder().id(ID).month(MONTH).build();
    }


    @Test
    void findByMonth() {
        when(monthBalanceRepository.findByMonth(anyString())).thenReturn(monthBalance);
        MonthBalanceDTO monthBalanceDTO = monthBalanceService.findByMonth(MONTH);
        assertNotNull(monthBalanceDTO);
        assertEquals(ID, monthBalanceDTO.getId());
        assertEquals(MONTH, monthBalanceDTO.getMonth());
        verify(monthBalanceRepository, times(1)).findByMonth(anyString());
    }

    @Test
    void findById() {
        when(monthBalanceRepository.findById(anyLong())).thenReturn(Optional.of(monthBalance));
        MonthBalanceDTO monthBalanceDTO = monthBalanceService.findById(ID);
        assertNotNull(monthBalanceDTO);
        assertEquals(ID, monthBalanceDTO.getId());
        assertEquals(MONTH, monthBalanceDTO.getMonth());
        verify(monthBalanceRepository, times(1)).findById(anyLong());
    }

    @Test
    void findAll() {
        List<MonthBalance> monthBalances = List.of(monthBalance, MonthBalance.builder().build());
        when(monthBalanceRepository.findAll()).thenReturn(monthBalances);
        assertEquals(monthBalances.size(), monthBalanceService.findAll().size());
        verify(monthBalanceRepository).findAll();
    }

    @Test
    void findMonthBalanceByIdAndYearBalanceId() {
        YearBalance yearBalance = YearBalance.builder().id(1L).build();
        yearBalance.addMonthBalance(monthBalance);

        when(yearBalanceRepository.findById(anyLong())).thenReturn(Optional.of(yearBalance));
        MonthBalanceDTO monthBalanceDTO = monthBalanceService.findMonthBalanceByYearBalanceIdAndId(1L, 1L);
        ModelEqualUtils.assertMonthBalanceAndDtoAreEqual(monthBalance, monthBalanceDTO);
        verify(yearBalanceRepository, times(1)).findById(anyLong());
    }
}