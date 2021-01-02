package com.astoppello.incomebalanceapp.controllers;

import com.astoppello.incomebalanceapp.dto.domain.BankBalanceDTO;
import com.astoppello.incomebalanceapp.dto.domain.BankBalanceListDTO;
import com.astoppello.incomebalanceapp.services.BankBalanceService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/** Created by @author stopp on 28/11/2020 */
@RestController
@RequestMapping(BankBalanceController.BASE_URL)
public class BankBalanceController {

  public static final String BASE_URL =
      "/api/v1/yearBalances/{yearBalanceId}/monthBalances/{monthBalanceId}/bankBalances";
  private final BankBalanceService bankBalanceService;

  public BankBalanceController(BankBalanceService bankBalanceService) {
    this.bankBalanceService = bankBalanceService;
  }

  @GetMapping()
  @ResponseStatus(HttpStatus.OK)
  public BankBalanceListDTO findAllBankBalances(
      @PathVariable Long yearBalanceId, @PathVariable Long monthBalanceId) {
    return new BankBalanceListDTO(bankBalanceService.findAll(yearBalanceId, monthBalanceId));
  }

  @GetMapping("/{bankBalanceId}")
  @ResponseStatus(HttpStatus.OK)
  public BankBalanceDTO findBankBalanceById(
      @PathVariable Long yearBalanceId,
      @PathVariable Long monthBalanceId,
      @PathVariable Long bankBalanceId) {
    return bankBalanceService.findById(yearBalanceId, monthBalanceId, bankBalanceId);
  }

  @PostMapping()
  @ResponseStatus(HttpStatus.OK)
  public BankBalanceDTO findBankBalanceByName(
      @PathVariable Long yearBalanceId,
      @PathVariable Long monthBalanceId,
      @RequestBody String bankName) {
    return bankBalanceService.findByBankName(yearBalanceId, monthBalanceId, bankName);
  }
}
