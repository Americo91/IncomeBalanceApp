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

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import static com.astoppello.incomebalanceapp.utils.ModelEqualUtils.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by @author stopp on 28/11/2020
 */
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Import({
        BankMapperImpl.class,
        BankBalanceMapperImpl.class,
        MonthBalanceMapperImpl.class,
        YearBalanceMapperImpl.class
})
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
    @Autowired
    BankMapper bankMapper;
    @Autowired
    BankBalanceMapper bankBalanceMapper;
    @Autowired
    MonthBalanceMapper monthBalanceMapper;
    @Autowired
    YearBalanceMapper yearBalanceMapper;
    private YearBalanceService yearBalanceService;
    private BankService bankService;
    private MonthBalanceService monthBalanceService;

    @BeforeAll
    void setUp() throws Exception {
        System.out.println("Loading data: " + bankBalanceRepository.count());
        Bootstrap bootstrap =
                new Bootstrap(
                        yearBalanceRepository, bankRepository, bankBalanceRepository, monthBalanceRepository);
        bootstrap.run();
        bankBalanceService =
                new BankBalanceServiceImpl(
                        bankBalanceRepository,
                        bankBalanceMapper,
                        monthBalanceRepository,
                        yearBalanceRepository);
        yearBalanceService =
                new YearBalanceServiceImpl(
                        yearBalanceRepository, yearBalanceMapper, monthBalanceMapper, bankBalanceMapper);
        bankService = new BankServiceImpl(bankRepository, bankMapper);
        monthBalanceService =
                new MonthBalanceServiceImpl(
                        monthBalanceRepository, monthBalanceMapper, yearBalanceRepository, bankBalanceMapper);
    }

    // YearBalance Tests

    @Test
    void getAllYearBalanceTest() {
        List<YearBalanceDTO> yearBalanceList = yearBalanceService.findAll();
        assertEquals(3, yearBalanceList.size());
    }

    @Test
    void getYearBalanceByIdTest() {
        YearBalance yearBalance =
                yearBalanceRepository.findAll().stream()
                                     .findAny()
                                     .orElseThrow(ResourceNotFoundException::new);
        YearBalanceDTO yearBalanceDTO = yearBalanceService.findById(yearBalance.getId());
        assertNotNull(yearBalanceDTO);
        assertYearBalanceAndYearBalanceDtoAreEquals(yearBalance, yearBalanceDTO);
    }

    @Test
    void getYearBalanceByYearTest() {
        YearBalance balance =
                yearBalanceRepository.findAll().stream()
                                     .filter(y -> Objects.nonNull(y.getYear()))
                                     .findAny()
                                     .orElseThrow(ResourceNotFoundException::new);
        YearBalanceDTO yearBalanceDTO = yearBalanceService.findYearBalanceByYear(balance.getYear());
        assertNotNull(yearBalanceDTO);
        assertYearBalanceAndYearBalanceDtoAreEquals(balance, yearBalanceDTO);
    }

    @Test
    void createNewYearBalance() {
        YearBalanceDTO yearBalanceDTO = new YearBalanceDTO();
        yearBalanceDTO.setYear(2021);
        YearBalanceDTO savedYearBalanceDto = yearBalanceService.createNewYearBalance(yearBalanceDTO);
        assertNotNull(savedYearBalanceDto);
        assertNotNull(savedYearBalanceDto.getId());
        assertEquals(savedYearBalanceDto.getYear(), 2021);
    }

    @Test
    void createOneShot() {
        YearBalanceDTO yearBalanceDTO = new YearBalanceDTO();
        yearBalanceDTO.setYear(2022);
        BankBalanceDTO bankBalanceDTO = new BankBalanceDTO();
        BigDecimal expenses = new BigDecimal(200);
        bankBalanceDTO.setExpenses(expenses);
        MonthBalanceDTO monthBalanceDTO = new MonthBalanceDTO();
        String september = "September";
        monthBalanceDTO.setMonth(september);
        BigDecimal salary = new BigDecimal(1000);
        monthBalanceDTO.setSalary(salary);
        yearBalanceDTO.setMonthBalances(List.of(monthBalanceDTO));
        yearBalanceDTO.setBankBalances(List.of(bankBalanceDTO));
        YearBalanceDTO savedDto = yearBalanceService.createNewYearBalance(yearBalanceDTO);
        assertNotNull(savedDto);
        assertNotNull(savedDto.getId());
        assertTrue(CollectionUtils.isNotEmpty(savedDto.getBankBalances()));
        assertTrue(CollectionUtils.isNotEmpty(savedDto.getMonthBalances()));
        assertEquals(expenses, savedDto.getExpenses());
        assertEquals(salary, savedDto.getSalary());
        assertEquals(savedDto.getId(), savedDto.getBankBalances().get(0).getYearBalanceId());
        assertEquals(savedDto.getId(), savedDto.getMonthBalances().get(0).getYearBalanceId());
    }

    // MonthBalance Tests
    @Test
    void getAllMonthBalanceTest() {
        assertEquals(3, monthBalanceRepository.findAll().size());
    }

    @Test
    void getMonthBalanceById() {
        MonthBalance monthBalance =
                monthBalanceRepository.findById(3L).orElseThrow(ResourceNotFoundException::new);
        MonthBalanceDTO monthBalanceDTO =
                monthBalanceService.findMonthOfYearById(
                        monthBalance.getYearBalance().getId(), monthBalance.getId());
        assertNotNull(monthBalanceDTO);
        assertMonthBalanceAndDtoAreEqual(monthBalance, monthBalanceDTO);
    }

    @Test
    void createNewMonthBalance() {
        MonthBalanceDTO monthBalanceDTO = new MonthBalanceDTO();
        monthBalanceDTO.setMonth("September");
        MonthBalanceDTO savedMonthBalanceDto =
                monthBalanceService.createNewMonthBalanceById(1L, monthBalanceDTO);
        assertNotNull(savedMonthBalanceDto);
        assertNotNull(savedMonthBalanceDto.getId());
        assertEquals(1, savedMonthBalanceDto.getYearBalanceId());
    }

    @Test
    void getMonthBalanceByMonth() {
        MonthBalance monthBalance =
                monthBalanceRepository.findById(3L).orElseThrow(ResourceNotFoundException::new);
        MonthBalanceDTO monthBalanceDTO =
                monthBalanceService.findByMonth(monthBalance.getMonth()).get(0);
        assertNotNull(monthBalanceDTO);
        assertMonthBalanceAndDtoAreEqual(monthBalance, monthBalanceDTO);
    }

    @Test
    void saveMonthBalance() {
        MonthBalanceDTO monthBalanceDTO = new MonthBalanceDTO();
        final var september = "September";
        monthBalanceDTO.setMonth(september);
        MonthBalanceDTO savedMonthBalanceDto =
                monthBalanceService.saveMonthBalance(2L, monthBalanceDTO);
        assertNotNull(savedMonthBalanceDto);
        assertNotNull(savedMonthBalanceDto.getId());
        assertEquals(2L, savedMonthBalanceDto.getId());
        assertEquals(september, savedMonthBalanceDto.getMonth());
    }

    @Test
    void updateMonthBalance() {
        MonthBalanceDTO monthBalanceDTO = new MonthBalanceDTO();
        final var salary = new BigDecimal(200);
        monthBalanceDTO.setSalary(salary);

        MonthBalanceDTO savedMonthBalanceDto =
                monthBalanceService.updateMonthBalance(2L, monthBalanceDTO);
        assertNotNull(savedMonthBalanceDto);
        assertNotNull(savedMonthBalanceDto.getId());
        assertEquals(salary, savedMonthBalanceDto.getSalary());
    }

    @Test
    void deleteMonthBalance() {
        monthBalanceService.delete(1L);
        assertThrows(ResourceNotFoundException.class, () -> monthBalanceService.findById(1L));
    }

    // BankBalance Tests
    @Test
    void getAllBankBalancesTest() {
        assertEquals(bankBalanceRepository.count(), bankBalanceService.findAllByIds(3L, 3L).size());
    }

    @Test
    void getBankBalanceByIdTest() {
        BankBalance bankBalance = bankBalanceRepository.findById(1L).get();
        BankBalanceDTO bankBalanceDTO = bankBalanceService.findById(1L);
        assertNotNull(bankBalanceDTO);
        assertBankBalanceAndDtoAreEqual(bankBalance, bankBalanceDTO);
    }

    @Test
    void createBankBalanceTest() {
        final BigDecimal incomes = new BigDecimal(100);
        final BigDecimal expenses = new BigDecimal(50);
        BankBalanceDTO bankBalanceDTO = new BankBalanceDTO();
        bankBalanceDTO.setIncomes(incomes);
        bankBalanceDTO.setExpenses(expenses);
        BankBalanceDTO savedBankBalanceDTO = bankBalanceService.createNewBankBalance(bankBalanceDTO);
        assertNotNull(savedBankBalanceDTO);
        assertEquals(incomes, savedBankBalanceDTO.getIncomes());
        assertEquals(expenses, savedBankBalanceDTO.getExpenses());
        assertNotNull(savedBankBalanceDTO.getId());
        assertEquals(incomes.subtract(expenses), savedBankBalanceDTO.getResult());

    }

    // Bank tests
    @Test
    void getAllBankTest() {
        assertEquals(bankRepository.count(), bankService.findAll().size());
    }

    @Test
    void getBankById() {
        Bank bank =
                bankRepository.findAll().stream().findAny().orElseThrow(ResourceNotFoundException::new);
        BankDTO bankDTO = bankService.findById(bank.getId());
        assertNotNull(bankDTO);
        assertBankAndBankDtoAreEquals(bank, bankDTO);
    }

    @Test
    void getBankByName() {
        Bank bank =
                bankRepository.findAll().stream().findAny().orElseThrow(ResourceNotFoundException::new);
        BankDTO bankDTO = bankService.findBankByName(bank.getName());
        assertNotNull(bankDTO);
        assertBankAndBankDtoAreEquals(bank, bankDTO);
    }

    @Test
    void createNewBank() {
        String bankName = "Revolut";
        BankDTO bankDTO = new BankDTO();
        bankDTO.setName(bankName);
        BankDTO savedBankDto = bankService.createNewBank(bankDTO);
        assertNotNull(savedBankDto);
        assertNotNull(savedBankDto.getId());
        assertEquals(savedBankDto.getName(), bankName);
    }
}
