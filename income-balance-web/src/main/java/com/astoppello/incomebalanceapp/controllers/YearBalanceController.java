package com.astoppello.incomebalanceapp.controllers;

import com.astoppello.incomebalanceapp.dto.domain.YearBalanceDTO;
import com.astoppello.incomebalanceapp.dto.domain.YearBalanceSetDTO;
import com.astoppello.incomebalanceapp.services.YearBalanceService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

/** Created by @author stopp on 15/11/2020 */
@RestController
@RequestMapping(YearBalanceController.BASE_URL)
public class YearBalanceController {

  public static final String BASE_URL = "/api/v1/yearBalances";
  private final YearBalanceService yearBalanceService;

  public YearBalanceController(YearBalanceService yearBalanceService) {
    this.yearBalanceService = yearBalanceService;
  }

  @GetMapping("/")
  @ResponseStatus(HttpStatus.OK)
  public YearBalanceSetDTO findAllYearBalance() {
    return new YearBalanceSetDTO(new TreeSet<>(yearBalanceService.findAll()));
  }

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public YearBalanceDTO findYearBalanceById(@PathVariable Long id) {
    return yearBalanceService.findById(id);
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public YearBalanceDTO findYearBalanceByYear(@RequestParam Integer year) {
    return yearBalanceService.findYearBalanceByYear(year);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public YearBalanceDTO createNewYearBalance(@RequestBody YearBalanceDTO yearBalanceDTO) {
    return yearBalanceService.createNewYearBalance(yearBalanceDTO);
  }

  @PutMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public YearBalanceDTO saveYearBalance(@PathVariable Long id, @RequestBody YearBalanceDTO yearBalanceDTO) {
    return yearBalanceService.saveYearBalance(id, yearBalanceDTO);
  }

  @PatchMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public YearBalanceDTO patchYearBalance(@PathVariable Long id, @RequestBody YearBalanceDTO yearBalanceDTO) {
    return yearBalanceService.updateYearBalance(id, yearBalanceDTO);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public void deleteYearBalance(@PathVariable Long id) {
    yearBalanceService.deleteYearBalance(id);
  }
}
