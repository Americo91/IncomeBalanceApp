package com.astoppello.incomebalanceapp.services;

import com.astoppello.incomebalanceapp.dto.domain.BankBalanceDTO;

import java.util.List;

/** Created by @author stopp on 28/11/2020 */
public interface BankBalanceService {

  String BANK_BALANCE_NOT_FOUND = "BankBalance not found: ";

  List<BankBalanceDTO> findAllById(Long yearBalanceId, Long monthBalanceId);

  BankBalanceDTO createNewBankBalanceById(long yearBalanceId, long monthBalanceId, BankBalanceDTO bankBalanceDTO);

  List<BankBalanceDTO> findAll();

  BankBalanceDTO findById(Long bankBalanceId);

  List<BankBalanceDTO> findByBankName(String bankName);

  BankBalanceDTO createNewBankBalance(BankBalanceDTO bankBalanceDTO);

  BankBalanceDTO saveBankBalance(Long bankBalanceId, BankBalanceDTO bankBalanceDTO);

  BankBalanceDTO updateBankBalance(Long bankBalanceId, BankBalanceDTO bankBalanceDTO);

  void deleteBankBalance(Long bankBalanceId);
}
