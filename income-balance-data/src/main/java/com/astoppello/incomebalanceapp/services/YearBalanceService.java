package com.astoppello.incomebalanceapp.services;

import com.astoppello.incomebalanceapp.dto.domain.YearBalanceDTO;
import com.astoppello.incomebalanceapp.model.YearBalance;

import java.util.List;

/**
 * Created by @author stopp on 15/11/2020
 */
public interface YearBalanceService {

    List<YearBalanceDTO> findAll();
    YearBalanceDTO findById(Long id);
    YearBalanceDTO findYearBalanceByYear(int year);
}
