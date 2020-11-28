package com.astoppello.incomebalanceapp.controllers;

import com.astoppello.incomebalanceapp.dto.domain.BankBalanceDTO;
import com.astoppello.incomebalanceapp.dto.domain.BankBalanceListDTO;
import com.astoppello.incomebalanceapp.services.BankBalanceService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Created by @author stopp on 28/11/2020
 */
@RestController
@RequestMapping(BankBalanceController.BASE_URL)
public class BankBalanceController {

    public static final String BASE_URL = "/api/v1/bankbalances";
    private final BankBalanceService bankBalanceService;


    public BankBalanceController(BankBalanceService bankBalanceService) {
        this.bankBalanceService = bankBalanceService;
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public BankBalanceListDTO findAllBankBalances() {
        return new BankBalanceListDTO(bankBalanceService.findAll());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BankBalanceDTO findBankBalanceById(@PathVariable Long id){
        return bankBalanceService.findById(id);
    }
}
