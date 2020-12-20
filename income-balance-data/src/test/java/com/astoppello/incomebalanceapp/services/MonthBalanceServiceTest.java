package com.astoppello.incomebalanceapp.services;

import com.astoppello.incomebalanceapp.dto.domain.MonthBalanceDTO;
import com.astoppello.incomebalanceapp.dto.mappers.MonthBalanceMapper;
import com.astoppello.incomebalanceapp.model.MonthBalance;
import com.astoppello.incomebalanceapp.repositories.MonthBalanceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MonthBalanceServiceTest {

    @Mock
    MonthBalanceRepository monthBalanceRepository;
    MonthBalance monthBalance;
    private static final Long ID = 1L;
    private static final String MONTH = "September";
    MonthBalanceService monthBalanceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        monthBalanceService = new MonthBalanceServiceImpl(monthBalanceRepository, MonthBalanceMapper.INSTANCE);
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
    void testFindAll() {
        List<MonthBalance> monthBalances = List.of(monthBalance, MonthBalance.builder().build());
        when(monthBalanceRepository.findAll()).thenReturn(monthBalances);
        assertEquals(monthBalances.size(), monthBalanceService.findAll().size());
        verify(monthBalanceRepository).findAll();
    }
}