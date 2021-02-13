package com.astoppello.incomebalanceapp.controllers;

import com.astoppello.incomebalanceapp.dto.domain.BankBalanceDTO;
import com.astoppello.incomebalanceapp.dto.domain.BankBalanceListDTO;
import com.astoppello.incomebalanceapp.services.BankBalanceService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/** Created by @author stopp on 28/11/2020 */
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
  public BankBalanceListDTO findAllBankBalancesByIds(
      @PathVariable Long yearBalanceId, @PathVariable Long monthBalanceId) {
    return new BankBalanceListDTO(bankBalanceService.findAllByIds(yearBalanceId, monthBalanceId));
  }

  @GetMapping(BASE_URL_BY_YEARBALANCEID)
  @ResponseStatus(HttpStatus.OK)
  public BankBalanceListDTO findAllBankBalancesByYearBalanceId(@PathVariable Long yearBalanceId) {
    return new BankBalanceListDTO(bankBalanceService.findAllByYearBalanceId(yearBalanceId));
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
  public BankBalanceListDTO findAllBankBalances() {
    return new BankBalanceListDTO(bankBalanceService.findAll());
  }

  @GetMapping(BASE_URL + "/{bankBalanceId}")
  @ResponseStatus(HttpStatus.OK)
  public BankBalanceDTO findBankBalanceById(@PathVariable Long bankBalanceId) {
    return bankBalanceService.findById(bankBalanceId);
  }

  @GetMapping(BASE_URL)
  @ResponseStatus(HttpStatus.OK)
  public BankBalanceListDTO findBankBalanceByName(@RequestParam String bankName) {
    return new BankBalanceListDTO(bankBalanceService.findByBankName(bankName));
  }

  @PostMapping(BASE_URL)
  @ResponseStatus(HttpStatus.CREATED)
  public BankBalanceDTO createNewBankBalance(@RequestBody BankBalanceDTO bankBalanceDTO) {
    return bankBalanceService.createNewBankBalance(bankBalanceDTO);
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
  public void deleteBankBalance(@PathVariable Long bankBalanceId) {
    bankBalanceService.deleteBankBalance(bankBalanceId);
  }
}
