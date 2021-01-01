package com.astoppello.incomebalanceapp.controllers;

import com.astoppello.incomebalanceapp.bootstrap.Bootstrap;
import com.astoppello.incomebalanceapp.dto.domain.BankBalanceDTO;
import com.astoppello.incomebalanceapp.dto.domain.BankDTO;
import com.astoppello.incomebalanceapp.dto.domain.MonthBalanceDTO;
import com.astoppello.incomebalanceapp.dto.domain.YearBalanceDTO;
import com.astoppello.incomebalanceapp.dto.mappers.*;
import com.astoppello.incomebalanceapp.exceptions.ResourceNotFoundException;
import com.astoppello.incomebalanceapp.model.Bank;
import com.astoppello.incomebalanceapp.model.BankBalance;
import com.astoppello.incomebalanceapp.model.MonthBalance;
import com.astoppello.incomebalanceapp.model.YearBalance;
import com.astoppello.incomebalanceapp.repositories.BankBalanceRepository;
import com.astoppello.incomebalanceapp.repositories.BankRepository;
import com.astoppello.incomebalanceapp.repositories.MonthBalanceRepository;
import com.astoppello.incomebalanceapp.repositories.YearBalanceRepository;
import com.astoppello.incomebalanceapp.services.*;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Objects;

import static com.astoppello.incomebalanceapp.utils.ModelEqualUtils.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by @author stopp on 28/11/2020
 */

@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Import({BankMapperImpl.class, BankBalanceMapperImpl.class, MonthBalanceMapperImpl.class, YearBalanceMapperImpl.class})
@DataJpaTest
public class ControllerIntegrationTest {
    @Autowired
    YearBalanceRepository yearBalanceRepository;
    @Autowired
    BankRepository bankRepository;
    @Autowired
    BankBalanceRepository bankBalanceRepository;
    @Autowired
    MonthBalanceRepository monthBalanceRepository;
    BankBalanceService bankBalanceService;
    private YearBalanceService yearBalanceService;
    private BankService bankService;
    private MonthBalanceService monthBalanceService;
    @Autowired
    BankMapper bankMapper;
    @Autowired
    BankBalanceMapper bankBalanceMapper;
    @Autowired
    MonthBalanceMapper monthBalanceMapper;
    @Autowired
    YearBalanceMapper yearBalanceMapper;

    @BeforeAll
    void setUp() throws Exception {
        System.out.println("Loading data: " + bankBalanceRepository.count());
        Bootstrap bootstrap = new Bootstrap(
                yearBalanceRepository,
                bankRepository,
                bankBalanceRepository,
                monthBalanceRepository);
        bootstrap.run();
        bankBalanceService = new BankBalanceServiceImpl(bankBalanceRepository, bankBalanceMapper);
        yearBalanceService = new YearBalanceServiceImpl(yearBalanceRepository, yearBalanceMapper);
        bankService = new BankServiceImpl(bankRepository, bankMapper);
        monthBalanceService = new MonthBalanceServiceImpl(
                monthBalanceRepository,
                monthBalanceMapper,
                yearBalanceRepository);
    }

    @Test
    void getAllBankBalancesTest() {
        assertEquals(bankBalanceRepository.count(), bankBalanceService.findAll().size());
    }

    @Test
    void getBankBalanceByIdTest() {
        BankBalance bankBalance = bankBalanceRepository.findAll()
                                                       .stream()
                                                       .findAny()
                                                       .orElseThrow(ResourceNotFoundException::new);
        BankBalanceDTO bankBalanceDTO = bankBalanceService.findById(bankBalance.getId());
        assertNotNull(bankBalanceDTO);
        assertBankBalanceAndDtoAreEqual(bankBalance, bankBalanceDTO);
    }

    @Test
    void getAllBankTest() {
        assertEquals(bankRepository.count(), bankService.findAll().size());
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
        YearBalance balance = yearBalanceRepository.findAll()
                                                   .stream()
                                                   .filter(y -> Objects.nonNull(y.getYear()))
                                                   .findAny()
                                                   .orElseThrow(ResourceNotFoundException::new);
        YearBalanceDTO yearBalanceDTO = yearBalanceService.findYearBalanceByYear(balance.getYear());
        assertNotNull(yearBalanceDTO);
        assertYearBalanceAndYearBalanceDtoAreEquals(balance, yearBalanceDTO);
    }

    @Test
    void getAllMonthBalanceTest() {
        assertEquals(3, monthBalanceRepository.findAll().size());
    }

    @Test
    void getMonthBalanceById() {
        MonthBalance monthBalance = monthBalanceRepository.findAll()
                                                          .stream()
                                                          .findAny()
                                                          .orElseThrow(ResourceNotFoundException::new);
        MonthBalanceDTO monthBalanceDTO = monthBalanceService.findById(monthBalance.getId());
        assertNotNull(monthBalanceDTO);
        assertMonthBalanceAndDtoAreEqual(monthBalance, monthBalanceDTO);
    }

    @Test
    void getMonthBalanceByMonth() {
        MonthBalance monthBalance = monthBalanceRepository.findAll()
                                                          .stream()
                                                          .findAny()
                                                          .orElseThrow(ResourceNotFoundException::new);
        MonthBalanceDTO monthBalanceDTO = monthBalanceService.findByMonth(monthBalance.getMonth());
        assertNotNull(monthBalanceDTO);
        assertMonthBalanceAndDtoAreEqual(monthBalance, monthBalanceDTO);
    }

    @Test
    void getMonthBalanceByYearBalanceIdAndId() {
        MonthBalanceDTO monthBalanceDTO = monthBalanceService.findMonthBalanceByYearBalanceIdAndId(3L, 3L);
        YearBalance yearBalance= yearBalanceRepository.findById(3L).get();
        assertNotNull(monthBalanceDTO);
        assertNotNull(yearBalance);
        assertFalse(CollectionUtils.isEmpty(yearBalance.getMonthBalanceList()));
        assertNotNull(yearBalance.getMonthBalanceList().get(0));
        assertMonthBalanceAndDtoAreEqual(yearBalance.getMonthBalanceList().get(0), monthBalanceDTO);
    }

}
