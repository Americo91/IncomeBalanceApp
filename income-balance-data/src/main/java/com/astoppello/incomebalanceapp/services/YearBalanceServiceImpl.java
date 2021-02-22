package com.astoppello.incomebalanceapp.services;

import com.astoppello.incomebalanceapp.dto.domain.YearBalanceDTO;
import com.astoppello.incomebalanceapp.dto.mappers.BankBalanceMapper;
import com.astoppello.incomebalanceapp.dto.mappers.MonthBalanceMapper;
import com.astoppello.incomebalanceapp.dto.mappers.YearBalanceMapper;
import com.astoppello.incomebalanceapp.exceptions.ResourceNotFoundException;
import com.astoppello.incomebalanceapp.model.BankBalance;
import com.astoppello.incomebalanceapp.model.MonthBalance;
import com.astoppello.incomebalanceapp.model.YearBalance;
import com.astoppello.incomebalanceapp.repositories.YearBalanceRepository;
import com.astoppello.incomebalanceapp.utils.YearBalanceUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/** Created by @author stopp on 15/11/2020 */
@Slf4j
@Service
public class YearBalanceServiceImpl implements YearBalanceService {

  private final YearBalanceRepository yearBalanceRepository;
  private final YearBalanceMapper yearBalanceMapper;
  private final MonthBalanceMapper monthBalanceMapper;
  private final BankBalanceMapper bankBalanceMapper;

  public YearBalanceServiceImpl(
      YearBalanceRepository yearBalanceRepository,
      YearBalanceMapper yearBalanceMapper,
      MonthBalanceMapper monthBalanceMapper,
      BankBalanceMapper bankBalanceMapper) {
    this.yearBalanceRepository = yearBalanceRepository;
    this.yearBalanceMapper = yearBalanceMapper;
    this.monthBalanceMapper = monthBalanceMapper;
    this.bankBalanceMapper = bankBalanceMapper;
  }

  @Override
  public List<YearBalanceDTO> findAll() {
    log.info("List all YearBalance");
    return yearBalanceRepository.findAll().stream()
        .map(yearBalanceMapper::yearBalanceToYearBalanceDto)
        .collect(Collectors.toList());
  }

  @Override
  public YearBalanceDTO findById(Long id) {
    log.info("List YearBalance id: " + id);
    return yearBalanceRepository
        .findById(id)
        .map(yearBalanceMapper::yearBalanceToYearBalanceDto)
        .orElseThrow(() -> new ResourceNotFoundException(YEAR_BALANCE_NOT_FOUND + id));
  }

  @Override
  public YearBalanceDTO findYearBalanceByYear(int year) {
    log.info("Find YearBalance by year: " + year);
    YearBalance yearBalance = yearBalanceRepository.findByYear(year);
    if (yearBalance == null) {
      throw new ResourceNotFoundException(YEAR_BALANCE_NOT_FOUND + year);
    }
    return yearBalanceMapper.yearBalanceToYearBalanceDto(yearBalance);
  }

  @Override
  public YearBalanceDTO createNewYearBalance(YearBalanceDTO yearBalanceDTO) {
    log.info("Create YearBalance: " + yearBalanceDTO);
    return saveAndReturnDto(yearBalanceMapper.yearBalanceDtoToYearBalance(yearBalanceDTO));
  }

  @Override
  public YearBalanceDTO saveYearBalance(Long id, YearBalanceDTO yearBalanceDTO) {
    log.info("Put YearBalance id: " + id + "YearBalance: " + yearBalanceDTO);
    YearBalance yearbalance = yearBalanceMapper.yearBalanceDtoToYearBalance(yearBalanceDTO);
    yearbalance.setId(id);
    return saveAndReturnDto(yearbalance);
  }

  @Override
  public YearBalanceDTO updateYearBalance(Long id, YearBalanceDTO yearBalanceDTO) {
    log.info("Patch YearBalance id: " + id + "YearBalance: " + yearBalanceDTO);
    return yearBalanceRepository
        .findById(id)
        .map(
            yearBalance -> {
              if (yearBalanceDTO.getYear() != null) {
                yearBalance.setYear(yearBalanceDTO.getYear());
              }
              if (yearBalanceDTO.getExpenses() != null) {
                yearBalance.setExpenses(new BigDecimal(yearBalanceDTO.getExpenses()));
              }
              if (yearBalanceDTO.getIncomes() != null) {
                yearBalance.setIncomes(new BigDecimal(yearBalanceDTO.getIncomes()));
              }
              if (yearBalanceDTO.getResult() != null) {
                yearBalance.setResult(new BigDecimal(yearBalanceDTO.getResult()));
              }
              if (yearBalanceDTO.getSalary() != null) {
                yearBalance.setSalary(new BigDecimal(yearBalanceDTO.getSalary()));
              }
              if (CollectionUtils.isNotEmpty(yearBalanceDTO.getMonthBalances())) {
                yearBalance.setMonthBalanceList(
                    yearBalanceDTO.getMonthBalances().stream()
                        .map(monthBalanceMapper::monthBalanceDtoToMonthBalance)
                        .collect(Collectors.toList()));
              }
              if (CollectionUtils.isNotEmpty(yearBalanceDTO.getBankBalances())) {
                yearBalance.setBankBalanceList(
                    yearBalanceDTO.getBankBalances().stream()
                        .map(bankBalanceMapper::bankBalanceDtoToBankBalance)
                        .collect(Collectors.toList()));
              }
              return saveAndReturnDto(yearBalance);
            })
        .orElseThrow(() -> new ResourceNotFoundException(YEAR_BALANCE_NOT_FOUND + id));
  }

  @Override
  public void deleteYearBalance(Long id) {
    log.info("Delete YearBalance id: " + id);
    yearBalanceRepository.deleteById(id);
  }

  private YearBalanceDTO saveAndReturnDto(YearBalance yearBalance) {
    addYearBalanceToBankBalance(yearBalance);
    addYearBalanceToMonthBalance(yearBalance);
    YearBalanceUtils.computeYearlyAmount(yearBalance);
    YearBalance savedYearBalance = yearBalanceRepository.save(yearBalance);
    return yearBalanceMapper.yearBalanceToYearBalanceDto(savedYearBalance);
  }

  private void addYearBalanceToMonthBalance(YearBalance yearBalance) {
    CollectionUtils.emptyIfNull(yearBalance.getMonthBalanceList()).stream()
        .filter(Objects::nonNull)
        .forEach(
            monthBalance -> {
              monthBalance.setYearBalance(yearBalance);
            });
  }

  private void addYearBalanceToBankBalance(YearBalance yearBalance) {
    CollectionUtils.emptyIfNull(yearBalance.getBankBalanceList()).stream()
        .filter(Objects::nonNull)
        .forEach(
            bankBalance -> {
              bankBalance.setYearBalance(yearBalance);
            });
  }
}
