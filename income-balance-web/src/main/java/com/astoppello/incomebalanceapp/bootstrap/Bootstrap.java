package com.astoppello.incomebalanceapp.bootstrap;

import com.astoppello.incomebalanceapp.model.Bank;
import com.astoppello.incomebalanceapp.model.BankBalance;
import com.astoppello.incomebalanceapp.model.YearBalance;
import com.astoppello.incomebalanceapp.repositories.BankBalanceRepository;
import com.astoppello.incomebalanceapp.repositories.BankRepository;
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
    private final BankRepository bankRepository;
    private final BankBalanceRepository bankBalanceRepository;

    public Bootstrap(YearBalanceRepository yearBalanceRepository, BankRepository bankRepository,
                     BankBalanceRepository bankBalanceRepository) {
        this.yearBalanceRepository = yearBalanceRepository;
        this.bankRepository = bankRepository;
        this.bankBalanceRepository = bankBalanceRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        yearBalanceRepository.save(YearBalance.builder().id(1L).year(2020).build());
        yearBalanceRepository.save(YearBalance.builder().id(2L).year(2019).salary(new BigDecimal("500.00")).build());
        yearBalanceRepository.save(YearBalance.builder().id(3L).expenses(new BigDecimal("500")).build());
        System.out.println("YearBalance data loaded " + yearBalanceRepository.count());

        Bank revolut = Bank.builder().id(1L).name("Revolut").build();
        Bank mediolanum = Bank.builder().id(2L).name("Mediolanum").build();
        bankRepository.save(revolut);
        bankRepository.save(mediolanum);
        bankRepository.save(Bank.builder().id(3L).build());
        System.out.println("Bank data loaded "+ bankRepository.count());

        BankBalance bankBalance = BankBalance.builder().id(1L).expenses(new BigDecimal("100")).bank(revolut).build();
        bankBalanceRepository.save(bankBalance);
    }
}
