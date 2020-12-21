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

/**
 * Created by @author stopp on 16/11/2020
 */
@Component
public class Bootstrap implements CommandLineRunner {

    private final YearBalanceRepository yearBalanceRepository;
    private final BankRepository bankRepository;
    private final BankBalanceRepository bankBalanceRepository;
    private final MonthBalanceRepository monthBalanceRepository;

    public Bootstrap(YearBalanceRepository yearBalanceRepository, BankRepository bankRepository, BankBalanceRepository bankBalanceRepository, MonthBalanceRepository monthBalanceRepository) {
        this.yearBalanceRepository = yearBalanceRepository;
        this.bankRepository = bankRepository;
        this.bankBalanceRepository = bankBalanceRepository;
        this.monthBalanceRepository = monthBalanceRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Bank revolut = Bank.builder().id(1L).name("Revolut").build();
        Bank mediolanum = Bank.builder().id(2L).name("Mediolanum").build();
        bankRepository.save(revolut);
        bankRepository.save(mediolanum);
        bankRepository.save(Bank.builder().id(3L).build());
        System.out.println("Bank data loaded " + bankRepository.count());

        BankBalance bankBalance = BankBalance.builder().id(1L).expenses(new BigDecimal("100")).bank(revolut).build();
        bankBalanceRepository.save(bankBalance);
        System.out.println("BankBalance loaded " + bankBalanceRepository.count());

        MonthBalance september = MonthBalance.builder()
                                             .id(1L)
                                             .month("September")
                                             .salary(new BigDecimal("2229.58"))
                                             .build();
        MonthBalance monthBalance = MonthBalance.builder().id(2L).result(new BigDecimal("200.00")).build();
        MonthBalance october = MonthBalance.builder()
                                           .id(3L)
                                           .month("October")
                                           .incomes(new BigDecimal("2000"))
                                           .build()
                                           .addBankBalance(bankBalance);
        monthBalanceRepository.save(september);
        monthBalanceRepository.save(monthBalance);
        monthBalanceRepository.save(october);
        System.out.println("MonthBalance loaded " + monthBalanceRepository.count());

        yearBalanceRepository.save(YearBalance.builder().id(1L).year(2020).build());
        yearBalanceRepository.save(YearBalance.builder().id(2L).year(2019).salary(new BigDecimal("500.00")).build());
        yearBalanceRepository.save(YearBalance.builder()
                                              .id(3L)
                                              .expenses(new BigDecimal("500"))
                                              .build()
                                              .addMonthBalance(october));
        System.out.println("YearBalance data loaded " + yearBalanceRepository.count());
    }
}
