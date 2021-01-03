package com.astoppello.incomebalanceapp.controllers;

import com.astoppello.incomebalanceapp.dto.domain.BankDTO;
import com.astoppello.incomebalanceapp.dto.domain.BankListDTO;
import com.astoppello.incomebalanceapp.services.BankService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/** Created by @author stopp on 21/11/2020 */
@RestController
@RequestMapping(BankController.BASE_URL)
public class BankController {

  public static final String BASE_URL = "/api/v1/banks";
  private final BankService bankService;

  public BankController(BankService bankService) {
    this.bankService = bankService;
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public BankListDTO findAllBanks() {
    return new BankListDTO(bankService.findAll());
  }

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public BankDTO findBankById(@PathVariable Long id) {
    return bankService.findById(id);
  }

  /*
  @PostMapping()
  @ResponseStatus(HttpStatus.OK)
  public BankDTO findBankByName(@RequestBody String name) {
    return bankService.findBankByName(name);
  }
   */

  @PostMapping()
  @ResponseStatus(HttpStatus.CREATED)
  public BankDTO createNewBank(@RequestBody BankDTO bankDTO) {
    return bankService.createNewBank(bankDTO);
  }
}
