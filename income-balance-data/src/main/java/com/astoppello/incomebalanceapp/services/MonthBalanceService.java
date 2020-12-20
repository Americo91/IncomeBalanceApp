package com.astoppello.incomebalanceapp.services;

import com.astoppello.incomebalanceapp.dto.domain.MonthBalanceDTO;

/**
 * Created by @author stopp on 20/12/2020
 */
public interface MonthBalanceService extends CrudService<MonthBalanceDTO, Long> {

    MonthBalanceDTO findByMonth(String month);
}
