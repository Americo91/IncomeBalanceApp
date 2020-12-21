package com.astoppello.incomebalanceapp.controllers;

import com.astoppello.incomebalanceapp.dto.domain.MonthBalanceDTO;
import com.astoppello.incomebalanceapp.services.MonthBalanceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MonthBalanceControllerTest {

    private static final Long ID = 1L;
    private static final String MONTH = "September";

    @InjectMocks
    MonthBalanceController monthBalanceController;
    @Mock
    MonthBalanceService monthBalanceService;
    MockMvc mockMvc;
    MonthBalanceDTO monthBalanceDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(monthBalanceController).build();
        monthBalanceDTO = new MonthBalanceDTO();
        monthBalanceDTO.setId(ID);
        monthBalanceDTO.setMonth(MONTH);
    }

    @Test
    void findAllMonthBalance() throws Exception {
        List<MonthBalanceDTO> monthBalanceDTOS = List.of(new MonthBalanceDTO(), new MonthBalanceDTO());
        when(monthBalanceService.findAll()).thenReturn(monthBalanceDTOS);
        mockMvc.perform(get(MonthBalanceController.BASE_URL).contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.monthbalances", hasSize(2)));
    }

    @Test
    void findMonthBalanceById() throws Exception {
        when(monthBalanceService.findById(anyLong())).thenReturn(monthBalanceDTO);
        mockMvc.perform(get(MonthBalanceController.BASE_URL + "/" + ID).contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id", equalTo(1)));
    }

    @Test
    void findMonthBalanceByMonth() throws Exception {
        when(monthBalanceService.findByMonth(anyString())).thenReturn(monthBalanceDTO);
        mockMvc.perform(post(MonthBalanceController.BASE_URL).contentType(MediaType.APPLICATION_JSON)
                                                             .content(AbstractRestControllerTest.asJsonString(MONTH)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.month", equalTo(MONTH)));
    }
}