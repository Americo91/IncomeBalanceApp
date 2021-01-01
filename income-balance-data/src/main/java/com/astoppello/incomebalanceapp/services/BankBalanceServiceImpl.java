package com.astoppello.incomebalanceapp.services;

import com.astoppello.incomebalanceapp.dto.domain.BankBalanceDTO;
import com.astoppello.incomebalanceapp.dto.mappers.BankBalanceMapper;
import com.astoppello.incomebalanceapp.exceptions.ResourceNotFoundException;
import com.astoppello.incomebalanceapp.model.MonthBalance;
import com.astoppello.incomebalanceapp.model.YearBalance;
import com.astoppello.incomebalanceapp.repositories.YearBalanceRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/** Created by @author stopp on 28/11/2020 */
@Service
public class BankBalanceServiceImpl implements BankBalanceService {

  private final BankBalanceMapper mapper;
  private final YearBalanceRepository yearBalanceRepository;

  public BankBalanceServiceImpl(
      BankBalanceMapper mapper, YearBalanceRepository yearBalanceRepository) {
    this.mapper = mapper;
    this.yearBalanceRepository = yearBalanceRepository;
  }

  @Override
  public List<BankBalanceDTO> findAll(Long yearBalanceId, Long monthBalanceId) {
    return CollectionUtils.emptyIfNull(getYearBalanceById(yearBalanceId).getMonthBalanceList())
        .stream()
        .filter(monthBalance -> monthBalanceId.equals(monthBalance.getId()))
        .map(MonthBalance::getBankBalanceList)
        .flatMap(Collection::stream)
        .map(mapper::bankBalanceToBankBalanceDTO)
        .collect(Collectors.toList());
  }

  @Override
  public BankBalanceDTO findById(Long yearBalanceId, Long monthBalanceId, Long bankBalanceId) {
    return CollectionUtils.emptyIfNull(getYearBalanceById(yearBalanceId).getMonthBalanceList())
        .stream()
        .filter(monthBalance -> monthBalanceId.equals(monthBalance.getId()))
        .map(MonthBalance::getBankBalanceList)
        .flatMap(Collection::stream)
        .filter(bankBalance -> bankBalanceId.equals(bankBalance.getId()))
        .findFirst()
        .map(mapper::bankBalanceToBankBalanceDTO)
        .orElseThrow(() -> new ResourceNotFoundException(BANK_BALANCE_NOT_FOUND + bankBalanceId));
  }

  @Override
  public BankBalanceDTO findByBankName(Long yearBalanceId, Long monthBalanceId, String bankName) {
    return CollectionUtils.emptyIfNull(getYearBalanceById(yearBalanceId).getMonthBalanceList())
                          .stream()
                          .filter(monthBalance -> monthBalanceId.equals(monthBalance.getId()))
                          .map(MonthBalance::getBankBalanceList)
                          .flatMap(Collection::stream)
                          .filter(bankBalance -> Objects.nonNull(bankBalance.getBank()))
                          .filter(bankBalance -> bankName.equals(bankBalance.getBank().getName()))
                          .findFirst()
                          .map(mapper::bankBalanceToBankBalanceDTO)
                          .orElseThrow(() -> new ResourceNotFoundException(BANK_BALANCE_NOT_FOUND + bankName));
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
