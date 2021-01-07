package com.astoppello.incomebalanceapp.services;

import com.astoppello.incomebalanceapp.dto.domain.BankBalanceDTO;
import com.astoppello.incomebalanceapp.dto.mappers.BankBalanceMapper;
import com.astoppello.incomebalanceapp.exceptions.ResourceNotFoundException;
import com.astoppello.incomebalanceapp.model.BankBalance;
import com.astoppello.incomebalanceapp.model.MonthBalance;
import com.astoppello.incomebalanceapp.model.YearBalance;
import com.astoppello.incomebalanceapp.repositories.BankBalanceRepository;
import com.astoppello.incomebalanceapp.repositories.MonthBalanceRepository;
import com.astoppello.incomebalanceapp.repositories.YearBalanceRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/** Created by @author stopp on 28/11/2020 */
@Slf4j
@Service
public class BankBalanceServiceImpl implements BankBalanceService {

  private final BankBalanceRepository bankBalanceRepository;
  private final YearBalanceRepository yearBalanceRepository;
  private final MonthBalanceRepository monthBalanceRepository;
  private final BankBalanceMapper bankBalanceMapper;

  public BankBalanceServiceImpl(
      BankBalanceRepository bankBalanceRepository,
      BankBalanceMapper bankBalanceMapper,
      MonthBalanceRepository monthBalanceRepository,
      YearBalanceRepository yearBalanceRepository) {
    this.bankBalanceRepository = bankBalanceRepository;
    this.bankBalanceMapper = bankBalanceMapper;
    this.monthBalanceRepository = monthBalanceRepository;
    this.yearBalanceRepository = yearBalanceRepository;
  }

  @Override
  public List<BankBalanceDTO> findAllById(Long yearBalanceId, Long monthBalanceId) {
    return CollectionUtils.emptyIfNull(getYearBalanceById(yearBalanceId).getMonthBalanceList())
        .stream()
        .filter(monthBalance -> monthBalanceId.equals(monthBalance.getId()))
        .map(MonthBalance::getBankBalanceList)
        .flatMap(Collection::stream)
        .map(bankBalanceMapper::bankBalanceToBankBalanceDTO)
        .collect(Collectors.toList());
  }

  @Override
  public List<BankBalanceDTO> findAll() {
    return bankBalanceRepository.findAll().stream()
        .map(bankBalanceMapper::bankBalanceToBankBalanceDTO)
        .collect(Collectors.toList());
  }

  @Override
  public BankBalanceDTO findById(Long bankBalanceId) {
    return bankBalanceRepository
        .findById(bankBalanceId)
        .map(bankBalanceMapper::bankBalanceToBankBalanceDTO)
        .orElseThrow(() -> new ResourceNotFoundException(BANK_BALANCE_NOT_FOUND + bankBalanceId));
  }

  @Override
  public List<BankBalanceDTO> findByBankName(String bankName) {
    return bankBalanceRepository.findAll().stream()
        .filter(bankBalance -> Objects.nonNull(bankBalance.getBank()))
        .filter(bankBalance -> bankName.equals(bankBalance.getBank().getName()))
        .map(bankBalanceMapper::bankBalanceToBankBalanceDTO)
        .collect(Collectors.toList());
  }

  @Override
  public BankBalanceDTO createNewBankBalance(BankBalanceDTO bankBalanceDTO) {
    return createAndReturnDto(bankBalanceMapper.bankBalanceDtoToBankBalance(bankBalanceDTO));
  }

  private BankBalanceDTO createAndReturnDto(BankBalance bankBalance) {
    BankBalance savedBankBalance = bankBalanceRepository.save(bankBalance);
    if (savedBankBalance.getMonthBalance() != null
        && savedBankBalance.getMonthBalance().getId() != null) {
      monthBalanceRepository
          .findById(savedBankBalance.getMonthBalance().getId())
          .ifPresent(
              monthBalance -> {
                monthBalance.addBankBalance(savedBankBalance);
                monthBalanceRepository.save(monthBalance);
              });
    }
    return bankBalanceMapper.bankBalanceToBankBalanceDTO(savedBankBalance);
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
