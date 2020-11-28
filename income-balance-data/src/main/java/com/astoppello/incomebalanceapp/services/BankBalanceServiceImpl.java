package com.astoppello.incomebalanceapp.services;

import com.astoppello.incomebalanceapp.dto.domain.BankBalanceDTO;
import com.astoppello.incomebalanceapp.dto.mappers.BankBalanceMapper;
import com.astoppello.incomebalanceapp.exceptions.ResourceNotFoundException;
import com.astoppello.incomebalanceapp.model.Bank;
import com.astoppello.incomebalanceapp.repositories.BankBalanceRepository;
import com.astoppello.incomebalanceapp.repositories.BankRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by @author stopp on 28/11/2020
 */
@Service
public class BankBalanceServiceImpl implements BankBalanceService {

    private final BankBalanceRepository repository;
    private final BankBalanceMapper mapper;

    public BankBalanceServiceImpl(BankBalanceRepository repository, BankBalanceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<BankBalanceDTO> findAll() {
        return repository.findAll()
                         .stream()
                         .map(mapper::bankBalanceToBankBalanceDTO)
                         .collect(Collectors.toList());
    }

    @Override
    public BankBalanceDTO findById(Long id) {
        return repository.findById(id)
                         .map(mapper::bankBalanceToBankBalanceDTO)
                         .orElseThrow(ResourceNotFoundException::new);
    }
}
