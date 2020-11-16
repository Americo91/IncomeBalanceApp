package com.astoppello.incomebalanceapp.services;

import com.astoppello.incomebalanceapp.exceptions.ResourceNotFoundException;
import com.astoppello.incomebalanceapp.model.YearBalance;
import com.astoppello.incomebalanceapp.repositories.YearBalanceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * Created by @author stopp on 15/11/2020
 */
@Service
public class YearBalanceServiceImpl implements YearBalanceService {

    private final YearBalanceRepository yearBalanceRepository;

    public YearBalanceServiceImpl(YearBalanceRepository yearBalanceRepository) {
        this.yearBalanceRepository = yearBalanceRepository;
    }

    @Override
    public List<YearBalance> findAllYearBalance() {
        return yearBalanceRepository.findAll();
    }

    @Override
    public YearBalance findYearBalanceById(Long id) {
        return yearBalanceRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public YearBalance findYearBalanceByYear(int year) {
        return yearBalanceRepository.findByYear(year);
    }
}
