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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.Month;
import java.util.List;
import java.util.Objects;

import static com.astoppello.incomebalanceapp.utils.ModelEqualUtils.assertBankAndBankDtoAreEquals;
import static com.astoppello.incomebalanceapp.utils.ModelEqualUtils.assertMonthBalanceAndDtoAreEqual;
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
    public static final long ID = 1L;
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
                        yearBalanceRepository,
                        bankRepository);
        yearBalanceService = new YearBalanceServiceImpl(yearBalanceRepository, yearBalanceMapper);
        bankService = new BankServiceImpl(bankRepository, bankMapper);
        monthBalanceService = new MonthBalanceServiceImpl(monthBalanceRepository, monthBalanceMapper,
                yearBalanceRepository);
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
                yearBalanceRepository.findAll()
                                     .stream()
                                     .findAny()
                                     .orElseThrow(ResourceNotFoundException::new);
        YearBalanceDTO yearBalanceDTO = yearBalanceService.findById(yearBalance.getId());
        assertNotNull(yearBalanceDTO);
        assertEquals(yearBalance.getId(), yearBalanceDTO.getId());
        assertEquals(yearBalance.getYear(), yearBalanceDTO.getYear());
    }

    @Test
    void getYearBalanceByYearTest() {
        YearBalance yearBalance =
                yearBalanceRepository.findAll()
                                     .stream()
                                     .findAny()
                                     .orElseThrow(ResourceNotFoundException::new);
        YearBalanceDTO yearBalanceDTO = yearBalanceService.findYearBalanceByYear(yearBalance.getYear());
        assertNotNull(yearBalanceDTO);
        assertEquals(yearBalance.getId(), yearBalanceDTO.getId());
        assertEquals(yearBalance.getYear(), yearBalanceDTO.getYear());
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

    // MonthBalance Tests
    @Test
    void getAllMonthBalanceTest() {
        assertEquals(4, monthBalanceRepository.findAll().size());
    }

    @Test
    void getMonthBalanceById() {
        MonthBalance monthBalance =
                monthBalanceRepository.findById(3L)
                                      .orElseThrow(ResourceNotFoundException::new);
        MonthBalanceDTO monthBalanceDTO =
                monthBalanceService.findMonthOfYearById(
                        monthBalance.getYearBalance()
                                    .getId(), monthBalance.getId());
        assertNotNull(monthBalanceDTO);
        assertMonthBalanceAndDtoAreEqual(monthBalance, monthBalanceDTO);
    }

    @Test
    void createNewMonthBalance() {
        MonthBalanceDTO monthBalanceDTO = new MonthBalanceDTO();
        monthBalanceDTO.setMonth(Month.SEPTEMBER);
        MonthBalanceDTO savedMonthBalanceDto =
                monthBalanceService.createNewMonthBalanceById(ID, monthBalanceDTO);
        assertNotNull(savedMonthBalanceDto);
        assertNotNull(savedMonthBalanceDto.getId());
        assertEquals(1, savedMonthBalanceDto.getYearBalanceId());
    }

    @Test
    void getMonthBalanceByMonth() {
        MonthBalance monthBalance =
                monthBalanceRepository.findById(1L)
                                      .orElseThrow(ResourceNotFoundException::new);
        MonthBalanceDTO monthBalanceDTO =
                monthBalanceService.findByMonth(Month.SEPTEMBER.name())
                                   .get(0);
        assertNotNull(monthBalanceDTO);
        assertMonthBalanceAndDtoAreEqual(monthBalance, monthBalanceDTO);
    }

    @Test
    void saveMonthBalance() {
        MonthBalanceDTO monthBalanceDTO = new MonthBalanceDTO();
        monthBalanceDTO.setMonth(Month.SEPTEMBER);
        monthBalanceDTO.setYearBalanceId(1L);

        MonthBalanceDTO savedMonthBalanceDto =
                monthBalanceService.saveMonthBalance(2L, monthBalanceDTO);
        assertNotNull(savedMonthBalanceDto);
        assertNotNull(savedMonthBalanceDto.getId());
        assertEquals(2L, savedMonthBalanceDto.getId());
        assertEquals("SEPTEMBER", savedMonthBalanceDto.getMonth()
                                                      .name());
        assertEquals(1L, savedMonthBalanceDto.getYearBalanceId());
    }

    @Test
    void updateMonthBalance() {
        MonthBalanceDTO monthBalanceDTO = new MonthBalanceDTO();
        String salary = "200";
        monthBalanceDTO.setSalary(salary);
        monthBalanceDTO.setYearBalanceId(1L);

        MonthBalanceDTO savedMonthBalanceDto =
                monthBalanceService.updateMonthBalance(2L, monthBalanceDTO);
        assertNotNull(savedMonthBalanceDto);
        assertNotNull(savedMonthBalanceDto.getId());
        assertEquals(salary, savedMonthBalanceDto.getSalary());
        assertEquals(1L, savedMonthBalanceDto.getYearBalanceId());
    }

    @Test
    void deleteMonthBalance() {
        MonthBalance monthBalanceToBeDeleted = monthBalanceRepository.findById(ID)
                                                                     .get();
        monthBalanceService.delete(ID);
        assertThrows(ResourceNotFoundException.class, () -> monthBalanceService.findById(ID));
        assertNotNull(monthBalanceToBeDeleted);
        assertNotNull(monthBalanceToBeDeleted.getYearBalance());
        Long yearBalanceUpdated = monthBalanceToBeDeleted.getYearBalance()
                                                         .getId();
        assertEquals(
                BigDecimal.ZERO,
                yearBalanceRepository.findById(yearBalanceUpdated)
                                     .map(YearBalance::getSalary)
                                     .get());
    }

    // BankBalance Tests
    @Test
    void getAllBankBalancesTest() {
        assertEquals(bankBalanceRepository.count(), bankBalanceService.findAllByIds(3L, 3L)
                                                                      .size());
    }

    @Test
    void getBankBalanceByIdTest() {
        BankBalance bankBalance = bankBalanceRepository.findById(ID)
                                                       .get();
        BankBalanceDTO bankBalanceDTO = bankBalanceService.findById(ID);
        assertNotNull(bankBalanceDTO);
        assertEquals(bankBalance.getId(), bankBalanceDTO.getId());
        assertEquals(bankBalance.getBank()
                                .getId(), bankBalanceDTO.getBank()
                                                        .getId());
        assertEquals(bankBalance.getBank()
                                .getName(), bankBalanceDTO.getBank()
                                                          .getName());
        assertEquals(bankBalance.getExpenses()
                                .toString(), bankBalanceDTO.getExpenses());
        assertEquals(bankBalance.getMonthBalance()
                                .getId(), bankBalanceDTO.getMonthBalanceId());
        assertEquals(bankBalance.getYearBalance()
                                .getId(), bankBalanceDTO.getYearBalanceId());
    }

    @Test
    void createBankBalanceTest() {
        String incomesString = "100.00";
        final BigDecimal incomes = new BigDecimal(incomesString);
        String expensesString = "50.00";
        final BigDecimal expenses = new BigDecimal(expensesString);
        BankDTO bankDTO = new BankDTO();
        bankDTO.setName("Revolut");
        BankBalanceDTO bankBalanceDTO = new BankBalanceDTO();
        bankBalanceDTO.setIncomes(incomesString);
        bankBalanceDTO.setExpenses(expensesString);
        bankBalanceDTO.setBank(bankDTO);
        long id = 3L;
        bankBalanceDTO.setYearBalanceId(id);
        bankBalanceDTO.setMonthBalanceId(id);
        BankBalanceDTO savedBankBalanceDTO = bankBalanceService.createNewBankBalance(bankBalanceDTO);
        assertNotNull(savedBankBalanceDTO);
        assertEquals(incomesString, savedBankBalanceDTO.getIncomes());
        assertEquals(expensesString, savedBankBalanceDTO.getExpenses());
        assertNotNull(savedBankBalanceDTO.getId());
        assertEquals(incomes.subtract(expenses)
                            .toString(), savedBankBalanceDTO.getResult());
        assertNotNull(savedBankBalanceDTO.getBank());
        assertEquals("Revolut", savedBankBalanceDTO.getBank()
                                                   .getName());
        assertEquals(id, savedBankBalanceDTO.getYearBalanceId());
        assertEquals(id, savedBankBalanceDTO.getMonthBalanceId());
    }

    @Test
    void deleteBankBalanceTest() {
        BankBalance bankBalance = bankBalanceRepository.findById(ID)
                                                       .get();
        bankBalanceService.deleteBankBalance(ID);
        assertThrows(ResourceNotFoundException.class, () -> bankBalanceService.findById(ID));
        MonthBalance monthBalanceOfBankBalanceDeleted = bankBalance.getMonthBalance();
        assertNotNull(monthBalanceOfBankBalanceDeleted);
        MonthBalance monthBalance =
                monthBalanceRepository.getOne(monthBalanceOfBankBalanceDeleted.getId());
        assertNotNull(monthBalance);
        assertEquals(BigDecimal.ZERO, monthBalance.getExpenses());
        assertEquals(BigDecimal.ZERO, monthBalance.getResult());
        assertEquals(BigDecimal.ZERO, monthBalance.getIncomes());

        YearBalance yearBalanceofBankBalanceDeleted = bankBalance.getYearBalance();
        assertNotNull(yearBalanceofBankBalanceDeleted);
        YearBalance yearBalance = yearBalanceRepository.getOne(yearBalanceofBankBalanceDeleted.getId());
        assertNotNull(yearBalance);
        assertEquals(BigDecimal.ZERO, yearBalance.getExpenses());
        assertEquals(BigDecimal.ZERO, yearBalance.getResult());
        assertEquals(BigDecimal.ZERO, yearBalance.getIncomes());
    }

    @Test
    void updateBankBalanceMonthBalanceIdTest() {
        BankBalance bankBalance = bankBalanceRepository.findById(ID)
                                                       .get();
        BankBalanceDTO bankBalanceDTO = bankBalanceMapper.bankBalanceToBankBalanceDTO(bankBalance);

        long oldMonthBalanceId = bankBalanceDTO.getMonthBalanceId();
        long monthBalanceId = 4L;
        bankBalanceDTO.setMonthBalanceId(monthBalanceId);
        bankBalanceDTO.setYearBalanceId(null);
        BankBalanceDTO savedBankBalanceDto = bankBalanceService.updateBankBalance(ID, bankBalanceDTO);
        assertNotNull(savedBankBalanceDto);
        assertEquals(monthBalanceId, savedBankBalanceDto.getMonthBalanceId());

        //Assert new MonthBalance modify correctly
        MonthBalanceDTO newMonthBalanceDTO = monthBalanceService.findById(monthBalanceId);
        assertEquals(savedBankBalanceDto.getIncomes(), newMonthBalanceDTO.getIncomes());
        assertEquals(savedBankBalanceDto.getExpenses(), newMonthBalanceDTO.getExpenses());
        assertEquals(savedBankBalanceDto.getResult(), newMonthBalanceDTO.getResult());

        //Assert old MonthBalance modify correctly
        MonthBalanceDTO oldMonthBalanceDTO = monthBalanceService.findById(oldMonthBalanceId);
        assertEquals("0", oldMonthBalanceDTO.getIncomes());
        assertEquals("0", oldMonthBalanceDTO.getExpenses());
        assertEquals("0", oldMonthBalanceDTO.getResult());
    }

    // Bank tests
    @Test
    void getAllBankTest() {
        assertEquals(bankRepository.count(), bankService.findAll()
                                                        .size());
    }

    @Test
    void getBankById() {
        Bank bank =
                bankRepository.findAll()
                              .stream()
                              .findAny()
                              .orElseThrow(ResourceNotFoundException::new);
        BankDTO bankDTO = bankService.findById(bank.getId());
        assertNotNull(bankDTO);
        assertBankAndBankDtoAreEquals(bank, bankDTO);
    }

    @Test
    void getBankByName() {
        Bank bank =
                bankRepository.findAll()
                              .stream()
                              .findAny()
                              .orElseThrow(ResourceNotFoundException::new);
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
