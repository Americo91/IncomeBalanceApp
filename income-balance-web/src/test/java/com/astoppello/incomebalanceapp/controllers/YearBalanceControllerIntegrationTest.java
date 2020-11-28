package com.astoppello.incomebalanceapp.controllers;

import com.astoppello.incomebalanceapp.bootstrap.Bootstrap;
import com.astoppello.incomebalanceapp.dto.domain.YearBalanceDTO;
import com.astoppello.incomebalanceapp.dto.mappers.YearBalanceMapper;
import com.astoppello.incomebalanceapp.exceptions.ResourceNotFoundException;
import com.astoppello.incomebalanceapp.model.YearBalance;
import com.astoppello.incomebalanceapp.repositories.BankRepository;
import com.astoppello.incomebalanceapp.repositories.YearBalanceRepository;
import com.astoppello.incomebalanceapp.services.YearBalanceService;
import com.astoppello.incomebalanceapp.services.YearBalanceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Created by @author stopp on 16/11/2020
 */
@ExtendWith(SpringExtension.class)
@DataJpaTest
public class YearBalanceControllerIntegrationTest {

    @Autowired
    YearBalanceRepository yearBalanceRepository;
    @Autowired
    BankRepository bankRepository;

    YearBalanceService yearBalanceService;

    @BeforeEach
    void setUp() throws Exception {
        System.out.println("Loanding data: " + yearBalanceRepository.count());

        Bootstrap run = new Bootstrap(yearBalanceRepository, bankRepository);
        run.run();

        yearBalanceService = new YearBalanceServiceImpl(yearBalanceRepository, YearBalanceMapper.INSTANCE);
    }

    @Test
    void getAllYearBalanceTest() {
        List<YearBalanceDTO> yearBalanceList = yearBalanceService.findAllYearBalance();
        assertEquals(3, yearBalanceList.size());
    }

    @Test
    void getYearBalanceByIdTest() {
        YearBalance yearBalance = yearBalanceRepository.findAll()
                                                       .stream()
                                                       .findAny()
                                                       .orElseThrow(ResourceNotFoundException::new);
        YearBalanceDTO yearBalanceDTO = yearBalanceService.findYearBalanceById(yearBalance.getId());
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
}
