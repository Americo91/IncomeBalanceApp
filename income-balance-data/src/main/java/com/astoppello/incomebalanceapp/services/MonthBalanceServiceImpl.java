package com.astoppello.incomebalanceapp.services;

import com.astoppello.incomebalanceapp.dto.domain.MonthBalanceDTO;
import com.astoppello.incomebalanceapp.dto.mappers.MonthBalanceMapper;
import com.astoppello.incomebalanceapp.exceptions.ResourceNotFoundException;
import com.astoppello.incomebalanceapp.model.MonthBalance;
import com.astoppello.incomebalanceapp.model.YearBalance;
import com.astoppello.incomebalanceapp.repositories.MonthBalanceRepository;
import com.astoppello.incomebalanceapp.repositories.YearBalanceRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/** Created by @author stopp on 20/12/2020 */
@Service
public class MonthBalanceServiceImpl implements MonthBalanceService {

  private final MonthBalanceRepository monthBalanceRepository;
  private final MonthBalanceMapper monthBalanceMapper;
  private final YearBalanceRepository yearBalanceRepository;

  public MonthBalanceServiceImpl(
      MonthBalanceRepository monthBalanceRepository,
      MonthBalanceMapper monthBalanceMapper,
      YearBalanceRepository yearBalanceRepository) {
    this.monthBalanceRepository = monthBalanceRepository;
    this.monthBalanceMapper = monthBalanceMapper;
    this.yearBalanceRepository = yearBalanceRepository;
  }

  /*
  @Override
  public MonthBalanceDTO findByMonth(Long yearBalanceId, String month) {
    return CollectionUtils.emptyIfNull(getYearBalanceById(yearBalanceId).getMonthBalanceList())
        .stream()
        .filter(monthBalance -> month.equals(monthBalance.getMonth()))
        .findFirst()
        .map(monthBalanceMapper::monthBalanceToMonthBalanceDto)
        .orElseThrow(() -> new ResourceNotFoundException(MONTH_BALANCE_NOT_FOUND + month));
  }
   */

  @Override
  public MonthBalanceDTO createNewMonthBalance(
      Long yearBalanceId, MonthBalanceDTO monthBalanceDTO) {
    YearBalance yearBalance = getYearBalanceById(yearBalanceId);
    return savedAndReturnDto(
        yearBalance, monthBalanceMapper.monthBalanceDtoToMonthBalance(monthBalanceDTO));
  }

  private MonthBalanceDTO savedAndReturnDto(YearBalance yearBalance, MonthBalance monthBalance) {
    MonthBalance savedMonthBalance = monthBalanceRepository.save(monthBalance);
    yearBalance.addMonthBalance(savedMonthBalance);
    yearBalanceRepository.save(yearBalance);
    return monthBalanceMapper.monthBalanceToMonthBalanceDto(savedMonthBalance);
  }

  @Override
  public List<MonthBalanceDTO> findAll(Long yearBalanceId) {
    return CollectionUtils.emptyIfNull(getYearBalanceById(yearBalanceId).getMonthBalanceList())
        .stream()
        .map(monthBalanceMapper::monthBalanceToMonthBalanceDto)
        .collect(Collectors.toList());
  }

  @Override
  public MonthBalanceDTO findById(Long yearBalanceId, Long monthBalanceId) {
    return CollectionUtils.emptyIfNull(getYearBalanceById(yearBalanceId).getMonthBalanceList())
        .stream()
        .filter(monthBalance -> monthBalanceId.equals(monthBalance.getId()))
        .findFirst()
        .map(monthBalanceMapper::monthBalanceToMonthBalanceDto)
        .orElseThrow(() -> new ResourceNotFoundException(MONTH_BALANCE_NOT_FOUND + monthBalanceId));
  }

  private YearBalance getYearBalanceById(Long yearBalanceId) {
    return yearBalanceRepository
        .findById(yearBalanceId)
        .orElseThrow(
            () ->
                new ResourceNotFoundException(
                    YearBalanceService.YEAR_BALANCE_NOT_FOUND + yearBalanceId));
  }
}
