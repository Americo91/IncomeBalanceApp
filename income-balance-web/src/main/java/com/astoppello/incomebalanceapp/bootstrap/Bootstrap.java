package com.astoppello.incomebalanceapp.bootstrap;

import com.astoppello.incomebalanceapp.model.Bank;
import com.astoppello.incomebalanceapp.model.BankBalance;
import com.astoppello.incomebalanceapp.model.MonthBalance;
import com.astoppello.incomebalanceapp.model.YearBalance;
import com.astoppello.incomebalanceapp.repositories.BankBalanceRepository;
import com.astoppello.incomebalanceapp.repositories.BankRepository;
import com.astoppello.incomebalanceapp.repositories.MonthBalanceRepository;
import com.astoppello.incomebalanceapp.repositories.YearBalanceRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Month;

/**
 * Created by @author stopp on 16/11/2020
 */
@Component
public class Bootstrap implements CommandLineRunner {

    private final YearBalanceRepository yearBalanceRepository;
    private final BankRepository bankRepository;
    private final BankBalanceRepository bankBalanceRepository;
    private final MonthBalanceRepository monthBalanceRepository;

    public Bootstrap(YearBalanceRepository yearBalanceRepository, BankRepository bankRepository,
                     BankBalanceRepository bankBalanceRepository, MonthBalanceRepository monthBalanceRepository) {
        this.yearBalanceRepository = yearBalanceRepository;
        this.bankRepository = bankRepository;
        this.bankBalanceRepository = bankBalanceRepository;
        this.monthBalanceRepository = monthBalanceRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        final Bank revolut = Bank.builder().id(1L).name("Revolut").build();
        final Bank mediolanum = Bank.builder().id(2L).name("Mediolanum").build();
        final Bank boursorama = Bank.builder().id(3L).name("Boursorama").build();
        bankRepository.save(revolut);
        bankRepository.save(mediolanum);
        bankRepository.save(boursorama);
        System.out.println("Bank data loaded " + bankRepository.count());

        BankBalance bankBalance = BankBalance.builder().id(1L).expenses(new BigDecimal("100"))
                                             .incomes(new BigDecimal("150")).result(new BigDecimal("50")).bank(revolut)
                                             .build();

        MonthBalance september = MonthBalance.builder().id(2L).month(Month.SEPTEMBER).salary(new BigDecimal("2229.58"))
                                             .build();
        MonthBalance april = MonthBalance.builder().month(Month.APRIL).id(1L).salary(new BigDecimal("200.00"))
                                                .build();
        MonthBalance october = MonthBalance.builder().id(3L).month(Month.OCTOBER).salary(new BigDecimal("2000")).build().addBankBalance(bankBalance);
        MonthBalance november = MonthBalance.builder().month(Month.NOVEMBER).salary(new BigDecimal("200")).id(4L).build();
        monthBalanceRepository.save(april);
        monthBalanceRepository.save(september);
        monthBalanceRepository.save(october);
        monthBalanceRepository.save(november);
        System.out.println("MonthBalance loaded " + monthBalanceRepository.count());

        yearBalanceRepository.save(YearBalance.builder().id(1L).year(2020).build().addMonthBalance(april));
        YearBalance yearBalance1 = YearBalance.builder().id(2L).year(2019).build().addMonthBalance(september);
        yearBalanceRepository.save(yearBalance1);
        YearBalance yearBalance = YearBalance.builder().id(3L).year(2021).build().addMonthBalance(october)
                                             .addMonthBalance(november).addBankBalance(bankBalance);
        yearBalanceRepository.save(yearBalance);
        System.out.println("YearBalance data loaded " + yearBalanceRepository.count());

        bankBalanceRepository.save(bankBalance);
        System.out.println("BankBalance loaded " + bankBalanceRepository.count());
    }
}
