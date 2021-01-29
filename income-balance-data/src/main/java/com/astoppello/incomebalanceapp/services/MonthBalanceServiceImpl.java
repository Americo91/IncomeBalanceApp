package com.astoppello.incomebalanceapp.services;

import com.astoppello.incomebalanceapp.dto.domain.MonthBalanceDTO;
import com.astoppello.incomebalanceapp.dto.mappers.BankBalanceMapper;
import com.astoppello.incomebalanceapp.dto.mappers.MonthBalanceMapper;
import com.astoppello.incomebalanceapp.exceptions.ResourceNotFoundException;
import com.astoppello.incomebalanceapp.model.MonthBalance;
import com.astoppello.incomebalanceapp.model.YearBalance;
import com.astoppello.incomebalanceapp.repositories.MonthBalanceRepository;
import com.astoppello.incomebalanceapp.repositories.YearBalanceRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.astoppello.incomebalanceapp.services.YearBalanceService.YEAR_BALANCE_NOT_FOUND;

/** Created by @author stopp on 20/12/2020 */
@Service
public class MonthBalanceServiceImpl implements MonthBalanceService {

  private final MonthBalanceRepository monthBalanceRepository;
  private final MonthBalanceMapper monthBalanceMapper;
  private final YearBalanceRepository yearBalanceRepository;
  private final BankBalanceMapper bankBalanceMapper;

  public MonthBalanceServiceImpl(
      MonthBalanceRepository monthBalanceRepository,
      MonthBalanceMapper monthBalanceMapper,
      YearBalanceRepository yearBalanceRepository,
      BankBalanceMapper bankBalanceMapper) {
    this.monthBalanceRepository = monthBalanceRepository;
    this.monthBalanceMapper = monthBalanceMapper;
    this.yearBalanceRepository = yearBalanceRepository;
    this.bankBalanceMapper = bankBalanceMapper;
  }

  @Override
  public List<MonthBalanceDTO> findByMonth(String month) {
    return monthBalanceRepository.findAll().stream()
        .filter(monthBalance -> month.equals(monthBalance.getMonth()))
        .map(monthBalanceMapper::monthBalanceToMonthBalanceDto)
        .collect(Collectors.toList());
  }

  @Override
  public MonthBalanceDTO createNewMonthBalanceById(
      Long yearBalanceId, MonthBalanceDTO monthBalanceDTO) {
    MonthBalance monthBalance = monthBalanceMapper.monthBalanceDtoToMonthBalance(monthBalanceDTO);
    monthBalance.setYearBalance(getYearBalanceById(yearBalanceId));
    return createAndReturnDto(monthBalance);
  }


  @Override
  public List<MonthBalanceDTO> findAllById(Long yearBalanceId) {
    return CollectionUtils.emptyIfNull(getYearBalanceById(yearBalanceId).getMonthBalanceList())
        .stream()
        .map(monthBalanceMapper::monthBalanceToMonthBalanceDto)
        .collect(Collectors.toList());
  }
  @Override
  public MonthBalanceDTO findMonthOfYearById(Long yearBalanceId, Long monthBalanceId) {
    return CollectionUtils.emptyIfNull(getYearBalanceById(yearBalanceId).getMonthBalanceList())
        .stream()
        .filter(monthBalance -> monthBalanceId.equals(monthBalance.getId()))
        .findFirst()
        .map(monthBalanceMapper::monthBalanceToMonthBalanceDto)
        .orElseThrow(() -> new ResourceNotFoundException(MONTH_BALANCE_NOT_FOUND + monthBalanceId));
  }

  @Override
  public List<MonthBalanceDTO> findAll() {
    return monthBalanceRepository.findAll().stream()
        .map(monthBalanceMapper::monthBalanceToMonthBalanceDto)
        .collect(Collectors.toList());
  }

