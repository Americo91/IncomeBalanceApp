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

    private final YearBalanceRepository yearBalanceRepository;
    private final YearBalanceMapper yearBalanceMapper;

    public YearBalanceServiceImpl(YearBalanceRepository yearBalanceRepository, YearBalanceMapper yearBalanceMapper) {
        this.yearBalanceRepository = yearBalanceRepository;
        this.yearBalanceMapper = yearBalanceMapper;
    }

    @Override
    public List<YearBalanceDTO> findAllYearBalance() {
        return yearBalanceRepository.findAll()
                                    .stream()
                                    .map(yearBalanceMapper::yearBalanceToYearBalanceDto)
                                    .collect(Collectors.toList());
    }

    @Override
    public YearBalanceDTO findYearBalanceById(Long id) {
        return yearBalanceRepository.findById(id)
                                    .map(yearBalanceMapper::yearBalanceToYearBalanceDto)
                                    .orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public YearBalanceDTO findYearBalanceByYear(int year) {
        YearBalance yearBalance = yearBalanceRepository.findByYear(year);
        if (yearBalance == null) {
            throw new ResourceNotFoundException("YearBalance not found. Year: " + year);
        }
        return yearBalanceMapper.yearBalanceToYearBalanceDto(yearBalance);
    }
}
