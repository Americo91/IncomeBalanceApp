package com.astoppello.incomebalanceapp.services;

import com.astoppello.incomebalanceapp.dto.domain.YearBalanceDTO;
import com.astoppello.incomebalanceapp.dto.mappers.YearBalanceMapper;
import com.astoppello.incomebalanceapp.model.YearBalance;
import com.astoppello.incomebalanceapp.repositories.YearBalanceRepository;
import com.astoppello.incomebalanceapp.services.jms.JmsTextMessageService;
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
    JmsTextMessageService jmsTextMessageService;

    @Mock
    YearBalanceRepository yearBalanceRepository;

    YearBalanceService yearBalanceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        yearBalanceService = new YearBalanceServiceImpl(yearBalanceRepository, YearBalanceMapper.INSTANCE);
        yearBalance = YearBalance.builder().year(YEAR).id(ID).build();
    }

    @Test
    void findAllYearBalance() {
        List<YearBalance> list = List.of(yearBalance, YearBalance.builder().build());
        when(yearBalanceRepository.findAll()).thenReturn(list);

        assertEquals(list.size(), yearBalanceService.findAll().size());
        verify(yearBalanceRepository).findAll();
    }

    @Test
    void findYearBalanceById() {
        when(yearBalanceRepository.findById(anyLong())).thenReturn(Optional.of(yearBalance));
        YearBalanceDTO yearBalanceDTO = yearBalanceService.findById(ID);
        assertNotNull(yearBalanceDTO);
        assertEquals(ID, yearBalanceDTO.getId());
        assertEquals(YEAR, yearBalanceDTO.getYear());
        verify(yearBalanceRepository, times(1)).findById(anyLong());
    }

    @Test
    void findYearBalanceByYear() {
        when(yearBalanceRepository.findByYear(anyInt())).thenReturn(yearBalance);
        YearBalanceDTO yearBalanceDTO = yearBalanceService.findYearBalanceByYear(YEAR);
        assertNotNull(yearBalanceDTO);
        assertEquals(ID, yearBalanceDTO.getId());
        assertEquals(YEAR, yearBalanceDTO.getYear());
        verify(yearBalanceRepository, times(1)).findByYear(anyInt());
    }
}