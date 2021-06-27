package com.astoppello.incomebalanceapp.ITcontroller;

import com.astoppello.incomebalanceapp.controllers.AbstractRestControllerTest;
import com.astoppello.incomebalanceapp.controllers.BankController;
import com.astoppello.incomebalanceapp.dto.domain.BankDTO;
import com.astoppello.incomebalanceapp.dto.domain.BankSetDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

/**
 * Created by @author stopp on 19/06/2021
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BankControllerIT {

    @Autowired
    private MockMvc mockMvc;
    private final Long ID = 1L;
    private final String MEDIOLANUM = "Mediolanum";
    private final String REVOLUT = "Revolut";
    private final String BOURSORAMA = "Boursorama";
    private final String bankName = "bankName";
    private Long createdBankDTOId;
    private final BankDTO bankDTO = BankDTO.builder().name(bankName).build();

    @Test
    @Order(1)
    void findAllBanks() throws Exception {
        MvcResult result = mockMvc.perform(get(BankController.BASE_URL + "/").contentType(MediaType.APPLICATION_JSON))
                                  .andReturn();

        int status = result.getResponse().getStatus();
        assertThat(status).isEqualTo(HttpStatus.OK.value());

        BankSetDTO bankSetDTO = new ObjectMapper()
                .readValue(result.getResponse().getContentAsString(), BankSetDTO.class);
        assertThat(bankSetDTO.getBanks().size()).isEqualTo(3);
        assertTrue(bankSetDTO.getBanks().stream().map(BankDTO::getName).collect(Collectors.toSet())
                             .containsAll(Set.of(BOURSORAMA, MEDIOLANUM, REVOLUT)));
    }

    @Test
    @Order(1)
    void findBankById() throws Exception {
        MvcResult result = mockMvc
                .perform(get(BankController.BASE_URL + "/" + ID).contentType(MediaType.APPLICATION_JSON)).andReturn();

        int status = result.getResponse().getStatus();
        assertThat(status).isEqualTo(HttpStatus.OK.value());

        BankDTO bankDTO = new ObjectMapper().readValue(result.getResponse().getContentAsString(), BankDTO.class);
        assertThat(bankDTO.getId()).isEqualTo(ID);
        assertThat(bankDTO.getName()).isEqualTo(REVOLUT);
    }

    @Test
    @Order(1)
    void findBankByName() throws Exception {
        MvcResult result = mockMvc
                .perform(get(BankController.BASE_URL + "?name=" + BOURSORAMA).contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        int status = result.getResponse().getStatus();
        assertThat(status).isEqualTo(HttpStatus.OK.value());

        BankDTO bankDTO = new ObjectMapper().readValue(result.getResponse().getContentAsString(), BankDTO.class);
        assertThat(bankDTO.getId()).isEqualTo(3L);
        assertThat(bankDTO.getName()).isEqualTo(BOURSORAMA);
    }

    @Test
    @Order(2)
    void createNewBank() throws Exception {
        MvcResult result = mockMvc.perform(post(BankController.BASE_URL).contentType(MediaType.APPLICATION_JSON)
                                                                        .content(AbstractRestControllerTest
                                                                                .asJsonString(bankDTO))).andReturn();

        int status = result.getResponse().getStatus();
        assertThat(status).isEqualTo(HttpStatus.CREATED.value());

        BankDTO bankDTOResult = new ObjectMapper().readValue(result.getResponse().getContentAsString(), BankDTO.class);
        createdBankDTOId = bankDTOResult.getId();
        assertThat(bankDTOResult.getId()).isNotNull();
        assertThat(bankDTOResult.getName()).isEqualTo(bankName);
    }

    @Test
    @Order(3)
    void saveBank() throws Exception {
        MvcResult result = mockMvc.perform(
                put(BankController.BASE_URL + "/" + createdBankDTOId).contentType(MediaType.APPLICATION_JSON).content(
                        AbstractRestControllerTest.asJsonString(bankDTO))).andReturn();

        int status = result.getResponse().getStatus();
        assertThat(status).isEqualTo(HttpStatus.OK.value());

        BankDTO bankDTOResult = new ObjectMapper().readValue(result.getResponse().getContentAsString(), BankDTO.class);
        assertThat(bankDTOResult.getId()).isEqualTo(createdBankDTOId);
        assertThat(bankDTOResult.getName()).isEqualTo(bankName);
    }

    @Test
    @Order(4)
    void updateBank() throws Exception {
        MvcResult result = mockMvc.perform(
                put(BankController.BASE_URL + "/" + ID).contentType(MediaType.APPLICATION_JSON)
                                                       .content(AbstractRestControllerTest.asJsonString(bankDTO)))
                                  .andReturn();

        int status = result.getResponse().getStatus();
        assertThat(status).isEqualTo(HttpStatus.OK.value());

        BankDTO bankDTOResult = new ObjectMapper().readValue(result.getResponse().getContentAsString(), BankDTO.class);
        assertThat(bankDTOResult.getId()).isEqualTo(ID);
        assertThat(bankDTOResult.getName()).isEqualTo(bankName);
    }

    @Test
    @Order(5)
    void deleteBank() throws Exception {
        MvcResult result = mockMvc.perform(
                delete(BankController.BASE_URL + "/" + createdBankDTOId).contentType(MediaType.APPLICATION_JSON))
                                  .andReturn();

        int status = result.getResponse().getStatus();
        BankDTO bankDTOResult = new ObjectMapper().readValue(result.getResponse().getContentAsString(), BankDTO.class);
        assertThat(status).isEqualTo(HttpStatus.OK.value());
        assertThat(bankDTOResult.getId()).isEqualTo(createdBankDTOId);
        assertThat(bankDTOResult.getName()).isEqualTo(bankName);

        MvcResult resultGet = mockMvc
                .perform(get(BankController.BASE_URL + "/" + createdBankDTOId).contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertThat(resultGet.getResponse().getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }
}
