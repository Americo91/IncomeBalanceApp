package com.astoppello.incomebalanceapp.services;

import com.astoppello.incomebalanceapp.dto.domain.MonthBalanceDTO;
import com.astoppello.incomebalanceapp.dto.mappers.MonthBalanceMapper;
import com.astoppello.incomebalanceapp.exceptions.ResourceNotFoundException;
import com.astoppello.incomebalanceapp.model.MonthBalance;
import com.astoppello.incomebalanceapp.model.YearBalance;
import com.astoppello.incomebalanceapp.repositories.MonthBalanceRepository;
import com.astoppello.incomebalanceapp.repositories.YearBalanceRepository;
import com.astoppello.incomebalanceapp.utils.MonthBalanceUtils;
import com.astoppello.incomebalanceapp.utils.YearBalanceUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static com.astoppello.incomebalanceapp.services.YearBalanceService.YEAR_BALANCE_NOT_FOUND;

/**
 * Created by @author stopp on 20/12/2020
 */
@Slf4j
@Service
public class MonthBalanceServiceImpl implements MonthBalanceService {

    private final MonthBalanceRepository monthBalanceRepository;
    private final MonthBalanceMapper monthBalanceMapper;
    private final YearBalanceRepository yearBalanceRepository;

    public MonthBalanceServiceImpl(MonthBalanceRepository monthBalanceRepository,
                                   MonthBalanceMapper monthBalanceMapper, YearBalanceRepository yearBalanceRepository) {
        this.monthBalanceRepository = monthBalanceRepository;
        this.monthBalanceMapper = monthBalanceMapper;
        this.yearBalanceRepository = yearBalanceRepository;
    }

    @Override
    public List<MonthBalanceDTO> findByMonth(String month) {
        log.info("Find MonthBalance by month: " + month);
        return monthBalanceRepository.findAll().stream()
                                     .filter(monthBalance -> month.equals(monthBalance.getMonth().name()))
                                     .map(monthBalanceMapper::toDto)
                                     .collect(Collectors.toList());
    }

    @Override
    public MonthBalanceDTO createNewMonthBalanceById(Long yearBalanceId, MonthBalanceDTO monthBalanceDTO) {
        log.info("Create MonthBalance.  yearBalanceId: " + yearBalanceId + ". MonthBalance: " + monthBalanceDTO);
        MonthBalance monthBalance = monthBalanceMapper.toEntity(monthBalanceDTO);
        monthBalance.setYearBalance(getYearBalanceById(yearBalanceId));
        return createAndReturnDto(monthBalance);
    }

    @Override
    public List<MonthBalanceDTO> findAllById(Long yearBalanceId) {
        log.info("Find all MonthBalance of yearBalanceId: " + yearBalanceId);
        return CollectionUtils.emptyIfNull(getYearBalanceById(yearBalanceId).getMonthBalanceSet()).stream()
                              .map(monthBalanceMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<MonthBalanceDTO> findAll() {
        log.info("Find all MonthBalance");
        return monthBalanceRepository.findAll().stream().map(monthBalanceMapper::toDto)
                                     .collect(Collectors.toList());
    }

    @Override
    public MonthBalanceDTO findById(Long id) {
        log.info("Find MonthBalance id:" + id);
        return monthBalanceRepository.findById(id).map(monthBalanceMapper::toDto)
                                     .orElseThrow(() -> new ResourceNotFoundException(MONTH_BALANCE_NOT_FOUND + id));
    }

    @Override
    public MonthBalanceDTO saveMonthBalance(Long id, MonthBalanceDTO monthBalanceDTO) {
        log.info("Put MonthBalance id: " + id + "MonthBalance: " + monthBalanceDTO);
        MonthBalance monthBalance = monthBalanceMapper.toEntity(monthBalanceDTO);
        monthBalance.setId(id);
        setYearBalanceIfPresent(monthBalanceDTO.getYearBalanceId(), monthBalance);
        return createAndReturnDto(monthBalance);
    }

    @Override
    public MonthBalanceDTO updateMonthBalance(Long monthBalanceId, MonthBalanceDTO monthBalanceDTO) {
        log.info("Patch MonthBalance with id: " + monthBalanceId + "MonthBalance: " + monthBalanceDTO);
        return monthBalanceRepository.findById(monthBalanceId).map(monthBalance -> {
            if (monthBalanceDTO.getExpenses()!= null) {
                monthBalance.setExpenses(monthBalanceDTO.getExpenses());
            }
            if (monthBalanceDTO.getIncomes()!= null) {
                monthBalance.setIncomes(monthBalanceDTO.getIncomes());
            }
            if (monthBalanceDTO.getMonth() != null && StringUtils.isNotBlank(monthBalanceDTO.getMonth().name())) {
                monthBalance.setMonth(monthBalanceDTO.getMonth());
            }
            if (monthBalanceDTO.getResult() != null) {
                monthBalance.setResult(monthBalanceDTO.getResult());
            }
            if (monthBalanceDTO.getSalary()!= null) {
                monthBalance.setSalary(monthBalanceDTO.getSalary());
            }
            setYearBalanceIfPresent(monthBalanceDTO.getYearBalanceId(), monthBalance);
            return createAndReturnDto(monthBalance);
        }).orElseThrow(() -> new ResourceNotFoundException(MONTH_BALANCE_NOT_FOUND + monthBalanceId));
    }

    @Override
    public MonthBalanceDTO delete(Long monthBalanceId) {
        MonthBalance monthBalance = monthBalanceRepository.findById(monthBalanceId).orElseThrow(
                () -> new ResourceNotFoundException(MONTH_BALANCE_NOT_FOUND + monthBalanceId));
        log.info("Delete MonthBalance: " + monthBalanceId);
        updateYearBalanceForDelete(monthBalance);
        monthBalanceRepository.deleteById(monthBalanceId);
        return monthBalanceMapper.toDto(monthBalance);
    }

    private void updateYearBalanceForDelete(MonthBalance monthBalance) {
        final YearBalance yearBalance = monthBalance.getYearBalance();
        log.info("Removing MonthBalance " + monthBalance.getId() + "from YearBalance " + yearBalance.getId());
        yearBalance.getMonthBalanceSet().remove(monthBalance);
        YearBalanceUtils.computeYearlyAmount(yearBalance);
        yearBalanceRepository.save(yearBalance);
    }

    private void setYearBalanceIfPresent(Long yearBalanceId, MonthBalance monthBalance) {
        if (yearBalanceId != null) {
            yearBalanceRepository.findById(yearBalanceId).ifPresent(monthBalance::setYearBalance);
        }
    }

    private YearBalance getYearBalanceById(Long yearBalanceId) {
        return yearBalanceRepository.findById(yearBalanceId).orElseThrow(
                () -> new ResourceNotFoundException(YEAR_BALANCE_NOT_FOUND + yearBalanceId));
    }

    private MonthBalanceDTO createAndReturnDto(MonthBalance monthBalance) {
        MonthBalanceUtils.computeMontlyAmount(monthBalance);
        MonthBalance savedMonthBalance = monthBalanceRepository.save(monthBalance);
        final YearBalance yearBalance = savedMonthBalance.getYearBalance();
        log.info("Updating YearBalance " + yearBalance.getId() + " after saving MonthBalance " + savedMonthBalance
                .getId());
        yearBalance.addMonthBalance(savedMonthBalance);
        YearBalanceUtils.computeYearlyAmount(yearBalance);
        yearBalanceRepository.save(yearBalance);

        return monthBalanceMapper.toDto(savedMonthBalance);
    }
}
