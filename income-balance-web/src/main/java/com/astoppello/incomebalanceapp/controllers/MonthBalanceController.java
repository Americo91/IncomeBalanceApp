package com.astoppello.incomebalanceapp.controllers;

import com.astoppello.incomebalanceapp.dto.domain.MonthBalanceDTO;
import com.astoppello.incomebalanceapp.dto.domain.MonthBalanceSetDTO;
import com.astoppello.incomebalanceapp.services.MonthBalanceService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.TreeSet;

/** Created by @author stopp on 20/12/2020 */
@RestController
public class MonthBalanceController {

  public static final String BASE_URL_BY_ID = "/api/v1/yearBalances/{yearBalanceId}/monthBalances";
  public static final String BASE_URL = "/api/v1/monthBalances";
  private final MonthBalanceService monthBalanceService;

  public MonthBalanceController(MonthBalanceService monthBalanceService) {
    this.monthBalanceService = monthBalanceService;
  }

  @GetMapping(BASE_URL_BY_ID + "/")
  @ResponseStatus(HttpStatus.OK)
  public MonthBalanceSetDTO findAllMonthBalanceById(@PathVariable Long yearBalanceId) {
    return new MonthBalanceSetDTO(new TreeSet<>(monthBalanceService.findAllById(yearBalanceId)));
  }

  @PostMapping(BASE_URL_BY_ID)
  @ResponseStatus(HttpStatus.CREATED)
  public MonthBalanceDTO createNewMonthBalanceById(
      @PathVariable Long yearBalanceId, @RequestBody MonthBalanceDTO monthBalanceDTO) {
    return monthBalanceService.createNewMonthBalanceById(yearBalanceId, monthBalanceDTO);
  }

  @GetMapping(BASE_URL)
  @ResponseStatus(HttpStatus.OK)
  public MonthBalanceSetDTO findMonthBalanceByMonth(@RequestParam String month) {
    return new MonthBalanceSetDTO(new TreeSet<>(monthBalanceService.findByMonth(month)));
  }

  @GetMapping(BASE_URL + "/")
  @ResponseStatus(HttpStatus.OK)
  public MonthBalanceSetDTO findAllMonthBalance() {
    return new MonthBalanceSetDTO(new TreeSet<>(monthBalanceService.findAll()));
  }

  @GetMapping(BASE_URL + "/{monthBalanceId}")
  @ResponseStatus(HttpStatus.OK)
  public MonthBalanceDTO findMonthBalanceById(@PathVariable Long monthBalanceId) {
    return monthBalanceService.findById(monthBalanceId);
  }

  @PutMapping(BASE_URL + "/{id}")
  @ResponseStatus(HttpStatus.OK)
  public MonthBalanceDTO saveMonthBalance(
      @PathVariable Long id, @RequestBody MonthBalanceDTO monthBalanceDTO) {
    return monthBalanceService.saveMonthBalance(id, monthBalanceDTO);
  }

  @PatchMapping(BASE_URL + "/{monthBalanceId}")
  @ResponseStatus(HttpStatus.OK)
  public MonthBalanceDTO updateMonthBalance(
      @PathVariable Long monthBalanceId, @RequestBody MonthBalanceDTO monthBalanceDTO) {
    return monthBalanceService.updateMonthBalance(monthBalanceId, monthBalanceDTO);
  }

  @DeleteMapping(BASE_URL + "/{monthBalanceId}")
  @ResponseStatus(HttpStatus.OK)
  public MonthBalanceDTO deleteMonthBalance(@PathVariable Long monthBalanceId) {
    return monthBalanceService.delete(monthBalanceId);
  }
}
