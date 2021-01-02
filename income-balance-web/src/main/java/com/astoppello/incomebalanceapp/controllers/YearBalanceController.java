package com.astoppello.incomebalanceapp.controllers;

import com.astoppello.incomebalanceapp.dto.domain.YearBalanceDTO;
import com.astoppello.incomebalanceapp.dto.domain.YearBalanceListDTO;
import com.astoppello.incomebalanceapp.services.YearBalanceService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/** Created by @author stopp on 15/11/2020 */
@RestController
@RequestMapping(YearBalanceController.BASE_URL)
public class YearBalanceController {

  public static final String BASE_URL = "/api/v1/yearBalances";
  private final YearBalanceService yearBalanceService;

  public YearBalanceController(YearBalanceService yearBalanceService) {
    this.yearBalanceService = yearBalanceService;
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public YearBalanceListDTO findAllYearBalance() {
    return new YearBalanceListDTO(yearBalanceService.findAll());
  }

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public YearBalanceDTO findYearBalanceById(@PathVariable Long id) {
    return yearBalanceService.findById(id);
  }

  /*
  @PostMapping
  @ResponseStatus(HttpStatus.OK)
  public YearBalanceDTO findYearBalanceByYear(@RequestBody Integer year) {
    return yearBalanceService.findYearBalanceByYear(year);
  }
   */

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public YearBalanceDTO createNewYearBalance(@RequestBody YearBalanceDTO yearBalanceDTO) {
    return yearBalanceService.createNewYearBalance(yearBalanceDTO);
  }
}
