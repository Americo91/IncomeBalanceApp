package com.astoppello.incomebalanceapp.cucumber;

import com.astoppello.incomebalanceapp.controllers.YearBalanceController;
import com.astoppello.incomebalanceapp.dto.domain.YearBalanceDTO;
import com.astoppello.incomebalanceapp.dto.domain.YearBalanceSetDTO;
import io.cucumber.spring.ScenarioScope;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.Year;

/** Created by @author stopp on 19/12/2021 */
@Component
@ScenarioScope
public class YearBalanceClient {

  private static final String HOST_URL = "http://localhost";
  protected RestTemplate restTemplate = new RestTemplate();
  @LocalServerPort private int port;

  YearBalanceSetDTO getYearBalances() {
    final String url = String.format("%s:%d%s/", HOST_URL, port, YearBalanceController.BASE_URL);
    ResponseEntity<YearBalanceSetDTO> response =
        restTemplate.getForEntity(url, YearBalanceSetDTO.class);
    return response.getBody();
  }

  public YearBalanceDTO createYearBalance(YearBalanceDTO yearBalanceDTO) {
    final String url = String.format("%s:%d%s", HOST_URL, port, YearBalanceController.BASE_URL);
    ResponseEntity<YearBalanceDTO> response = restTemplate.postForEntity(url, yearBalanceDTO, YearBalanceDTO.class);
    return response.getBody();
  }

  public YearBalanceDTO findYearBalanceByYear(int year) {
    final String url = String.format("%s:%d%s?year=%d", HOST_URL, port, YearBalanceController.BASE_URL, year);
    return restTemplate.getForEntity(url, YearBalanceDTO.class).getBody();
  }

    public YearBalanceDTO putYearBalance(Long id, YearBalanceDTO yearBalanceDTO) {
      final String url = String.format("%s:%d%s/%d", HOST_URL, port, YearBalanceController.BASE_URL, id);
      return restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(yearBalanceDTO), YearBalanceDTO.class).getBody();
    }
}
