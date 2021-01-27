package com.astoppello.incomebalanceapp.services;

import com.astoppello.incomebalanceapp.dto.domain.YearBalanceDTO;
import com.astoppello.incomebalanceapp.dto.mappers.MonthBalanceMapper;
import com.astoppello.incomebalanceapp.dto.mappers.YearBalanceMapper;
import com.astoppello.incomebalanceapp.exceptions.ResourceNotFoundException;
import com.astoppello.incomebalanceapp.model.YearBalance;
import com.astoppello.incomebalanceapp.repositories.YearBalanceRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/** Created by @author stopp on 15/11/2020 */
@Service
public class YearBalanceServiceImpl implements YearBalanceService {

  private final YearBalanceRepository yearBalanceRepository;
  private final YearBalanceMapper yearBalanceMapper;
  private final MonthBalanceMapper monthBalanceMapper;

  public YearBalanceServiceImpl(
      YearBalanceRepository yearBalanceRepository,
      YearBalanceMapper yearBalanceMapper,
      MonthBalanceMapper monthBalanceMapper) {
    this.yearBalanceRepository = yearBalanceRepository;
    this.yearBalanceMapper = yearBalanceMapper;
    this.monthBalanceMapper = monthBalanceMapper;
  }

  @Override
  public List<YearBalanceDTO> findAll() {
    return yearBalanceRepository.findAll().stream()
        .map(yearBalanceMapper::yearBalanceToYearBalanceDto)
        .collect(Collectors.toList());
  }

  @Override
  public YearBalanceDTO findById(Long id) {
    return yearBalanceRepository
        .findById(id)
        .map(yearBalanceMapper::yearBalanceToYearBalanceDto)
        .orElseThrow(() -> new ResourceNotFoundException(YEAR_BALANCE_NOT_FOUND + id));
  }

  @Override
  public YearBalanceDTO findYearBalanceByYear(int year) {
    YearBalance yearBalance = yearBalanceRepository.findByYear(year);
    if (yearBalance == null) {
      throw new ResourceNotFoundException(YEAR_BALANCE_NOT_FOUND + year);
    }
    return yearBalanceMapper.yearBalanceToYearBalanceDto(yearBalance);
  }

  @Override
  public YearBalanceDTO createNewYearBalance(YearBalanceDTO yearBalanceDTO) {
    return saveAndReturnDto(yearBalanceMapper.yearBalanceDtoToYearBalance(yearBalanceDTO));
  }

  @Override
  public YearBalanceDTO saveYearBalance(Long yearBalanceId, YearBalanceDTO yearBalanceDTO) {
    yearBalanceDTO.setId(yearBalanceId);
    return saveAndReturnDto(yearBalanceMapper.yearBalanceDtoToYearBalance(yearBalanceDTO));
  }

  @Override
  public YearBalanceDTO updateYearBalance(Long id, YearBalanceDTO yearBalanceDTO) {
    return yearBalanceRepository
        .findById(id)
        .map(
            yearBalance -> {
              if (yearBalanceDTO.getYear() != null) {
                yearBalance.setYear(yearBalanceDTO.getYear());
              }
              if (yearBalanceDTO.getExpenses() != null) {
                yearBalance.setExpenses(yearBalanceDTO.getExpenses());
              }
              if (yearBalanceDTO.getIncomes() != null) {
                yearBalance.setIncomes(yearBalanceDTO.getIncomes());
              }
              if (yearBalanceDTO.getResult() != null) {
                yearBalance.setResult(yearBalanceDTO.getResult());
              }
              if (yearBalanceDTO.getSalary() != null) {
                yearBalance.setSalary(yearBalanceDTO.getSalary());
              }
              if (CollectionUtils.isNotEmpty(yearBalanceDTO.getMonthBalances())) {
                yearBalance.setMonthBalanceList(
                    yearBalanceDTO.getMonthBalances().stream()
                        .map(monthBalanceMapper::monthBalanceDtoToMonthBalance)
                        .collect(Collectors.toList()));
              }
              return saveAndReturnDto(yearBalance);
            })
        .orElseThrow(() -> new ResourceNotFoundException(YEAR_BALANCE_NOT_FOUND + id));
  }

    @Override
    public void deleteYearBalance(Long id) {
        yearBalanceRepository.deleteById(id);
    }

    private YearBalanceDTO saveAndReturnDto(YearBalance yearBalance) {
    YearBalance savedYearBalance = yearBalanceRepository.save(yearBalance);
    return yearBalanceMapper.yearBalanceToYearBalanceDto(savedYearBalance);
  }
}
