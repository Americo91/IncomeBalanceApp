package com.astoppello.incomebalanceapp.services;

import com.astoppello.incomebalanceapp.dto.domain.BankDTO;

import java.util.List;

/** Created by @author stopp on 21/11/2020 */
public interface BankService {

  List<BankDTO> findAll();

  BankDTO findById(Long id);

  BankDTO findBankByName(String name);

  BankDTO createNewBank(BankDTO bankDTO);

  BankDTO saveBank(Long id, BankDTO bankDTO);

  BankDTO updateBank(Long id, BankDTO bankDTO);

  BankDTO deleteBank(Long id);
}
