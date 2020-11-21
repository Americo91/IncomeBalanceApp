package com.astoppello.incomebalanceapp.services;

import com.astoppello.incomebalanceapp.dto.domain.BankDTO;
import com.astoppello.incomebalanceapp.dto.mappers.BankMapper;
import com.astoppello.incomebalanceapp.exceptions.ResourceNotFoundException;
import com.astoppello.incomebalanceapp.model.Bank;
import com.astoppello.incomebalanceapp.repositories.BankRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by @author stopp on 21/11/2020
 */
@Service
public class BankServiceImpl implements BankService {

    private final BankRepository bankRepository;
    private final BankMapper bankMapper;

    public BankServiceImpl(BankRepository bankRepository, BankMapper bankMapper) {
        this.bankRepository = bankRepository;
        this.bankMapper = bankMapper;
    }

    @Override
    public List<BankDTO> findAllBanks() {
        return bankRepository.findAll()
                             .stream()
                             .map(bankMapper::bankToBankDto)
                             .collect(Collectors.toList());
    }

    @Override
    public BankDTO findBankById(Long id) {
        return bankRepository.findById(id)
                             .map(bankMapper::bankToBankDto)
                             .orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public BankDTO findBankByName(String name) {
        Bank bank = bankRepository.findBankByName(name);
        if (bank == null) {
            throw new ResourceNotFoundException("Bank not found. Name: " + name);
        }
        return bankMapper.bankToBankDto(bank);
    }
}