  @Override
  public MonthBalanceDTO findById(Long id) {
    return monthBalanceRepository
        .findById(id)
        .map(monthBalanceMapper::monthBalanceToMonthBalanceDto)
        .orElseThrow(() -> new ResourceNotFoundException(MONTH_BALANCE_NOT_FOUND + id));
  }

  @Override
  public MonthBalanceDTO createNewMonthBalance(MonthBalanceDTO monthBalanceDTO) {
    MonthBalance monthBalance = monthBalanceMapper.monthBalanceDtoToMonthBalance(monthBalanceDTO);
    if (monthBalanceDTO.getYearBalanceId() != null) {
      yearBalanceRepository
          .findById(monthBalanceDTO.getYearBalanceId())
          .ifPresent(monthBalance::setYearBalance);
    }
    return createAndReturnDto(monthBalance);
  }

  @Override
  public MonthBalanceDTO saveMonthBalance(Long monthBalanceId, MonthBalanceDTO monthBalanceDTO) {
    MonthBalance monthBalance = monthBalanceMapper.monthBalanceDtoToMonthBalance(monthBalanceDTO);
    monthBalance.setId(monthBalanceId);
    if (monthBalanceDTO.getYearBalanceId() != null) {
      yearBalanceRepository
          .findById(monthBalanceDTO.getYearBalanceId())
          .ifPresent(monthBalance::setYearBalance);
    }
    return createAndReturnDto(monthBalance);
  }

  @Override
  public MonthBalanceDTO updateMonthBalance(Long monthBalanceId, MonthBalanceDTO monthBalanceDTO) {
    return monthBalanceRepository
        .findById(monthBalanceId)
        .map(
            monthBalance -> {
              if (monthBalanceDTO.getExpenses() != null) {
                monthBalance.setExpenses(monthBalanceDTO.getExpenses());
              }
              if (monthBalanceDTO.getIncomes() != null) {
                monthBalance.setIncomes(monthBalanceDTO.getIncomes());
              }
              if (StringUtils.isNotBlank(monthBalanceDTO.getMonth())) {
                monthBalance.setMonth(monthBalanceDTO.getMonth());
              }
              if (monthBalanceDTO.getResult() != null) {
                monthBalance.setResult(monthBalanceDTO.getResult());
              }
              if (monthBalanceDTO.getSalary() != null) {
                monthBalance.setSalary(monthBalanceDTO.getSalary());
              }
              if (monthBalanceDTO.getYearBalanceId() != null) {
                yearBalanceRepository
                    .findById(monthBalanceDTO.getYearBalanceId())
                    .ifPresent(monthBalance::setYearBalance);
              }
              // TODO: Check this better
              if (CollectionUtils.isNotEmpty(monthBalanceDTO.getBankBalances())) {
                monthBalance.setBankBalanceList(
                    monthBalanceDTO.getBankBalances().stream()
                        .map(bankBalanceMapper::bankBalanceDtoToBankBalance)
                        .collect(Collectors.toList()));
              }
              return createAndReturnDto(monthBalance);
            })
        .orElseThrow(() -> new ResourceNotFoundException(MONTH_BALANCE_NOT_FOUND + monthBalanceId));
  }

  @Override
  public void delete(Long monthBalanceId) {
    monthBalanceRepository.deleteById(monthBalanceId);
  }

  private MonthBalanceDTO createAndReturnDto(MonthBalance monthBalance) {
    MonthBalance savedMonthBalance = monthBalanceRepository.save(monthBalance);
    if (savedMonthBalance.getYearBalance() != null) {
      savedMonthBalance.getYearBalance().addMonthBalance(savedMonthBalance);
      yearBalanceRepository.save(savedMonthBalance.getYearBalance());
    }
    return monthBalanceMapper.monthBalanceToMonthBalanceDto(savedMonthBalance);
  }

  private YearBalance getYearBalanceById(Long yearBalanceId) {
    return yearBalanceRepository
        .findById(yearBalanceId)
        .orElseThrow(() -> new ResourceNotFoundException(YEAR_BALANCE_NOT_FOUND + yearBalanceId));
  }
}
