package com.astoppello.incomebalanceapp.controllers;

import com.astoppello.incomebalanceapp.dto.domain.BankDTO;
import com.astoppello.incomebalanceapp.dto.domain.BankSetDTO;
import com.astoppello.incomebalanceapp.services.BankService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.TreeSet;

/**
 * Created by @author stopp on 21/11/2020
 */
@RestController
@RequestMapping(BankController.BASE_URL)
public class BankController {

    public static final String BASE_URL = "/api/v1/banks";
    private final BankService bankService;

    public BankController(BankService bankService) {
        this.bankService = bankService;
    }

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public BankSetDTO findAllBanks() {
        return new BankSetDTO(new TreeSet<>(bankService.findAll()));
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BankDTO findBankById(@PathVariable Long id) {
        return bankService.findById(id);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public BankDTO findBankByName(@RequestParam String name) {
        return bankService.findBankByName(name);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public BankDTO createNewBank(@RequestBody BankDTO bankDTO) {
        return bankService.createNewBank(bankDTO);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BankDTO saveBank(@PathVariable Long id, @RequestBody BankDTO bankDTO) {
        return bankService.saveBank(id, bankDTO);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BankDTO updateBank(@PathVariable Long id, @RequestBody BankDTO bankDTO) {
        return bankService.updateBank(id, bankDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteBank(@PathVariable Long id) {
        bankService.deleteBank(id);
    }
}
