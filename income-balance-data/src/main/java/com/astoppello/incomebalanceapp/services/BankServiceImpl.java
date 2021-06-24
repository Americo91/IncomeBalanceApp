package com.astoppello.incomebalanceapp.services;

import com.astoppello.incomebalanceapp.dto.domain.BankDTO;
import com.astoppello.incomebalanceapp.dto.mappers.BankMapper;
import com.astoppello.incomebalanceapp.exceptions.ResourceNotFoundException;
import com.astoppello.incomebalanceapp.model.Bank;
import com.astoppello.incomebalanceapp.repositories.BankRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/** Created by @author stopp on 21/11/2020 */
@Slf4j
@Service
public class BankServiceImpl implements BankService {

  public static final String BANK_NOT_FOUND = "Bank not found. ";
  private final BankRepository repository;
  private final BankMapper bankMapper;

  public BankServiceImpl(BankRepository repository, BankMapper bankMapper) {
    this.repository = repository;
    this.bankMapper = bankMapper;
  }

  @Override
  public List<BankDTO> findAll() {
    log.info("Get all Banks");
    return repository.findAll().stream()
        .map(bankMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  public BankDTO findById(Long id) {
    log.info("Get Bank by id: " + id);
    return repository
        .findById(id)
        .map(bankMapper::toDto)
        .orElseThrow(ResourceNotFoundException::new);
  }

  @Override
  public BankDTO findBankByName(String name) {
    log.info("Get bank by name:" + name);
    Bank bank = repository.findBankByName(name);
    if (bank == null) {
      throw new ResourceNotFoundException(BANK_NOT_FOUND + name);
    }
    return bankMapper.toDto(bank);
  }

  @Override
  public BankDTO createNewBank(BankDTO bankDTO) {
    log.info("Create new bank " + bankDTO.toString());
    return saveAndReturnDto(bankMapper.toEntity(bankDTO));
  }

  @Override
  public BankDTO saveBank(Long id, BankDTO bankDTO) {
    log.info("Put bank with id:" +id+". Bank: " + bankDTO.toString());
    Bank bank = bankMapper.toEntity(bankDTO);
    bank.setId(id);
    return saveAndReturnDto(bank);
  }

  @Override
  public BankDTO updateBank(Long id, BankDTO bankDTO) {
    log.info("Update bank by id: " + id + ". Bank: " + bankDTO.toString());
    return repository
        .findById(id)
        .map(
            bank -> {
              if (StringUtils.isNotBlank(bankDTO.getName())) {
                bank.setName(bankDTO.getName());
              }
              return saveAndReturnDto(bank);
            })
        .orElseThrow(() -> new ResourceNotFoundException(BANK_NOT_FOUND + "id"));
  }

  @Override
  public BankDTO deleteBank(Long id) {
    Bank bank = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(BANK_NOT_FOUND + "id"));
    log.info("Delete bank id: " + id);
    repository.deleteById(id);
    return bankMapper.toDto(bank);
  }

  private BankDTO saveAndReturnDto(Bank bank) {
    Bank savedBank = repository.save(bank);
    return bankMapper.toDto(savedBank);
  }
}
