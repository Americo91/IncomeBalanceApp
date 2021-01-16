package com.astoppello.incomebalanceapp.controllers;

import com.astoppello.incomebalanceapp.dto.domain.BankDTO;
import com.astoppello.incomebalanceapp.exceptions.ResourceNotFoundException;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    mockMvc =
        MockMvcBuilders.standaloneSetup(bankController)
            .setControllerAdvice(new RestResponseEntityExceptionHanlder())
            .build();
    bankDTO = new BankDTO();
    bankDTO.setName(NAME);
    bankDTO.setId(ID);
  }

  @Test
  void findAllBanks() throws Exception {
    List<BankDTO> bankDTOS = List.of(new BankDTO(), new BankDTO());
    when(bankService.findAll()).thenReturn(bankDTOS);
    mockMvc
        .perform(get(BankController.BASE_URL + "/").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.banks", hasSize(2)));
    verify(bankService).findAll();
  }

  @Test
  void findBankById() throws Exception {
    when(bankService.findById(anyLong())).thenReturn(bankDTO);
    mockMvc
        .perform(get(BankController.BASE_URL + "/" + ID).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", equalTo(1)));
    verify(bankService).findById(anyLong());
  }

  @Test
  void findBankByName() throws Exception {
    when(bankService.findBankByName(anyString())).thenReturn(bankDTO);
    mockMvc
        .perform(
            get(BankController.BASE_URL + "?name=" + NAME).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name", equalTo(NAME)));
    verify(bankService).findBankByName(anyString());
  }

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
    verify(bankService).createNewBank(any());
  }

  @Test
  void saveBankById() throws Exception {
    when(bankService.saveBankById(anyLong(), any(BankDTO.class))).thenReturn(bankDTO);
    mockMvc
        .perform(
            put(BankController.BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(AbstractRestControllerTest.asJsonString(bankDTO)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", equalTo(1)))
        .andExpect(jsonPath("$.name", equalTo(NAME)));
    verify(bankService).saveBankById(anyLong(), any());
  }

  @Test
  void updateBank() throws Exception {
    when(bankService.updateBank(anyLong(), any(BankDTO.class))).thenReturn(bankDTO);
    mockMvc
        .perform(
            patch(BankController.BASE_URL + "/1")
                .content(AbstractRestControllerTest.asJsonString(bankDTO))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", equalTo(1)))
        .andExpect(jsonPath("$.name", equalTo(NAME)));
    verify(bankService).updateBank(anyLong(), any());
  }

  @Test
  void deleteBank() throws Exception {
    mockMvc
        .perform(delete(BankController.BASE_URL + "/1").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
    verify(bankService).deleteBank(anyLong());
  }

  @Test
  void testNotFoundException() throws Exception {
    when(bankService.findById(anyLong())).thenThrow(ResourceNotFoundException.class);
    mockMvc.perform(get(BankController.BASE_URL + "/222").contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isNotFound());
  }
}
