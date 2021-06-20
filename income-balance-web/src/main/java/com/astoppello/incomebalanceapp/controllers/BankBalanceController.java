package com.astoppello.incomebalanceapp.controllers;

import com.astoppello.incomebalanceapp.dto.domain.BankBalanceDTO;
import com.astoppello.incomebalanceapp.dto.domain.BankBalanceSetDTO;
import com.astoppello.incomebalanceapp.services.BankBalanceService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.TreeSet;

/**
 * Created by @author stopp on 28/11/2020
 */
@RestController
public class BankBalanceController {

    public static final String BASE_URL_BY_IDS =
            "/api/v1/yearBalances/{yearBalanceId}/monthBalances/{monthBalanceId}/bankBalances";
    public static final String BASE_URL_BY_YEARBALANCEID = "/api/v1/yearBalances/{yearBalanceId}/bankBalances/";
    public static final String BASE_URL = "/api/v1/bankBalances";
    private final BankBalanceService bankBalanceService;

    public BankBalanceController(BankBalanceService bankBalanceService) {
        this.bankBalanceService = bankBalanceService;
    }

    @GetMapping(BASE_URL_BY_IDS + "/")
    @ResponseStatus(HttpStatus.OK)
    public BankBalanceSetDTO findAllBankBalancesByIds(
            @PathVariable Long yearBalanceId, @PathVariable Long monthBalanceId) {
        return new BankBalanceSetDTO(new TreeSet<>(bankBalanceService.findAllByIds(yearBalanceId, monthBalanceId)));
    }

    @GetMapping(BASE_URL_BY_YEARBALANCEID)
    @ResponseStatus(HttpStatus.OK)
    public BankBalanceSetDTO findAllBankBalancesByYearBalanceId(@PathVariable Long yearBalanceId) {
        return new BankBalanceSetDTO(new TreeSet<>(bankBalanceService.findAllByYearBalanceId(yearBalanceId)));
    }

    @PostMapping(BASE_URL_BY_IDS)
    @ResponseStatus(HttpStatus.CREATED)
    public BankBalanceDTO createNewBankBalancesById(
            @PathVariable Long yearBalanceId,
            @PathVariable Long monthBalanceId,
            @RequestBody BankBalanceDTO bankBalanceDTO) {
        return bankBalanceService.createNewBankBalanceById(yearBalanceId, monthBalanceId, bankBalanceDTO);
    }

    @GetMapping(BASE_URL + "/")
    @ResponseStatus(HttpStatus.OK)
    public BankBalanceSetDTO findAllBankBalances() {
        return new BankBalanceSetDTO(new TreeSet<>(bankBalanceService.findAll()));
    }

    @GetMapping(BASE_URL + "/{bankBalanceId}")
    @ResponseStatus(HttpStatus.OK)
    public BankBalanceDTO findBankBalanceById(@PathVariable Long bankBalanceId) {
        return bankBalanceService.findById(bankBalanceId);
    }

    @GetMapping(BASE_URL)
    @ResponseStatus(HttpStatus.OK)
    public BankBalanceSetDTO findBankBalanceByName(@RequestParam String bankName) {
        return new BankBalanceSetDTO(new TreeSet<>(bankBalanceService.findByBankName(bankName)));
    }

    @PutMapping(BASE_URL + "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BankBalanceDTO saveBankBalance(
            @PathVariable Long id, @RequestBody BankBalanceDTO bankBalanceDTO) {
        return bankBalanceService.saveBankBalance(id, bankBalanceDTO);
    }

    @PatchMapping(BASE_URL + "/{bankBalanceId}")
    @ResponseStatus(HttpStatus.OK)
    public BankBalanceDTO updateBankBalance(
            @PathVariable Long bankBalanceId, @RequestBody BankBalanceDTO bankBalanceDTO) {
        return bankBalanceService.updateBankBalance(bankBalanceId, bankBalanceDTO);
    }

    @DeleteMapping(BASE_URL + "/{bankBalanceId}")
    @ResponseStatus(HttpStatus.OK)
    public BankBalanceDTO deleteBankBalance(@PathVariable Long bankBalanceId) {
        BankBalanceDTO bankBalanceDTO = bankBalanceService.findById(bankBalanceId);
        bankBalanceService.deleteBankBalance(bankBalanceId);
        return bankBalanceDTO;
    }
}
