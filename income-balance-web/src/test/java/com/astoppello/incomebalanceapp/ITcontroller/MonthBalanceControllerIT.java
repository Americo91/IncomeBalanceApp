package com.astoppello.incomebalanceapp.ITcontroller;

import com.astoppello.incomebalanceapp.controllers.MonthBalanceController;
import com.astoppello.incomebalanceapp.controllers.YearBalanceController;
import com.astoppello.incomebalanceapp.dto.domain.MonthBalanceDTO;
import com.astoppello.incomebalanceapp.dto.domain.MonthBalanceSetDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.time.Month;

import static com.astoppello.incomebalanceapp.controllers.AbstractRestControllerTest.asJsonString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

/**
 * Created by @author stopp on 19/06/2021
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MonthBalanceControllerIT {

    private static final Long ID = 1L;
    @Autowired
    private MockMvc mockMvc;
    private Long createdMonthBalanceId;

    @Test
    @Order(1)
    void findAllMonthBalanceById() throws Exception {
        MvcResult result = mockMvc.perform(get(YearBalanceController.BASE_URL + "/" + ID + "/monthBalances/")
                .contentType(MediaType.APPLICATION_JSON)).andReturn();

        int status = result.getResponse().getStatus();
        assertThat(status).isEqualTo(HttpStatus.OK.value());

        MonthBalanceSetDTO monthBalanceSetDTO = new ObjectMapper()
                .readValue(result.getResponse().getContentAsString(), MonthBalanceSetDTO.class);
        assertThat(monthBalanceSetDTO.getMonthBalances().size()).isEqualTo(1);
        MonthBalanceDTO monthBalanceDTO = monthBalanceSetDTO.getMonthBalances().iterator().next();
        assertThat(monthBalanceDTO.getMonth()).isEqualTo(Month.APRIL);
        assertThat(monthBalanceDTO.getYearBalanceId()).isEqualTo(ID);
        assertThat(monthBalanceDTO.getSalary()).isEqualTo("200.00");
    }

    @Test
    @Order(1)
    void findMonthBalanceByMonth() throws Exception {
        MvcResult result = mockMvc.perform(
                get(MonthBalanceController.BASE_URL + "?month=OCTOBER").contentType(MediaType.APPLICATION_JSON))
                                  .andReturn();

        int status = result.getResponse().getStatus();
        assertThat(status).isEqualTo(HttpStatus.OK.value());

        MonthBalanceSetDTO monthBalanceSETDTO = new ObjectMapper()
                .readValue(result.getResponse().getContentAsString(), MonthBalanceSetDTO.class);
        assertThat(monthBalanceSETDTO.getMonthBalances().size()).isGreaterThan(0);
        MonthBalanceDTO monthBalanceDTO = monthBalanceSETDTO.getMonthBalances().iterator().next();
        assertThat(monthBalanceDTO.getMonth()).isEqualTo(Month.OCTOBER);
        assertThat(monthBalanceDTO.getSalary()).isEqualTo("2000.00");
        assertThat(monthBalanceDTO.getYearBalanceId()).isEqualTo(3L);
        assertThat(monthBalanceDTO.getId()).isEqualTo(3L);
    }

    @Test
    @Order(1)
    void findAllMonthBalance() throws Exception {
        MvcResult result = mockMvc
                .perform(get(MonthBalanceController.BASE_URL + "/").contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        int status = result.getResponse().getStatus();
        assertThat(status).isEqualTo(HttpStatus.OK.value());

        MonthBalanceSetDTO monthBalanceSetDTO = new ObjectMapper()
                .readValue(result.getResponse().getContentAsString(), MonthBalanceSetDTO.class);
        assertThat(monthBalanceSetDTO.getMonthBalances().size()).isEqualTo(4);
    }

    @Test
    @Order(1)
    void findMonthBalanceById() throws Exception {
        MvcResult result = mockMvc
                .perform(get(MonthBalanceController.BASE_URL + "/4").contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        int status = result.getResponse().getStatus();
        assertThat(status).isEqualTo(HttpStatus.OK.value());

        MonthBalanceDTO monthBalanceDTO = new ObjectMapper()
                .readValue(result.getResponse().getContentAsString(), MonthBalanceDTO.class);
        assertThat(monthBalanceDTO.getMonth()).isEqualTo(Month.NOVEMBER);
        assertThat(monthBalanceDTO.getSalary()).isEqualTo("200.00");
        assertThat(monthBalanceDTO.getYearBalanceId()).isEqualTo(3L);
        assertThat(monthBalanceDTO.getId()).isEqualTo(4L);
    }

    @Test
    @Order(2)
    void createNewMonthBalanceById() throws Exception {
        MonthBalanceDTO monthBalanceDTO = MonthBalanceDTO.builder().yearBalanceId(ID).month(Month.NOVEMBER)
                                                         .salary(new BigDecimal("300.00")).build();
        MvcResult result = mockMvc.perform(post(YearBalanceController.BASE_URL + "/" + ID + "/monthBalances")
                .contentType(MediaType.APPLICATION_JSON).content(asJsonString(monthBalanceDTO))).andReturn();

        int status = result.getResponse().getStatus();
        assertThat(status).isEqualTo(HttpStatus.CREATED.value());

        MonthBalanceDTO monthBalanceDTOResult = new ObjectMapper()
                .readValue(result.getResponse().getContentAsString(), MonthBalanceDTO.class);
        createdMonthBalanceId = monthBalanceDTOResult.getId();
        assertThat(monthBalanceDTOResult.getMonth()).isEqualTo(Month.NOVEMBER);
        assertThat(monthBalanceDTOResult.getSalary()).isEqualTo("300.00");
        assertThat(monthBalanceDTOResult.getYearBalanceId()).isEqualTo(ID);
        assertThat(monthBalanceDTOResult.getSavingPercentage()).isNull();
        assertThat(monthBalanceDTOResult.getId()).isNotNull();
    }

    @Test
    @Order(3)
    void updateMonthBalance() throws Exception {
        MonthBalanceDTO monthBalanceDTO = MonthBalanceDTO.builder().month(Month.DECEMBER).build();
        MvcResult result = mockMvc.perform(patch(MonthBalanceController.BASE_URL + "/" + createdMonthBalanceId)
                .contentType(MediaType.APPLICATION_JSON).content(asJsonString(monthBalanceDTO))).andReturn();

        int status = result.getResponse().getStatus();
        assertThat(status).isEqualTo(HttpStatus.OK.value());

        MonthBalanceDTO monthBalanceDTOResult = new ObjectMapper()
                .readValue(result.getResponse().getContentAsString(), MonthBalanceDTO.class);
        assertThat(monthBalanceDTOResult.getMonth()).isEqualTo(Month.DECEMBER);
        assertThat(monthBalanceDTOResult.getSalary()).isEqualTo("300.00");
        assertThat(monthBalanceDTOResult.getYearBalanceId()).isEqualTo(ID);
        assertThat(monthBalanceDTOResult.getId()).isEqualTo(createdMonthBalanceId);
    }

    @Test
    @Order(4)
    void saveMonthBalance() throws Exception {
        MonthBalanceDTO monthBalanceDTO = MonthBalanceDTO.builder().yearBalanceId(ID).month(Month.DECEMBER).build();
        MvcResult result = mockMvc.perform(put(MonthBalanceController.BASE_URL + "/" + createdMonthBalanceId)
                .contentType(MediaType.APPLICATION_JSON).content(asJsonString(monthBalanceDTO))).andReturn();

        int status = result.getResponse().getStatus();
        assertThat(status).isEqualTo(HttpStatus.OK.value());

        MonthBalanceDTO monthBalanceDTOResult = new ObjectMapper()
                .readValue(result.getResponse().getContentAsString(), MonthBalanceDTO.class);
        assertThat(monthBalanceDTOResult.getMonth()).isEqualTo(Month.DECEMBER);
        assertThat(monthBalanceDTOResult.getSalary()).isEqualTo("0");
        assertThat(monthBalanceDTOResult.getYearBalanceId()).isEqualTo(ID);
        assertThat(monthBalanceDTOResult.getId()).isEqualTo(createdMonthBalanceId);
    }

    @Test
    @Order(5)
    void deleteMonthBalance() throws Exception {
        MvcResult result = mockMvc.perform(delete(MonthBalanceController.BASE_URL + "/" + createdMonthBalanceId)
                .contentType(MediaType.APPLICATION_JSON)).andReturn();
        int status = result.getResponse().getStatus();
        assertThat(status).isEqualTo(HttpStatus.OK.value());

        MonthBalanceDTO monthBalanceDTOResult = new ObjectMapper()
                .readValue(result.getResponse().getContentAsString(), MonthBalanceDTO.class);
        assertThat(monthBalanceDTOResult.getId()).isEqualTo(createdMonthBalanceId);
        assertThat(monthBalanceDTOResult.getMonth()).isEqualTo(Month.DECEMBER);
        assertThat(monthBalanceDTOResult.getSalary()).isEqualTo("0.00");
        assertThat(monthBalanceDTOResult.getYearBalanceId()).isEqualTo(ID);

        result = mockMvc.perform(get(MonthBalanceController.BASE_URL + "/"+createdMonthBalanceId).contentType(MediaType.APPLICATION_JSON))
                        .andReturn();
        status = result.getResponse().getStatus();
        assertThat(status).isEqualTo(HttpStatus.NOT_FOUND.value());
    }
}
