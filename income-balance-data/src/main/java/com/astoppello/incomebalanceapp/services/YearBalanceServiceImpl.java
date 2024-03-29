package com.astoppello.incomebalanceapp.services;

import com.astoppello.incomebalanceapp.dto.domain.YearBalanceDTO;
import com.astoppello.incomebalanceapp.dto.mappers.YearBalanceMapper;
import com.astoppello.incomebalanceapp.exceptions.ResourceNotFoundException;
import com.astoppello.incomebalanceapp.model.YearBalance;
import com.astoppello.incomebalanceapp.repositories.YearBalanceRepository;
import com.astoppello.incomebalanceapp.utils.YearBalanceUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by @author stopp on 15/11/2020
 */
@Slf4j
@Service
public class YearBalanceServiceImpl implements YearBalanceService {

    private final YearBalanceRepository yearBalanceRepository;
    private final YearBalanceMapper yearBalanceMapper;

    public YearBalanceServiceImpl(YearBalanceRepository yearBalanceRepository, YearBalanceMapper yearBalanceMapper) {
        this.yearBalanceRepository = yearBalanceRepository;
        this.yearBalanceMapper = yearBalanceMapper;
    }

    @Override
    public List<YearBalanceDTO> findAll() {
        log.info("List all YearBalance");
        return yearBalanceRepository.findAll().stream().map(yearBalanceMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public YearBalanceDTO findById(Long id) {
        log.info("List YearBalance id: " + id);
        return yearBalanceRepository.findById(id).map(yearBalanceMapper::toDto)
                                    .orElseThrow(() -> new ResourceNotFoundException(YEAR_BALANCE_NOT_FOUND + id));
    }

    @Override
    public YearBalanceDTO findYearBalanceByYear(int year) {
        log.info("Find YearBalance by year: " + year);
        YearBalance yearBalance = yearBalanceRepository.findByYear(year);
        if (yearBalance == null) {
            throw new ResourceNotFoundException(YEAR_BALANCE_NOT_FOUND + year);
        }
        return yearBalanceMapper.toDto(yearBalance);
    }

    @Override
    public YearBalanceDTO createNewYearBalance(YearBalanceDTO yearBalanceDTO) {
        log.info("Create YearBalance: " + yearBalanceDTO);
        return saveAndReturnDto(yearBalanceMapper.toEntity(yearBalanceDTO));
    }

    @Override
    public YearBalanceDTO saveYearBalance(Long id, YearBalanceDTO yearBalanceDTO) {
        log.info("Put YearBalance id: " + id + "YearBalance: " + yearBalanceDTO);
        YearBalance yearbalance = yearBalanceMapper.toEntity(yearBalanceDTO);
        yearbalance.setId(id);
        return saveAndReturnDto(yearbalance);
    }

    @Override
    public YearBalanceDTO updateYearBalance(Long id, YearBalanceDTO yearBalanceDTO) {
        log.info("Patch YearBalance id: " + id + "YearBalance: " + yearBalanceDTO);
        return yearBalanceRepository.findById(id).map(yearBalance -> {
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
            return saveAndReturnDto(yearBalance);
        }).orElseThrow(() -> new ResourceNotFoundException(YEAR_BALANCE_NOT_FOUND + id));
    }

    @Override
    public YearBalanceDTO deleteYearBalance(Long id) {
        YearBalance yearBalance = yearBalanceRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(YEAR_BALANCE_NOT_FOUND + id));
        log.info("Delete YearBalance id: " + id);
        yearBalanceRepository.deleteById(id);
        return yearBalanceMapper.toDto(yearBalance);
    }

    private YearBalanceDTO saveAndReturnDto(YearBalance yearBalance) {
        YearBalanceUtils.computeYearlyAmount(yearBalance);
        YearBalance savedYearBalance = yearBalanceRepository.save(yearBalance);
        return yearBalanceMapper.toDto(savedYearBalance);
    }
}
