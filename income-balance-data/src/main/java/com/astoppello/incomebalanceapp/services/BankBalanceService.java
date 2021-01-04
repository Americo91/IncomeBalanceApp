package com.astoppello.incomebalanceapp.services;

import com.astoppello.incomebalanceapp.dto.domain.BankBalanceDTO;

import java.util.List;

/** Created by @author stopp on 28/11/2020 */
public interface BankBalanceService {

  String BANK_BALANCE_NOT_FOUND = "BankBalance not found: ";

  List<BankBalanceDTO> findAll(Long yearBalanceId, Long monthBalanceId);

  BankBalanceDTO findById(Long bankBalanceId);

  BankBalanceDTO findByBankName(Long yearBalanceId, Long monthBalanceId, String bankName);

  BankBalanceDTO createNewBankBalance(BankBalanceDTO bankBalanceDTO);
}
