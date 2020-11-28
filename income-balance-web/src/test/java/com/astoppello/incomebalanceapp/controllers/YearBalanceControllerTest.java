package com.astoppello.incomebalanceapp.controllers;

import com.astoppello.incomebalanceapp.dto.domain.YearBalanceDTO;
import com.astoppello.incomebalanceapp.services.YearBalanceService;
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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class YearBalanceControllerTest {

    private final int YEAR = 2002;
    private final Long ID = 1L;

    @InjectMocks
    YearBalanceController yearBalanceController;
    @Mock
    YearBalanceService yearBalanceService;
    MockMvc mockMvc;
    YearBalanceDTO yearBalanceDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(yearBalanceController)
                                 .build();
        yearBalanceDTO = new YearBalanceDTO();
        yearBalanceDTO.setId(ID);
        yearBalanceDTO.setYear(YEAR);
    }

    @Test
    void findAllYearBalance() throws Exception {
        List<YearBalanceDTO> yearBalances = List.of(new YearBalanceDTO(), new YearBalanceDTO());
        when(yearBalanceService.findAll()).thenReturn(yearBalances);
        mockMvc.perform(get(YearBalanceController.BASE_URL).contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.yearbalances", hasSize(2)));
    }

    @Test
    void findYearBalanceById() throws Exception {
        when(yearBalanceService.findById(anyLong())).thenReturn(yearBalanceDTO);
        mockMvc.perform(get(YearBalanceController.BASE_URL + "/" + ID).contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id", equalTo(1)));
    }

    @Test
    void findYearBalanceByYear() throws Exception {
        when(yearBalanceService.findYearBalanceByYear(anyInt())).thenReturn(yearBalanceDTO);
                mockMvc.perform(post(YearBalanceController.BASE_URL).contentType(MediaType.APPLICATION_JSON).content(AbstractRestControllerTest.asJsonString(YEAR)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.year", equalTo(YEAR)));
    }
}