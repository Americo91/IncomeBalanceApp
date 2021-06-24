package com.astoppello.incomebalanceapp.services;

import com.astoppello.incomebalanceapp.dto.domain.YearBalanceDTO;

import java.util.List;

/** Created by @author stopp on 15/11/2020 */
public interface YearBalanceService {

  String YEAR_BALANCE_NOT_FOUND = "YearBalance not found: ";

  List<YearBalanceDTO> findAll();

  YearBalanceDTO findById(Long id);

  YearBalanceDTO findYearBalanceByYear(int year);

  YearBalanceDTO createNewYearBalance(YearBalanceDTO yearBalanceDTO);

  YearBalanceDTO saveYearBalance(Long id, YearBalanceDTO yearBalanceDTO);

  YearBalanceDTO updateYearBalance(Long id, YearBalanceDTO yearBalanceDTO);

  YearBalanceDTO deleteYearBalance(Long id);
}
