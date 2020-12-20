package com.astoppello.incomebalanceapp.services;

import com.astoppello.incomebalanceapp.dto.domain.MonthBalanceDTO;
import com.astoppello.incomebalanceapp.dto.mappers.MonthBalanceMapper;
import com.astoppello.incomebalanceapp.exceptions.ResourceNotFoundException;
import com.astoppello.incomebalanceapp.model.MonthBalance;
import com.astoppello.incomebalanceapp.repositories.MonthBalanceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by @author stopp on 20/12/2020
 */
@Service
public class MonthBalanceServiceImpl implements MonthBalanceService {

    private final MonthBalanceRepository monthBalanceRepository;
    private final MonthBalanceMapper monthBalanceMapper;

    public MonthBalanceServiceImpl(MonthBalanceRepository monthBalanceRepository, MonthBalanceMapper monthBalanceMapper) {
        this.monthBalanceRepository = monthBalanceRepository;
        this.monthBalanceMapper = monthBalanceMapper;
    }

    @Override
    public MonthBalanceDTO findByMonth(String month) {
        MonthBalance monthBalance = monthBalanceRepository.findByMonth(month);
        if (monthBalance == null) {
            throw new ResourceNotFoundException("MonthBalance not found with month: " + month);
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
        return monthBalanceRepository.findById(id)
                                     .map(monthBalanceMapper::monthBalanceToMonthBalanceDto)
                                     .orElseThrow(ResourceNotFoundException::new);
    }
}
