package com.astoppello.incomebalanceapp.services;

import com.astoppello.incomebalanceapp.dto.domain.BankDTO;

import java.util.List;

/**
 * Created by @author stopp on 21/11/2020
 */
public interface BankService {

    List<BankDTO> findAllBanks();

    BankDTO findBankById(Long id);

    BankDTO findBankByName(String name);
}
