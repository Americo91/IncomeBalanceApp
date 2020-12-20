package com.astoppello.incomebalanceapp.controllers;

import com.astoppello.incomebalanceapp.dto.domain.MonthBalanceDTO;
import com.astoppello.incomebalanceapp.dto.domain.MonthBalanceListDTO;
import com.astoppello.incomebalanceapp.services.MonthBalanceService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Created by @author stopp on 20/12/2020
 */
@RestController
@RequestMapping(MonthBalanceController.BASE_URL)
public class MonthBalanceController {

    public static final String BASE_URL = "/api/v1/monthbalances/";
    private final MonthBalanceService monthBalanceService;


    public MonthBalanceController(MonthBalanceService monthBalanceService) {
        this.monthBalanceService = monthBalanceService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public MonthBalanceListDTO findAllMonthBalance() { return new MonthBalanceListDTO(monthBalanceService.findAll()); }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public MonthBalanceDTO findMonthBalanceById(@PathVariable Long id) {
        return monthBalanceService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public MonthBalanceDTO findMonthBalanceByMonth(@RequestBody String month) {
        return monthBalanceService.findByMonth(month);
    }

}
