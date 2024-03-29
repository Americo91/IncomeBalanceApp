package com.astoppello.incomebalanceapp.ITcontroller;

import com.astoppello.incomebalanceapp.controllers.YearBalanceController;
import com.astoppello.incomebalanceapp.dto.domain.YearBalanceDTO;
import com.astoppello.incomebalanceapp.dto.domain.YearBalanceSetDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.annotations.OnDelete;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.astoppello.incomebalanceapp.controllers.AbstractRestControllerTest.asJsonString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

/**
 * Created by @author stopp on 20/06/2021
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class YearBalanceControllerIT {

    private static final Long ID = 3L;
    @Autowired
    private MockMvc mockMvc;
    private Long createdYearBalanceDtoId;

    @Test
    @Order(1)
    void findAllYearBalance() throws Exception {
        MvcResult result = mockMvc
                .perform(get(YearBalanceController.BASE_URL + "/").contentType(MediaType.APPLICATION_JSON)).andReturn();
        int status = result.getResponse().getStatus();
        assertThat(status).isEqualTo(HttpStatus.OK.value());

        YearBalanceSetDTO yearBalanceSetDTO = new ObjectMapper()
                .readValue(result.getResponse().getContentAsString(), YearBalanceSetDTO.class);
        assertThat(yearBalanceSetDTO.getYearBalances().size()).isEqualTo(3);
    }

    @Test
    @Order(1)
    void findYearBalanceById() throws Exception {
        MvcResult result = mockMvc
                .perform(get(YearBalanceController.BASE_URL + "/" + ID).contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        int status = result.getResponse().getStatus();
        assertThat(status).isEqualTo(HttpStatus.OK.value());

        YearBalanceDTO yearBalanceDTO = new ObjectMapper()
                .readValue(result.getResponse().getContentAsString(), YearBalanceDTO.class);
        assertThat(yearBalanceDTO.getYear()).isEqualTo(2021);
        assertThat(yearBalanceDTO.getId()).isEqualTo(ID);
        assertThat(yearBalanceDTO.getMonthBalances().size()).isEqualTo(2);
        assertThat(yearBalanceDTO.getBankBalances().size()).isEqualTo(1);
    }

    @Test
    @Order(1)
    void findYearBalanceByYear() throws Exception {
        final int year = 2019;
        MvcResult result = mockMvc
                .perform(get(YearBalanceController.BASE_URL + "?year=" + year).contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        int status = result.getResponse().getStatus();
        assertThat(status).isEqualTo(HttpStatus.OK.value());

        YearBalanceDTO yearBalanceDTO = new ObjectMapper()
                .readValue(result.getResponse().getContentAsString(), YearBalanceDTO.class);
        assertThat(yearBalanceDTO.getYear()).isEqualTo(year);
        assertThat(yearBalanceDTO.getId()).isEqualTo(2L);
        assertThat(yearBalanceDTO.getMonthBalances().size()).isEqualTo(1);
        assertThat(yearBalanceDTO.getBankBalances().size()).isEqualTo(0);
    }

    @Test
    @Order(2)
    void createNewYearBalance() throws Exception {
        int year = 2022;
        YearBalanceDTO yearBalanceDTO = YearBalanceDTO.builder().year(year).build();
        MvcResult result = mockMvc.perform(post(YearBalanceController.BASE_URL).contentType(MediaType.APPLICATION_JSON)
                                                                               .content(asJsonString(yearBalanceDTO)))
                                  .andReturn();
        int status = result.getResponse().getStatus();
        assertThat(status).isEqualTo(HttpStatus.CREATED.value());

        YearBalanceDTO yearBalanceDTOResult = new ObjectMapper()
                .readValue(result.getResponse().getContentAsString(), YearBalanceDTO.class);
        createdYearBalanceDtoId = yearBalanceDTOResult.getId();
        assertThat(yearBalanceDTOResult.getYear()).isEqualTo(year);
        assertThat(yearBalanceDTOResult.getId()).isNotNull();
        assertThat(yearBalanceDTOResult.getMonthBalances().size()).isEqualTo(0);
        assertThat(yearBalanceDTOResult.getBankBalances().size()).isEqualTo(0);
    }

    @Test
    @Order(3)
    void patchYearBalance() throws Exception {
        final int year = 2034;
        YearBalanceDTO yearBalanceDTO = YearBalanceDTO.builder().year(year).build();
        MvcResult result = mockMvc.perform(
                patch(YearBalanceController.BASE_URL + "/" + ID).contentType(MediaType.APPLICATION_JSON)
                                                                .content(asJsonString(yearBalanceDTO))).andReturn();
        int status = result.getResponse().getStatus();
        assertThat(status).isEqualTo(HttpStatus.OK.value());

        YearBalanceDTO yearBalanceDTOResult = new ObjectMapper()
                .readValue(result.getResponse().getContentAsString(), YearBalanceDTO.class);
        assertThat(yearBalanceDTOResult.getYear()).isEqualTo(year);
        assertThat(yearBalanceDTOResult.getId()).isEqualTo(ID);
        assertThat(yearBalanceDTOResult.getSalary()).isEqualTo("2200.00");
        assertThat(yearBalanceDTOResult.getIncomes()).isEqualTo("150.00");
        assertThat(yearBalanceDTOResult.getExpenses()).isEqualTo("100.00");
        assertThat(yearBalanceDTOResult.getResult()).isEqualTo("50.00");
        assertThat(yearBalanceDTOResult.getMonthBalances().size()).isEqualTo(2);
        assertThat(yearBalanceDTOResult.getBankBalances().size()).isEqualTo(1);
    }

    @Test
    @Order(4)
    void saveYearBalance() throws Exception {
        int year = 2023;
        YearBalanceDTO yearBalanceDTO = YearBalanceDTO.builder().year(year).build();
        MvcResult result = mockMvc.perform(put(YearBalanceController.BASE_URL + "/" + ID)
                .contentType(MediaType.APPLICATION_JSON).content(asJsonString(yearBalanceDTO))).andReturn();
        int status = result.getResponse().getStatus();
        assertThat(status).isEqualTo(HttpStatus.OK.value());

        YearBalanceDTO yearBalanceDTOResult = new ObjectMapper()
                .readValue(result.getResponse().getContentAsString(), YearBalanceDTO.class);
        assertThat(yearBalanceDTOResult.getYear()).isEqualTo(year);
        assertThat(yearBalanceDTOResult.getId()).isEqualTo(ID);
        assertThat(yearBalanceDTOResult.getMonthBalances().size()).isEqualTo(2);
        assertThat(yearBalanceDTOResult.getBankBalances().size()).isEqualTo(0);
    }

    @Test
    @Order(5)
    void deleteYearBalance() throws Exception {
        MvcResult result = mockMvc
                .perform(delete(YearBalanceController.BASE_URL + "/" + createdYearBalanceDtoId).contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        int status = result.getResponse().getStatus();
        assertThat(status).isEqualTo(HttpStatus.OK.value());
        YearBalanceDTO yearBalanceDTOResult = new ObjectMapper()
                .readValue(result.getResponse().getContentAsString(), YearBalanceDTO.class);
        assertThat(yearBalanceDTOResult.getId()).isEqualTo(createdYearBalanceDtoId);

        result =
                mockMvc.perform(get(YearBalanceController.BASE_URL + "/" + createdYearBalanceDtoId).contentType(MediaType.APPLICATION_JSON))
                        .andReturn();
        status = result.getResponse().getStatus();
        assertThat(status).isEqualTo(HttpStatus.NOT_FOUND.value());
    }
}
