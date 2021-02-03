package com.astoppello.incomebalanceapp.repositories;

import com.astoppello.incomebalanceapp.model.YearBalance;
import org.springframework.data.jpa.repository.JpaRepository;

/** Created by @author stopp on 15/11/2020 */
public interface YearBalanceRepository extends JpaRepository<YearBalance, Long> {

  YearBalance findByYear(int year);
}
