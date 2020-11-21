package com.astoppello.incomebalanceapp.repositories;

import com.astoppello.incomebalanceapp.model.Bank;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by @author stopp on 21/11/2020
 */
public interface BankRepository extends JpaRepository<Bank, Long> {
    Bank findBankByName(String name);
}
