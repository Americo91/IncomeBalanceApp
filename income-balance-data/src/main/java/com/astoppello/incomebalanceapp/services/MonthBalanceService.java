package com.astoppello.incomebalanceapp.services;

import com.astoppello.incomebalanceapp.dto.domain.MonthBalanceDTO;

import java.util.List;

/**
 * Created by @author stopp on 20/12/2020
 */
public interface MonthBalanceService {

    List<MonthBalanceDTO> findAll();
    MonthBalanceDTO findById(Long id);
    MonthBalanceDTO findMonthBalanceByYearBalanceIdAndId(Long yearBalanceId, Long monthBalanceId);
    MonthBalanceDTO findByMonth(String month);
}
