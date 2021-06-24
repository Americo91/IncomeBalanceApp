package com.astoppello.incomebalanceapp.services;

import com.astoppello.incomebalanceapp.dto.domain.YearBalanceDTO;
import com.astoppello.incomebalanceapp.dto.mappers.*;
import com.astoppello.incomebalanceapp.exceptions.ResourceNotFoundException;
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

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(
    classes = {
      YearBalanceMapperImpl.class,
      BankBalanceMapperImpl.class,
      BankMapperImpl.class,
      MonthBalanceMapperImpl.class
    })
class YearBalanceServiceTest {

  private static final Long ID = 1L;
  private static final int YEAR = 2020;
  YearBalance yearBalance;

  @Autowired YearBalanceMapper yearBalanceMapper;
  @Mock YearBalanceRepository yearBalanceRepository;

  YearBalanceService yearBalanceService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    yearBalanceService = new YearBalanceServiceImpl(yearBalanceRepository, yearBalanceMapper);
    yearBalance = YearBalance.builder().year(YEAR).id(ID).build();
  }

  @Test
  void findAllYearBalance() {
    List<YearBalance> list = List.of(yearBalance, YearBalance.builder().year(2021).build());
    when(yearBalanceRepository.findAll()).thenReturn(list);
    assertEquals(list.size(), yearBalanceService.findAll().size());
    verify(yearBalanceRepository, times(1)).findAll();
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

  @Test
  void createNewYearBalance() {
    YearBalanceDTO yearBalanceDTO = new YearBalanceDTO();
    yearBalanceDTO.setId(ID);
    yearBalanceDTO.setYear(YEAR);
    when(yearBalanceRepository.save(any(YearBalance.class))).thenReturn(yearBalance);

    YearBalanceDTO savedDto = yearBalanceService.createNewYearBalance(yearBalanceDTO);
    assertNotNull(savedDto);
    assertEquals(ID, savedDto.getId());
    assertEquals(YEAR, savedDto.getYear());
    verify(yearBalanceRepository, times(1)).save(any(YearBalance.class));
  }

  @Test
  void saveYearBalance() {
    YearBalance yearBalance = YearBalance.builder().id(ID).year(YEAR).build();

    YearBalanceDTO yearBalanceDTO = new YearBalanceDTO();
    yearBalanceDTO.setYear(yearBalance.getYear());
    when(yearBalanceRepository.save(any(YearBalance.class))).thenReturn(yearBalance);
    YearBalanceDTO savedYearBalanceDto = yearBalanceService.saveYearBalance(ID, yearBalanceDTO);
    assertNotNull(savedYearBalanceDto);
    assertEquals(ID, savedYearBalanceDto.getId());
    assertEquals(YEAR, savedYearBalanceDto.getYear());
    verify(yearBalanceRepository, times(1)).save(any(YearBalance.class));
  }

  @Test
  void updateYearBalance() {
    YearBalanceDTO yearBalanceDTO = new YearBalanceDTO();
    yearBalanceDTO.setId(ID);
    yearBalanceDTO.setYear(2019);

    when(yearBalanceRepository.findById(anyLong())).thenReturn(Optional.ofNullable(yearBalance));
    when(yearBalanceRepository.save(any(YearBalance.class))).thenReturn(yearBalance);

    YearBalanceDTO savedYearBalanceDto = yearBalanceService.updateYearBalance(ID, yearBalanceDTO);
    assertNotNull(savedYearBalanceDto);
    assertEquals(ID, savedYearBalanceDto.getId());
    assertNotEquals(YEAR, savedYearBalanceDto.getYear());
    verify(yearBalanceRepository, times(1)).findById(anyLong());
    verify(yearBalanceRepository, times(1)).save(any(YearBalance.class));
  }

  @Test
  void deleteYearBalance() {
    when(yearBalanceRepository.findById(anyLong())).thenReturn(Optional.ofNullable(yearBalance));
    YearBalanceDTO yearBalanceDTO = yearBalanceService.deleteYearBalance(ID);
    verify(yearBalanceRepository, times(1)).deleteById(ID);
    verify(yearBalanceRepository, times(1)).findById(ID);
    assertThat(yearBalanceDTO.getId()).isEqualTo(ID);
  }
}
