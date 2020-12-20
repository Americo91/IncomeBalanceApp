package com.astoppello.incomebalanceapp.services;

import com.astoppello.incomebalanceapp.dto.domain.BankDTO;
import com.astoppello.incomebalanceapp.model.Bank;

import java.util.List;

/**
 * Created by @author stopp on 21/11/2020
 */
public interface BankService extends CrudService<BankDTO, Long>{

    BankDTO findBankByName(String name);
}
