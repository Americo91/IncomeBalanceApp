package com.astoppello.incomebalanceapp.controllers;

import com.astoppello.incomebalanceapp.dto.domain.BankDTO;
import com.astoppello.incomebalanceapp.services.BankService;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BankControllerTest {

  private static final Long ID = 1L;
  private static final String NAME = "Revolut";
  @InjectMocks BankController bankController;
  @Mock BankService bankService;
  MockMvc mockMvc;
  BankDTO bankDTO;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    mockMvc = MockMvcBuilders.standaloneSetup(bankController).build();
    bankDTO = new BankDTO();
    bankDTO.setName(NAME);
    bankDTO.setId(ID);
  }

  @Test
  void findAllBanks() throws Exception {
    List<BankDTO> bankDTOS = List.of(new BankDTO(), new BankDTO());
    when(bankService.findAll()).thenReturn(bankDTOS);
    mockMvc
        .perform(get(BankController.BASE_URL).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.banks", hasSize(2)));
  }

  @Test
  void findBankById() throws Exception {
    when(bankService.findById(anyLong())).thenReturn(bankDTO);
    mockMvc
        .perform(get(BankController.BASE_URL + "/" + ID).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", equalTo(1)));
  }

  /*
  @Test
  void findBankByName() throws Exception {
    when(bankService.findBankByName(anyString())).thenReturn(bankDTO);
    mockMvc
        .perform(
            post(BankController.BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(AbstractRestControllerTest.asJsonString(NAME)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name", equalTo(NAME)));
  }
  */

  @Test
  void createNewBank() throws Exception {
    when(bankService.createNewBank(any(BankDTO.class))).thenReturn(bankDTO);
    mockMvc
        .perform(
            post(BankController.BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(AbstractRestControllerTest.asJsonString(bankDTO)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id", equalTo(1)))
        .andExpect(jsonPath("$.name", equalTo(NAME)));
  }
}
