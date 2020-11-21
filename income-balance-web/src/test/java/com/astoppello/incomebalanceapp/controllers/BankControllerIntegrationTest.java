package com.astoppello.incomebalanceapp.controllers;

import com.astoppello.incomebalanceapp.bootstrap.Bootstrap;
import com.astoppello.incomebalanceapp.dto.domain.BankDTO;
import com.astoppello.incomebalanceapp.dto.mappers.BankMapper;
import com.astoppello.incomebalanceapp.exceptions.ResourceNotFoundException;
import com.astoppello.incomebalanceapp.model.Bank;
import com.astoppello.incomebalanceapp.repositories.BankRepository;
import com.astoppello.incomebalanceapp.repositories.YearBalanceRepository;
import com.astoppello.incomebalanceapp.services.BankService;
import com.astoppello.incomebalanceapp.services.BankServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Created by @author stopp on 21/11/2020
 */
@ExtendWith(SpringExtension.class)
@DataJpaTest
public class BankControllerIntegrationTest {
    @Autowired
    YearBalanceRepository yearBalanceRepository;
    @Autowired
    BankRepository bankRepository;
    BankService bankService;

    @BeforeEach
    void setUp() throws Exception {
        System.out.println("Loading data: " + bankRepository.count());
        Bootstrap bootstrap = new Bootstrap(yearBalanceRepository, bankRepository);
        bootstrap.run();
        bankService = new BankServiceImpl(bankRepository, BankMapper.INSTANCE);
    }

    @Test
    void getAllBankTest() {
        assertEquals(bankRepository.count(), bankService.findAllBanks()
                                                        .size());
    }

    @Test
    void getBankById() {
        Bank bank = bankRepository.findAll().stream().findAny().orElseThrow(ResourceNotFoundException::new);
        BankDTO bankDTO = bankService.findBankById(bank.getId());
        assertNotNull(bankDTO);
        assertBankAndBankDtoAreEquals(bank, bankDTO);
    }

    @Test
    void getBankByName() {
        Bank bank = bankRepository.findAll().stream().findAny().orElseThrow(ResourceNotFoundException::new);
        BankDTO bankDTO = bankService.findBankByName(bank.getName());
        assertNotNull(bankDTO);
        assertBankAndBankDtoAreEquals(bank, bankDTO);
    }

    private void assertBankAndBankDtoAreEquals(Bank bank, BankDTO bankDTO) {
        assertEquals(bank.getId(), bankDTO.getId());
        assertEquals(bank.getName(), bankDTO.getName());
    }
}
