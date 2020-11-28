package com.astoppello.incomebalanceapp.controllers;

import com.astoppello.incomebalanceapp.bootstrap.Bootstrap;
import com.astoppello.incomebalanceapp.dto.domain.BankBalanceDTO;
import com.astoppello.incomebalanceapp.dto.domain.BankDTO;
import com.astoppello.incomebalanceapp.dto.domain.YearBalanceDTO;
import com.astoppello.incomebalanceapp.dto.mappers.BankBalanceMapper;
import com.astoppello.incomebalanceapp.dto.mappers.BankMapper;
import com.astoppello.incomebalanceapp.dto.mappers.YearBalanceMapper;
import com.astoppello.incomebalanceapp.exceptions.ResourceNotFoundException;
import com.astoppello.incomebalanceapp.model.Bank;
import com.astoppello.incomebalanceapp.model.BankBalance;
import com.astoppello.incomebalanceapp.model.YearBalance;
import com.astoppello.incomebalanceapp.repositories.BankBalanceRepository;
import com.astoppello.incomebalanceapp.repositories.BankRepository;
import com.astoppello.incomebalanceapp.repositories.YearBalanceRepository;
import com.astoppello.incomebalanceapp.services.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Created by @author stopp on 28/11/2020
 */

@ExtendWith(SpringExtension.class)
@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ControllerIntegrationTest {
    @Autowired
    YearBalanceRepository yearBalanceRepository;
    @Autowired
    BankRepository bankRepository;
    @Autowired
    BankBalanceRepository bankBalanceRepository;
    BankBalanceService bankBalanceService;
    private YearBalanceService yearBalanceService;
    private BankService bankService;

    @BeforeAll
    void setUp() throws Exception {
        System.out.println("Loading data: " + bankBalanceRepository.count());
        Bootstrap bootstrap = new Bootstrap(yearBalanceRepository, bankRepository, bankBalanceRepository);
        bootstrap.run();
        bankBalanceService = new BankBalanceServiceImpl(bankBalanceRepository, BankBalanceMapper.INSTANCE);
        yearBalanceService = new YearBalanceServiceImpl(yearBalanceRepository, YearBalanceMapper.INSTANCE);
        bankService = new BankServiceImpl(bankRepository, BankMapper.INSTANCE);
    }

    @Test
    void getAllBankBalancesTest() {
        assertEquals(bankBalanceRepository.count(), bankBalanceService.findAll().size());
    }

    @Test
    void getBankBalanceByIdTest() {
        BankBalance bankBalance =
                bankBalanceRepository.findAll().stream().findAny().orElseThrow(ResourceNotFoundException::new);
        BankBalanceDTO bankBalanceDTO = bankBalanceService.findById(bankBalance.getId());
        assertNotNull(bankBalanceDTO);
        assertBankBalanceAndDtoAreEqual(bankBalance, bankBalanceDTO);
    }

    @Test
    void getAllBankTest() {
        assertEquals(bankRepository.count(), bankService.findAll()
                                                        .size());
    }

    @Test
    void getBankById() {
        Bank bank = bankRepository.findAll().stream().findAny().orElseThrow(ResourceNotFoundException::new);
        BankDTO bankDTO = bankService.findById(bank.getId());
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

    @Test
    void getAllYearBalanceTest() {
        List<YearBalanceDTO> yearBalanceList = yearBalanceService.findAll();
        assertEquals(3, yearBalanceList.size());
    }

    @Test
    void getYearBalanceByIdTest() {
        YearBalance yearBalance = yearBalanceRepository.findAll()
                                                       .stream()
                                                       .findAny()
                                                       .orElseThrow(ResourceNotFoundException::new);
        YearBalanceDTO yearBalanceDTO = yearBalanceService.findById(yearBalance.getId());
        assertNotNull(yearBalanceDTO);
        assertYearBalanceAndYearBalanceDtoAreEquals(yearBalance, yearBalanceDTO);
    }


    @Test
    void getYearBalanceByYearTest() {
        YearBalance balance =
                yearBalanceRepository.findAll()
                                     .stream()
                                     .filter(y -> Objects.nonNull(y.getYear()))
                                     .findAny()
                                     .orElseThrow(ResourceNotFoundException::new);
        YearBalanceDTO yearBalanceDTO = yearBalanceService.findYearBalanceByYear(balance.getYear());
        assertNotNull(yearBalanceDTO);
        assertYearBalanceAndYearBalanceDtoAreEquals(balance, yearBalanceDTO);
    }

    private void assertYearBalanceAndYearBalanceDtoAreEquals(YearBalance yearBalance, YearBalanceDTO yearBalanceDTO) {
        assertEquals(yearBalance.getId(), yearBalanceDTO.getId());
        assertEquals(yearBalance.getYear(), yearBalanceDTO.getYear());
        assertEquals(yearBalance.getExpenses(),yearBalanceDTO.getExpenses());
        assertEquals(yearBalance.getIncomes(), yearBalanceDTO.getIncomes());
        assertEquals(yearBalance.getSalary(),yearBalanceDTO.getSalary());
        assertEquals(yearBalance.getResult(), yearBalanceDTO.getResult());
    }

    private void assertBankAndBankDtoAreEquals(Bank bank, BankDTO bankDTO) {
        assertEquals(bank.getId(), bankDTO.getId());
        assertEquals(bank.getName(), bankDTO.getName());
    }

    private void assertBankBalanceAndDtoAreEqual(BankBalance bankBalance, BankBalanceDTO bankBalanceDTO) {
        assertEquals(bankBalance.getId(), bankBalanceDTO.getId());
        assertEquals(bankBalance.getBank().getId(), bankBalanceDTO.getBank().getId());
        assertEquals(bankBalance.getBank().getName(), bankBalanceDTO.getBank().getName());
        assertEquals(bankBalance.getExpenses(), bankBalanceDTO.getExpenses());
        assertEquals(bankBalance.getIncomes(), bankBalanceDTO.getIncomes());
        assertEquals(bankBalance.getResult(), bankBalanceDTO.getResult());
        assertEquals(bankBalance.getSalary(), bankBalanceDTO.getSalary());
    }
}
