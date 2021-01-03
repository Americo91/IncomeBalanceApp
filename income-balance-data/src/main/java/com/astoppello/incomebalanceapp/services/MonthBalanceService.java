package com.astoppello.incomebalanceapp.services;

import com.astoppello.incomebalanceapp.dto.domain.MonthBalanceDTO;

import java.util.List;

/** Created by @author stopp on 20/12/2020 */
public interface MonthBalanceService {

  String MONTH_BALANCE_NOT_FOUND = "MonthBalace not found:";

  List<MonthBalanceDTO> findAll(Long yearBalanceId);

  MonthBalanceDTO findById(Long yearBalanceId, Long monthBalanceId);

  //MonthBalanceDTO findByMonth(Long yearBalanceId, String month);

  MonthBalanceDTO createNewMonthBalance(Long yearBalanceId, MonthBalanceDTO monthBalanceDTO);
}
