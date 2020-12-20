package com.astoppello.incomebalanceapp.services;

import com.astoppello.incomebalanceapp.dto.domain.YearBalanceDTO;

import java.util.List;

/**
 * Created by @author stopp on 15/11/2020
 */
public interface YearBalanceService extends CrudService<YearBalanceDTO, Long>{

    YearBalanceDTO findYearBalanceByYear(int year);
}
