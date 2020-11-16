package com.astoppello.incomebalanceapp.controllers;

import com.astoppello.incomebalanceapp.model.YearBalance;
import com.astoppello.incomebalanceapp.services.YearBalanceService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by @author stopp on 15/11/2020
 */
@RestController
@RequestMapping(YearBalanceController.BASE_URL)
public class YearBalanceController {

    public static final String BASE_URL = "/api/v1/years";
    private final YearBalanceService yearBalanceService;

    public YearBalanceController(YearBalanceService yearBalanceService) {
        this.yearBalanceService = yearBalanceService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<YearBalance> findAllYearBalance() {
        return yearBalanceService.findAllYearBalance();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public YearBalance findYearBalanceById(@PathVariable Long id) {
        return yearBalanceService.findYearBalanceById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public YearBalance findYearBalanceByYear(@RequestBody int year) {
        return yearBalanceService.findYearBalanceByYear(year);
    }
}
