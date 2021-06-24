package com.astoppello.incomebalanceapp.services;

import com.astoppello.incomebalanceapp.dto.domain.MonthBalanceDTO;

import java.util.List;

/** Created by @author stopp on 20/12/2020 */
public interface MonthBalanceService {

  String MONTH_BALANCE_NOT_FOUND = "MonthBalace not found:";

  List<MonthBalanceDTO> findAllById(Long yearBalanceId);

  List<MonthBalanceDTO> findByMonth(String month);

  MonthBalanceDTO createNewMonthBalanceById(Long yearBalanceId, MonthBalanceDTO monthBalanceDTO);

  List<MonthBalanceDTO> findAll();

  MonthBalanceDTO findById(Long id);

  MonthBalanceDTO saveMonthBalance(Long id, MonthBalanceDTO monthBalanceDTO);

  MonthBalanceDTO updateMonthBalance(Long monthBalanceId, MonthBalanceDTO monthBalanceDTO);

  MonthBalanceDTO delete(Long monthBalanceId);
}
