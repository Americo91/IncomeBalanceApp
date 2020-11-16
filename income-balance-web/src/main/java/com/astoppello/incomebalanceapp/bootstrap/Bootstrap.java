package com.astoppello.incomebalanceapp.bootstrap;

import com.astoppello.incomebalanceapp.model.YearBalance;
import com.astoppello.incomebalanceapp.repositories.YearBalanceRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Created by @author stopp on 16/11/2020
 */
@Component
public class Bootstrap implements CommandLineRunner {

    private final YearBalanceRepository yearBalanceRepository;

    public Bootstrap(YearBalanceRepository yearBalanceRepository) {
        this.yearBalanceRepository = yearBalanceRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        yearBalanceRepository.save(YearBalance.builder().id(1L).year(2020).build());
        yearBalanceRepository.save(YearBalance.builder().id(2L).year(2019).salary(new BigDecimal("500.00")).build());
        yearBalanceRepository.save(YearBalance.builder().id(3L).expenses(new BigDecimal("500")).build());
        System.out.println("YearBalance data loaded " + yearBalanceRepository.count());
    }
}
