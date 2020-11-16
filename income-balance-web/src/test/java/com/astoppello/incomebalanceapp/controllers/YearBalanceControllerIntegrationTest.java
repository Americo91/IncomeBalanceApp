package com.astoppello.incomebalanceapp.controllers;

import com.astoppello.incomebalanceapp.bootstrap.Bootstrap;
import com.astoppello.incomebalanceapp.exceptions.ResourceNotFoundException;
import com.astoppello.incomebalanceapp.model.YearBalance;
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

    YearBalanceService yearBalanceService;

    @BeforeEach
    void setUp() throws Exception {
        System.out.println("Loanding data..");
        System.out.println(yearBalanceRepository.count());

        Bootstrap run = new Bootstrap(yearBalanceRepository);
        run.run();

        yearBalanceService = new YearBalanceServiceImpl(yearBalanceRepository);
    }

    @Test
    public void getAllYearBalance() {
        List<YearBalance> yearBalanceList = yearBalanceService.findAllYearBalance();
        assertEquals(3, yearBalanceList.size());
    }

    @Test
    public void getYearBalanceById() {
        YearBalance balance = yearBalanceRepository.findAll()
                                                   .stream()
                                                   .findAny()
                                                   .orElseThrow(ResourceNotFoundException::new);
        YearBalance yearBalance = yearBalanceService.findYearBalanceById(balance.getId());
        assertNotNull(yearBalance);
        assertEquals(balance, yearBalance);
    }

    @Test
    public void getYearBalanceByYear() {
        YearBalance balance =
                yearBalanceRepository.findAll()
                                     .stream()
                                     .filter(y -> Objects.nonNull(y.getYear()))
                                     .findAny()
                                     .orElseThrow(ResourceNotFoundException::new);
        YearBalance yearBalance = yearBalanceService.findYearBalanceByYear(balance.getYear());
        assertNotNull(yearBalance);
        assertEquals(balance, yearBalance);
        assertEquals(balance.getYear(), yearBalance.getYear());
    }
}
