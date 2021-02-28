package com.astoppello.incomebalanceapp.services;

import com.astoppello.incomebalanceapp.dto.domain.BankBalanceDTO;
import com.astoppello.incomebalanceapp.dto.domain.BankDTO;
import com.astoppello.incomebalanceapp.dto.mappers.BankBalanceMapper;
import com.astoppello.incomebalanceapp.exceptions.ResourceNotFoundException;
import com.astoppello.incomebalanceapp.model.Bank;
import com.astoppello.incomebalanceapp.model.BankBalance;
import com.astoppello.incomebalanceapp.model.MonthBalance;
import com.astoppello.incomebalanceapp.model.YearBalance;
import com.astoppello.incomebalanceapp.repositories.BankBalanceRepository;
import com.astoppello.incomebalanceapp.repositories.BankRepository;
import com.astoppello.incomebalanceapp.repositories.MonthBalanceRepository;
import com.astoppello.incomebalanceapp.repositories.YearBalanceRepository;
import com.astoppello.incomebalanceapp.utils.BankBalanceUtils;
import com.astoppello.incomebalanceapp.utils.MonthBalanceUtils;
import com.astoppello.incomebalanceapp.utils.YearBalanceUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
  private final BankRepository bankRepository;

  public BankBalanceServiceImpl(
      BankBalanceRepository bankBalanceRepository,
      BankBalanceMapper bankBalanceMapper,
      MonthBalanceRepository monthBalanceRepository,
      YearBalanceRepository yearBalanceRepository,
      BankRepository bankRepository) {
    this.bankBalanceRepository = bankBalanceRepository;
    this.bankBalanceMapper = bankBalanceMapper;
    this.monthBalanceRepository = monthBalanceRepository;
    this.yearBalanceRepository = yearBalanceRepository;
    this.bankRepository = bankRepository;
  }

  @Override
  public List<BankBalanceDTO> findAllByIds(Long yearBalanceId, Long monthBalanceId) {
    log.info(
        "Get all BankBalance by yearBalanceId "
            + yearBalanceId
            + "monthBalanceId "
            + monthBalanceId);
    return CollectionUtils.emptyIfNull(getYearBalanceById(yearBalanceId).getMonthBalanceList())
        .stream()
        .filter(monthBalance -> monthBalanceId.equals(monthBalance.getId()))
        .map(MonthBalance::getBankBalanceList)
        .filter(Objects::nonNull)
        .flatMap(Collection::stream)
        .map(bankBalanceMapper::bankBalanceToBankBalanceDTO)
        .collect(Collectors.toList());
  }

  @Override
  public List<BankBalanceDTO> findAllByYearBalanceId(Long yearBalanceId) {
    log.info("Get all BankBalance by yearBalanceId " + yearBalanceId);
    return CollectionUtils.emptyIfNull(getYearBalanceById(yearBalanceId).getBankBalanceList())
        .stream()
        .map(bankBalanceMapper::bankBalanceToBankBalanceDTO)
        .collect(Collectors.toList());
  }

  @Override
  public BankBalanceDTO createNewBankBalanceById(
      Long yearBalanceId, Long monthBalanceId, BankBalanceDTO bankBalanceDTO) {
    log.info(
        "Create BankBalance by yearBalanceId "
            + yearBalanceId
            + "monthBalanceId "
            + monthBalanceId
            + ". BankBalance: "
            + bankBalanceDTO);
    BankBalance bankBalance = bankBalanceMapper.bankBalanceDtoToBankBalance(bankBalanceDTO);
    bankBalance.setYearBalance(getYearBalanceById(yearBalanceId));
    bankBalance.setMonthBalance(getMonthBalanceById(monthBalanceId));
    return createAndReturnDto(bankBalance);
  }

  @Override
  public List<BankBalanceDTO> findAll() {
    log.info("Get all BankBalance");
    return bankBalanceRepository.findAll().stream()
        .map(bankBalanceMapper::bankBalanceToBankBalanceDTO)
        .collect(Collectors.toList());
  }

  @Override
  public BankBalanceDTO findById(Long bankBalanceId) {
    log.info("Get BankBalance id " + bankBalanceId);
    return bankBalanceRepository
        .findById(bankBalanceId)
        .map(bankBalanceMapper::bankBalanceToBankBalanceDTO)
        .orElseThrow(() -> new ResourceNotFoundException(BANK_BALANCE_NOT_FOUND + bankBalanceId));
  }

  @Override
  public List<BankBalanceDTO> findByBankName(String bankName) {
    log.info("Get BankBalance bankName " + bankName);
    return bankBalanceRepository.findAll().stream()
        .filter(bankBalance -> Objects.nonNull(bankBalance.getBank()))
        .filter(bankBalance -> bankName.equals(bankBalance.getBank().getName()))
        .map(bankBalanceMapper::bankBalanceToBankBalanceDTO)
        .collect(Collectors.toList());
  }

  @Override
  public BankBalanceDTO createNewBankBalance(BankBalanceDTO bankBalanceDTO) {
    log.info("Create BankBalance " + bankBalanceDTO);
    BankBalance bankBalance = bankBalanceMapper.bankBalanceDtoToBankBalance(bankBalanceDTO);
    setMonthBalanceIfPresent(bankBalance, bankBalanceDTO.getMonthBalanceId());
    setYearBalanceIfPresent(bankBalance, bankBalanceDTO.getYearBalanceId());
    return createAndReturnDto(bankBalance);
  }

  @Override
  public BankBalanceDTO saveBankBalance(Long id, BankBalanceDTO bankBalanceDTO) {
    log.info("Put BankBalance id " + id + ". BankBalance " + bankBalanceDTO);
    BankBalance bankBalance = bankBalanceMapper.bankBalanceDtoToBankBalance(bankBalanceDTO);
    bankBalance.setId(id);
    setMonthBalanceIfPresent(bankBalance, bankBalanceDTO.getMonthBalanceId());
    setYearBalanceIfPresent(bankBalance, bankBalanceDTO.getYearBalanceId());
    return createAndReturnDto(bankBalance);
  }

  @Override
  public BankBalanceDTO updateBankBalance(Long bankBalanceId, BankBalanceDTO bankBalanceDTO) {
    log.info("Patch BankBalance id " + bankBalanceId + ". BankBalance " + bankBalanceDTO);
    return bankBalanceRepository
        .findById(bankBalanceId)
        .map(
            bankBalance -> {
              if (bankBalanceDTO.getExpenses() != null) {
                bankBalance.setExpenses(new BigDecimal(bankBalanceDTO.getExpenses()));
              }
              if (bankBalanceDTO.getIncomes() != null) {
                bankBalance.setIncomes(new BigDecimal(bankBalanceDTO.getIncomes()));
              }
              if (bankBalanceDTO.getResult() != null) {
                bankBalance.setResult(new BigDecimal(bankBalanceDTO.getResult()));
              }
              setMonthBalanceIfPresent(bankBalance, bankBalanceDTO.getMonthBalanceId());
              setYearBalanceIfPresent(bankBalance, bankBalanceDTO.getYearBalanceId());
              return createAndReturnDto(bankBalance);
            })
        .orElseThrow(() -> new ResourceNotFoundException(BANK_BALANCE_NOT_FOUND + bankBalanceId));
  }

  @Override
  public void deleteBankBalance(Long bankBalanceId) {
    bankBalanceRepository
        .findById(bankBalanceId)
        .ifPresent(this::updateCollateralBalancesForDelete);
    log.info("Delete BankBalance " + bankBalanceId);
    bankBalanceRepository.deleteById(bankBalanceId);
  }

  private BankBalanceDTO createAndReturnDto(BankBalance bankBalance) {
    bankBalance.setResult(BankBalanceUtils.computeResult(bankBalance));
    saveBankToBankBalance(bankBalance, bankBalance.getBank());
    BankBalance savedBankBalance = bankBalanceRepository.save(bankBalance);
    updateCollateralBalances(savedBankBalance);
    return bankBalanceMapper.bankBalanceToBankBalanceDTO(savedBankBalance);
  }

  /**
   * Update MonthBalance and YearBalance once the BankBalance has been saved to DB.
   *
   * @param savedBankBalance
   */
  private void updateCollateralBalances(BankBalance savedBankBalance) {
    MonthBalance monthBalance = savedBankBalance.getMonthBalance();
    if (monthBalance != null) {
      log.info(
          "Updating MonthBalance: "
              + monthBalance.getId()
              + " with bankBalanceId "
              + savedBankBalance.getId());
      monthBalance.addBankBalance(savedBankBalance);
      MonthBalanceUtils.computeMontlyAmount(monthBalance);
      monthBalanceRepository.save(monthBalance);
    }
    YearBalance yearBalance = savedBankBalance.getYearBalance();
    if (yearBalance != null) {
      log.info(
          "Updating YearBalance: "
              + yearBalance.getId()
              + " with bankBalanceId "
              + savedBankBalance.getId());
      yearBalance.addBankBalance(savedBankBalance);
      YearBalanceUtils.computeYearlyAmount(yearBalance);
      yearBalanceRepository.save(yearBalance);
    }
  }

  /**
   * Remove bankBalance in MonthBalance and YEarBalance in case of delete
   *
   * @param bankBalance
   */
  private void updateCollateralBalancesForDelete(BankBalance bankBalance) {
    MonthBalance monthBalance = bankBalance.getMonthBalance();
    if (monthBalance != null) {
      log.info(
          "Removing bankBalanceId "
              + bankBalance.getId()
              + " from MonthBalance: "
              + monthBalance.getId());
      Objects.requireNonNull(monthBalance.getBankBalanceList()).remove(bankBalance);
      MonthBalanceUtils.computeMontlyAmount(monthBalance);
      monthBalanceRepository.save(monthBalance);
    }
    YearBalance yearBalance = bankBalance.getYearBalance();
    if (yearBalance != null) {
      log.info(
          "Removing bankBalanceId "
              + bankBalance.getId()
              + "from YearBalance: "
              + yearBalance.getId());
      yearBalance.getBankBalanceList().remove(bankBalance);
      YearBalanceUtils.computeYearlyAmount(yearBalance);
      yearBalanceRepository.save(yearBalance);
    }
  }

  private void setYearBalanceIfPresent(BankBalance bankBalance, Long yearBalanceId) {
    if (yearBalanceId != null) {
      yearBalanceRepository.findById(yearBalanceId).ifPresent(bankBalance::setYearBalance);
    }
  }

  private void setMonthBalanceIfPresent(BankBalance bankBalance, Long monthBalanceId) {
    if (monthBalanceId != null) {
      monthBalanceRepository.findById(monthBalanceId).ifPresent(bankBalance::setMonthBalance);
    }
  }

  private void saveBankToBankBalance(BankBalance bankBalance, Bank bank) {
    if (bank == null) {
      return;
    }
    log.info("Saving Bank: " + bank.getId() + " " + bank.getName());
    if (bank.getId() != null) {
      bankRepository.findById(bank.getId()).ifPresent(bankBalance::setBank);
    } else {
      final String name = bank.getName();
      if (StringUtils.isNotBlank(name)) {
        Bank bankByName = bankRepository.findBankByName(name);
        bankBalance.setBank(bankByName);
      } else {
        bankBalance.setBank(bankRepository.save(Bank.builder().name(name).build()));
      }
    }
  }

  private MonthBalance getMonthBalanceById(Long monthBalanceId) {
    return monthBalanceRepository
        .findById(monthBalanceId)
        .orElseThrow(
            () ->
                new ResourceNotFoundException(
                    MonthBalanceService.MONTH_BALANCE_NOT_FOUND + monthBalanceId));
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
