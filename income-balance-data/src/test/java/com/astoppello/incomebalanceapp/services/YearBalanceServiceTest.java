package com.astoppello.incomebalanceapp.services;

import com.astoppello.incomebalanceapp.model.YearBalance;
import com.astoppello.incomebalanceapp.repositories.YearBalanceRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class YearBalanceServiceTest {

    private static final Long ID = 1L;
    private static final int YEAR = 2020;
    YearBalance yearBalance;

    @Mock
    YearBalanceRepository yearBalanceRepository;

    YearBalanceService yearBalanceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        yearBalanceService = new YearBalanceServiceImpl(yearBalanceRepository);
        yearBalance = YearBalance.builder().year(YEAR).id(ID).build();
    }

    @Test
    void findAllYearBalance() {
        List<YearBalance> list = List.of(yearBalance, YearBalance.builder().build());
        when(yearBalanceRepository.findAll()).thenReturn(list);

        assertEquals(list.size(), yearBalanceService.findAllYearBalance().size());
        verify(yearBalanceRepository).findAll();
    }

    @Test
    void findYearBalanceById() {
        when(yearBalanceRepository.findById(anyLong())).thenReturn(Optional.of(yearBalance));
        YearBalance yb = yearBalanceService.findYearBalanceById(ID);
        assertNotNull(yb);
        assertEquals(ID, yb.getId());
        assertEquals(YEAR, yb.getYear());
        verify(yearBalanceRepository, times(1)).findById(anyLong());
    }

    @Test
    void findYearBalanceByYear() {
        when(yearBalanceRepository.findByYear(anyInt())).thenReturn(yearBalance);
        YearBalance yb = yearBalanceService.findYearBalanceByYear(YEAR);
        assertNotNull(yb);
        assertEquals(ID, yb.getId());
        assertEquals(YEAR, yb.getYear());
        verify(yearBalanceRepository, times(1)).findByYear(anyInt());
    }
}