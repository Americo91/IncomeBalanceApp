package com.astoppello.incomebalanceapp.services;

import com.astoppello.incomebalanceapp.model.YearBalance;

import java.util.List;

/**
 * Created by @author stopp on 15/11/2020
 */
public interface YearBalanceService {

    List<YearBalance> findAllYearBalance();

    YearBalance findYearBalanceById(Long id);

    YearBalance findYearBalanceByYear(int year);
}
