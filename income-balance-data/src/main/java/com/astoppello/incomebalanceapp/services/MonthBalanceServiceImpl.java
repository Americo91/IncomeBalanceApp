package com.astoppello.incomebalanceapp.services;

import com.astoppello.incomebalanceapp.dto.domain.MonthBalanceDTO;
import com.astoppello.incomebalanceapp.dto.mappers.MonthBalanceMapper;
import com.astoppello.incomebalanceapp.exceptions.ResourceNotFoundException;
import com.astoppello.incomebalanceapp.model.MonthBalance;
import com.astoppello.incomebalanceapp.model.YearBalance;
import com.astoppello.incomebalanceapp.repositories.MonthBalanceRepository;
import com.astoppello.incomebalanceapp.repositories.YearBalanceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by @author stopp on 20/12/2020
 */
@Service
public class MonthBalanceServiceImpl implements MonthBalanceService {

    public static final String MONTH_BALANCE_NOT_FOUND = "MonthBalace not found:";
    private final MonthBalanceRepository monthBalanceRepository;
    private final MonthBalanceMapper monthBalanceMapper;
    private final YearBalanceRepository yearBalanceRepository;

    public MonthBalanceServiceImpl(MonthBalanceRepository monthBalanceRepository, MonthBalanceMapper monthBalanceMapper,
                                   YearBalanceRepository yearBalanceRepository) {
        this.monthBalanceRepository = monthBalanceRepository;
        this.monthBalanceMapper = monthBalanceMapper;
        this.yearBalanceRepository = yearBalanceRepository;
    }

    @Override
    public MonthBalanceDTO findByMonth(String month) {
        MonthBalance monthBalance = monthBalanceRepository.findByMonth(month);
        if (monthBalance == null) {
            throw new ResourceNotFoundException(MONTH_BALANCE_NOT_FOUND + month);
        }
        return monthBalanceMapper.monthBalanceToMonthBalanceDto(monthBalance);
    }

    @Override
    public List<MonthBalanceDTO> findAll() {
        return monthBalanceRepository.findAll()
                                     .stream()
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
    public MonthBalanceDTO findMonthBalanceByYearBalanceIdAndId(Long yearBalanceId, Long monthBalanceId) {
        YearBalance yearBalance = yearBalanceRepository
                .findById(yearBalanceId)
                .orElseThrow(() -> new ResourceNotFoundException(YearBalanceServiceImpl.YEAR_BALANCE_NOT_FOUND + yearBalanceId));

        return yearBalance
                .getMonthBalanceList().stream()
                .filter(monthBalance -> monthBalanceId.equals(monthBalance.getId()))
                .findFirst()
                .map(monthBalanceMapper::monthBalanceToMonthBalanceDto)
                .orElseThrow(() -> new ResourceNotFoundException(MONTH_BALANCE_NOT_FOUND + monthBalanceId));
    }
}
