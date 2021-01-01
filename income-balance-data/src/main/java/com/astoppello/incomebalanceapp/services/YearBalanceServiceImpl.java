package com.astoppello.incomebalanceapp.services;

import com.astoppello.incomebalanceapp.dto.domain.YearBalanceDTO;
import com.astoppello.incomebalanceapp.dto.mappers.YearBalanceMapper;
import com.astoppello.incomebalanceapp.exceptions.ResourceNotFoundException;
import com.astoppello.incomebalanceapp.model.YearBalance;
import com.astoppello.incomebalanceapp.repositories.YearBalanceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by @author stopp on 15/11/2020
 */
@Service
public class YearBalanceServiceImpl implements YearBalanceService {

    private final YearBalanceRepository repository;
    private final YearBalanceMapper mapper;

    public YearBalanceServiceImpl(YearBalanceRepository repository, YearBalanceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<YearBalanceDTO> findAll() {
        return repository.findAll()
                         .stream()
                         .map(mapper::yearBalanceToYearBalanceDto)
                         .collect(Collectors.toList());
    }

    @Override
    public YearBalanceDTO findById(Long id) {
        return repository.findById(id)
                         .map(mapper::yearBalanceToYearBalanceDto)
                         .orElseThrow(() -> new ResourceNotFoundException(YEAR_BALANCE_NOT_FOUND+id));
    }

    @Override
    public YearBalanceDTO findYearBalanceByYear(int year) {
        YearBalance yearBalance = repository.findByYear(year);
        if (yearBalance == null) {
            throw new ResourceNotFoundException(YEAR_BALANCE_NOT_FOUND + year);
        }
        return mapper.yearBalanceToYearBalanceDto(yearBalance);
    }
}
