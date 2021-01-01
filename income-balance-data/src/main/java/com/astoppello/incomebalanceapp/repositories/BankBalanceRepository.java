package com.astoppello.incomebalanceapp.repositories;

import com.astoppello.incomebalanceapp.model.BankBalance;
import org.springframework.data.jpa.repository.JpaRepository;

/** Created by @author stopp on 28/11/2020 */
public interface BankBalanceRepository extends JpaRepository<BankBalance, Long> {}
