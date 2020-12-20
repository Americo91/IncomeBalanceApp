package com.astoppello.incomebalanceapp.repositories;

import com.astoppello.incomebalanceapp.model.MonthBalance;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by @author stopp on 20/12/2020
 */
public interface MonthBalanceRepository extends JpaRepository<MonthBalance, Long> {

    MonthBalance findByMonth(String month);
}
