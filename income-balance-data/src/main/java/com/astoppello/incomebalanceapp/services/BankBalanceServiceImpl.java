package com.astoppello.incomebalanceapp.services;

import com.astoppello.incomebalanceapp.dto.domain.BankBalanceDTO;
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
import org.apache.logging.log4j.message.StringFormattedMessage;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by @author stopp on 28/11/2020
 */
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
        log.info("Get all BankBalance by yearBalanceId " + yearBalanceId + "monthBalanceId " + monthBalanceId);
        return CollectionUtils.emptyIfNull(getYearBalanceById(yearBalanceId).getMonthBalanceSet())
                              .stream()
                              .filter(monthBalance -> monthBalanceId.equals(monthBalance.getId()))
                              .map(MonthBalance::getBankBalanceSet)
                              .flatMap(Collection::stream)
                              .map(bankBalanceMapper::bankBalanceToBankBalanceDTO)
                              .collect(Collectors.toList());
    }

    @Override
    public List<BankBalanceDTO> findAllByYearBalanceId(Long yearBalanceId) {
        log.info("Get all BankBalance by yearBalanceId " + yearBalanceId);
        return CollectionUtils.emptyIfNull(getYearBalanceById(yearBalanceId).getBankBalanceSet())
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
        final YearBalance yearBalance = getYearBalanceById(yearBalanceId);
        bankBalance.setYearBalance(yearBalance);
        bankBalance.setMonthBalance(getMonthBalanceByYearBalance(yearBalance, monthBalanceId));
        return createAndReturnDto(bankBalance);
    }

    @Override
    public List<BankBalanceDTO> findAll() {
        log.info("Get all BankBalance");
        return bankBalanceRepository.findAll()
                                    .stream()
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
        return bankBalanceRepository.findAll()
                                    .stream()
                                    .filter(bankBalance -> bankName.equals(bankBalance.getBank()
                                                                                      .getName()))
                                    .map(bankBalanceMapper::bankBalanceToBankBalanceDTO)
                                    .collect(Collectors.toList());
    }

    @Override
    public BankBalanceDTO createNewBankBalance(BankBalanceDTO bankBalanceDTO) {
        log.info("Create BankBalance " + bankBalanceDTO);
        BankBalance bankBalance = bankBalanceMapper.bankBalanceDtoToBankBalance(bankBalanceDTO);
        YearBalance yearBalance = getYearBalanceById(bankBalanceDTO.getYearBalanceId());
        MonthBalance monthBalance = getMonthBalanceByYearBalance(yearBalance, bankBalanceDTO.getMonthBalanceId());
        bankBalance.setYearBalance(yearBalance);
        bankBalance.setMonthBalance(monthBalance);
        return createAndReturnDto(bankBalance);
    }

    @Override
    public BankBalanceDTO saveBankBalance(Long id, BankBalanceDTO bankBalanceDTO) {
        log.info("Put BankBalance id " + id + ". BankBalance " + bankBalanceDTO);
        BankBalance bankBalance = bankBalanceMapper.bankBalanceDtoToBankBalance(bankBalanceDTO);
        bankBalance.setId(id);
        YearBalance yearBalance = getYearBalanceById(bankBalanceDTO.getYearBalanceId());
        MonthBalance monthBalance = getMonthBalanceByYearBalance(yearBalance, bankBalanceDTO.getMonthBalanceId());
        bankBalance.setYearBalance(yearBalance);
        bankBalance.setMonthBalance(monthBalance);
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
                            if (bankBalanceDTO.getBank() != null) {
                                final Bank bank = new Bank(bankBalanceDTO.getBank()
                                                                         .getId(),
                                        bankBalanceDTO.getBank()
                                                      .getName());
                                bankBalance.setBank(bank);
                            }
                            MonthBalance oldMonthBalance = bankBalance.getMonthBalance();
                            YearBalance oldYearBalance = bankBalance.getYearBalance();
                            if (bankBalanceDTO.getYearBalanceId() != null) {
                                YearBalance yearBalance = getYearBalanceById(bankBalanceDTO.getYearBalanceId());
                                bankBalance.setYearBalance(yearBalance);
                            }
                            if (bankBalanceDTO.getMonthBalanceId() != null) {
                                MonthBalance monthBalance = getMonthBalanceByYearBalance(bankBalance.getYearBalance(),
                                        bankBalanceDTO.getMonthBalanceId());
                                bankBalance.setMonthBalance(monthBalance);
                            }
                            updateCollateralBalancesForDelete(oldYearBalance, oldMonthBalance, bankBalance);
                            BankBalanceDTO savedBankBalanceDto = createAndReturnDto(bankBalance);
                            return savedBankBalanceDto;
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
        MonthBalance savedMonthBalance = updateMonthBalance(savedBankBalance);
        updateYearBalance(savedBankBalance, savedMonthBalance);
    }

    private void updateYearBalance(BankBalance savedBankBalance, MonthBalance monthBalance) {
        YearBalance yearBalance = savedBankBalance.getYearBalance();
        log.info("Updating YearBalance {} with {} and {}",
                yearBalance.getId(),
                monthBalance,
                savedBankBalance);
        yearBalance.addMonthBalance(monthBalance);
        yearBalance.addBankBalance(savedBankBalance);
        YearBalanceUtils.computeYearlyAmount(yearBalance);
        yearBalanceRepository.save(yearBalance);

    }

    private MonthBalance updateMonthBalance(BankBalance savedBankBalance) {
        MonthBalance monthBalance = savedBankBalance.getMonthBalance();
        log.info("Updating MonthBalance: "
                + monthBalance.getId()
                + " with bankBalance: "
                + savedBankBalance);
        monthBalance.addBankBalance(savedBankBalance);
        MonthBalanceUtils.computeMontlyAmount(monthBalance);
        return monthBalanceRepository.save(monthBalance);
    }

    /**
     * Remove bankBalance in MonthBalance and YEarBalance in case of update
     *
     * @param yearBalance
     * @param monthBalance
     * @param bankBalance
     */
    private void updateCollateralBalancesForDelete(YearBalance yearBalance, MonthBalance monthBalance,
                                                   BankBalance bankBalance) {
        log.info("Removing bankBalanceId "
                + bankBalance.getId()
                + " from MonthBalance: "
                + monthBalance.getId());
        CollectionUtils.emptyIfNull(monthBalance.getBankBalanceSet())
                       .remove(bankBalance);
        MonthBalanceUtils.computeMontlyAmount(monthBalance);
        monthBalanceRepository.save(monthBalance);

        log.info("Removing bankBalanceId "
                + bankBalance.getId()
                + "from YearBalance: "
                + yearBalance.getId());
        yearBalance.getBankBalanceSet()
                   .remove(bankBalance);
        YearBalanceUtils.computeYearlyAmount(yearBalance);
        yearBalanceRepository.save(yearBalance);
    }

    /**
     * Remove bankBalance in MonthBalance and YEarBalance in case of delete
     *
     * @param bankBalance
     */
    private void updateCollateralBalancesForDelete(@NonNull BankBalance bankBalance) {
        updateCollateralBalancesForDelete(bankBalance.getYearBalance(), bankBalance.getMonthBalance(), bankBalance);
    }

    private void saveBankToBankBalance(BankBalance bankBalance, Bank bank) {
        if (bank == null)
            return;
        saveBankToBankBalance(bankBalance, bank.getId(), bank.getName());
    }

    private void saveBankToBankBalance(BankBalance bankBalance, Long id, String bankName) {
        if (id == null && StringUtils.isBlank(bankName)) {
            return;
        }
        log.info("Adding Bank: id {}, name {} to BankBalance {}", id, bankName, bankBalance);
        if (id != null) {
            bankRepository.findById(id)
                          .ifPresent(bankBalance::setBank);
        } else if (StringUtils.isNotBlank(bankName) && bankRepository.findBankByName(bankName) != null) {
            bankBalance.setBank(bankRepository.findBankByName(bankName));
        } else {
            bankBalance.setBank(bankRepository.save(Bank.builder()
                                                        .name(bankName)
                                                        .build()));
        }
    }

    private MonthBalance getMonthBalanceByYearBalance(YearBalance yearBalance, Long monthBalanceId) {
        return CollectionUtils
                .emptyIfNull(yearBalance.getMonthBalanceSet())
                .stream()
                .filter(monthBalance -> monthBalanceId.equals(monthBalance.getId()))
                .findFirst()
                .orElseThrow(() ->
                        new ResourceNotFoundException(String.format("YearBalance %d doesn't contain MonthBalance %d",
                                yearBalance.getId(), monthBalanceId)));
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
