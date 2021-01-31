package com.astoppello.incomebalanceapp.controllers;

import com.astoppello.incomebalanceapp.dto.domain.MonthBalanceDTO;
import com.astoppello.incomebalanceapp.dto.domain.MonthBalanceListDTO;
import com.astoppello.incomebalanceapp.services.MonthBalanceService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
  public MonthBalanceListDTO findAllMonthBalanceById(@PathVariable Long yearBalanceId) {
    return new MonthBalanceListDTO(monthBalanceService.findAllById(yearBalanceId));
  }

  @GetMapping(BASE_URL_BY_ID + "/{monthBalanceId}")
  @ResponseStatus(HttpStatus.OK)
  public MonthBalanceDTO findMonthBalanceOfYearById(
      @PathVariable Long yearBalanceId, @PathVariable Long monthBalanceId) {
    return monthBalanceService.findMonthOfYearById(yearBalanceId, monthBalanceId);
  }

  @PostMapping(BASE_URL_BY_ID)
  @ResponseStatus(HttpStatus.CREATED)
  public MonthBalanceDTO createNewMonthBalanceById(
      @PathVariable Long yearBalanceId, @RequestBody MonthBalanceDTO monthBalanceDTO) {
    return monthBalanceService.createNewMonthBalanceById(yearBalanceId, monthBalanceDTO);
  }

  @GetMapping(BASE_URL)
  @ResponseStatus(HttpStatus.OK)
  public MonthBalanceListDTO findMonthBalanceByMonth(@RequestParam String month) {
    return new MonthBalanceListDTO(monthBalanceService.findByMonth(month));
  }

  @GetMapping(BASE_URL + "/")
  @ResponseStatus(HttpStatus.OK)
  public MonthBalanceListDTO findAllMonthBalance() {
    return new MonthBalanceListDTO(monthBalanceService.findAll());
  }

  @GetMapping(BASE_URL + "/{monthBalanceId}")
  @ResponseStatus(HttpStatus.OK)
  public MonthBalanceDTO findMonthBalanceById(@PathVariable Long monthBalanceId) {
    return monthBalanceService.findById(monthBalanceId);
  }

  @PostMapping(BASE_URL)
  @ResponseStatus(HttpStatus.CREATED)
  public MonthBalanceDTO createNewMonthBalance(@RequestBody MonthBalanceDTO monthBalanceDTO) {
    return monthBalanceService.createNewMonthBalance(monthBalanceDTO);
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
  public void deleteMonthBalance(@PathVariable Long monthBalanceId) {
    monthBalanceService.delete(monthBalanceId);
  }
}
