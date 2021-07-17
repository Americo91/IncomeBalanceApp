package com.astoppello.incomebalanceapp.ITcontroller;

import com.astoppello.incomebalanceapp.controllers.AbstractRestControllerTest;
import com.astoppello.incomebalanceapp.controllers.BankBalanceController;
import com.astoppello.incomebalanceapp.controllers.YearBalanceController;
import com.astoppello.incomebalanceapp.dto.domain.*;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

/**
 * Created by @author stopp on 31/05/2021
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BankBalanceControllerIT {

    @Autowired
    private MockMvc mockMvc;
    private Long ID = 1L;
    private Long createdBankBalanceDTOId;
    BankBalanceDTO createdBankBalanceDTO;

    @BeforeAll
    void setup() {
        createdBankBalanceDTO = BankBalanceDTO
                .builder()
                .bank(BankDTO.builder().name("bank").build())
                .incomes(new BigDecimal("0.00"))
                .expenses(new BigDecimal("200"))
                .yearBalanceId(ID)
                .monthBalanceId(ID)
                .build();
    }

    @Test
    @Order(1)
    void createBankBalanceTest() throws Exception {
        /* Test Failure case */
        MvcResult resultFailure = mockMvc
                .perform(post(YearBalanceController.BASE_URL + "/" + ID + "/monthBalances/" + 100 + "/bankBalances")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(AbstractRestControllerTest.asJsonString(createdBankBalanceDTO)))
                .andReturn();
        int status = resultFailure.getResponse().getStatus();
        assertThat(status).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(resultFailure.getResolvedException().getMessage())
                .isEqualTo("MonthBalance 100 is not linked to " + "YearBalance 1");

        /* Test working case */
        MvcResult result = mockMvc
                .perform(post(YearBalanceController.BASE_URL + "/" + ID + "/monthBalances/" + ID + "/bankBalances")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(AbstractRestControllerTest.asJsonString(createdBankBalanceDTO)))
                .andReturn();

        status = result.getResponse().getStatus();
        assertThat(status).isEqualTo(HttpStatus.CREATED.value());

        BankBalanceDTO bankBalanceDTOResult = new ObjectMapper().readValue(result.getResponse().getContentAsString(),
                BankBalanceDTO.class);

        assertThat(bankBalanceDTOResult.getId()).isNotNull();
        createdBankBalanceDTOId = bankBalanceDTOResult.getId();
        assertThat(bankBalanceDTOResult.getExpenses()).isEqualTo("200");
        assertThat(bankBalanceDTOResult.getYearBalanceId()).isEqualTo(ID);
        assertThat(bankBalanceDTOResult.getMonthBalanceId()).isEqualTo(ID);
        assertThat(bankBalanceDTOResult.getBank().getName()).isEqualTo("bank");
    }

    @Test
    @Order(2)
    void updateBankBalance() throws Exception {
        /* Modifying expenses. Other variable should be the same */
        BankBalanceDTO bankBalanceDTO = BankBalanceDTO.builder().expenses(new BigDecimal("150")).build();
        MvcResult result = mockMvc
                .perform(patch(BankBalanceController.BASE_URL + "/" + createdBankBalanceDTOId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(AbstractRestControllerTest.asJsonString(bankBalanceDTO)))
                .andReturn();

        int status = result.getResponse().getStatus();
        assertThat(status).isEqualTo(HttpStatus.OK.value());

        BankBalanceDTO bankBalanceDTOResult = new ObjectMapper().readValue(result.getResponse().getContentAsString(),
                BankBalanceDTO.class);
        assertThat(bankBalanceDTOResult.getId()).isEqualTo(createdBankBalanceDTOId);
        assertThat(bankBalanceDTOResult.getExpenses()).isEqualTo("150");
        assertThat(bankBalanceDTOResult.getResult()).isEqualTo("-150.00");
        assertThat(bankBalanceDTOResult.getIncomes()).isEqualTo(createdBankBalanceDTO.getIncomes());
        assertThat(bankBalanceDTOResult.getBank().getName()).isEqualTo(createdBankBalanceDTO.getBank().getName());
        assertThat(bankBalanceDTOResult.getYearBalanceId()).isEqualTo(createdBankBalanceDTO.getYearBalanceId());
        assertThat(bankBalanceDTOResult.getMonthBalanceId()).isEqualTo(createdBankBalanceDTO.getMonthBalanceId());
    }

    @Test
    @Order(3)
    void updateBankBalanceChangingYearBalance() throws Exception {
        /* Modifying yearBalanceId and monthBalanceId */
        final long newYearBalanceId = 2L;
        MvcResult result = mockMvc
                .perform(patch(BankBalanceController.BASE_URL + "/" + createdBankBalanceDTOId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(AbstractRestControllerTest.asJsonString(BankBalanceDTO
                                .builder()
                                .yearBalanceId(newYearBalanceId)
                                .monthBalanceId(newYearBalanceId))))
                .andReturn();

        int status = result.getResponse().getStatus();
        assertThat(status).isEqualTo(HttpStatus.OK.value());

        BankBalanceDTO bankBalanceDTOResult = new ObjectMapper().readValue(result.getResponse().getContentAsString(),
                BankBalanceDTO.class);
        assertThat(bankBalanceDTOResult.getId()).isEqualTo(createdBankBalanceDTOId);
        assertThat(bankBalanceDTOResult.getIncomes()).isEqualTo(createdBankBalanceDTO.getIncomes());
        assertThat(bankBalanceDTOResult.getExpenses()).isEqualTo("150.00");
        assertThat(bankBalanceDTOResult.getResult()).isEqualTo("-150.00");
        assertThat(bankBalanceDTOResult.getBank().getName()).isEqualTo(createdBankBalanceDTO.getBank().getName());
        assertThat(bankBalanceDTOResult.getYearBalanceId()).isEqualTo(newYearBalanceId);
        assertThat(bankBalanceDTOResult.getMonthBalanceId()).isEqualTo(newYearBalanceId);


        MvcResult resultGetNewYearBalance = mockMvc
                .perform(get(YearBalanceController.BASE_URL + "/" + newYearBalanceId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        YearBalanceDTO newYearBalance = new ObjectMapper().readValue(
                resultGetNewYearBalance.getResponse().getContentAsString(), YearBalanceDTO.class);
        assertThat(newYearBalance.getId()).isEqualTo(newYearBalanceId);
        assertThat(newYearBalance.getIncomes()).isEqualTo(bankBalanceDTOResult.getIncomes());
        assertThat(newYearBalance.getExpenses()).isEqualTo(bankBalanceDTOResult.getExpenses());
        assertThat(newYearBalance.getResult()).isEqualTo(bankBalanceDTOResult.getResult());
        MonthBalanceDTO monthBalanceDTO = newYearBalance.getMonthBalances().iterator().next();
        assertThat(monthBalanceDTO.getExpenses()).isEqualTo(bankBalanceDTOResult.getExpenses());
        assertThat(monthBalanceDTO.getIncomes()).isEqualTo(bankBalanceDTOResult.getIncomes());
        assertThat(monthBalanceDTO.getExpenses()).isEqualTo(bankBalanceDTOResult.getExpenses());
        assertThat(monthBalanceDTO.getId()).isEqualTo(newYearBalanceId);
        assertThat(newYearBalance.getBankBalances().size()).isEqualTo(1);
        assertThat(monthBalanceDTO.getBankBalances().size()).isEqualTo(1);

        MvcResult resultGetYearBalanceOriginalAfterPatch = mockMvc
                .perform(get(YearBalanceController.BASE_URL + "/" + ID).contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        YearBalanceDTO yearBalanceOriginalAfterPatch = new ObjectMapper().readValue(
                resultGetYearBalanceOriginalAfterPatch.getResponse().getContentAsString(), YearBalanceDTO.class);
        assertThat(yearBalanceOriginalAfterPatch.getId()).isEqualTo(ID);
        assertThat(yearBalanceOriginalAfterPatch.getIncomes()).isEqualTo("0.00");
        assertThat(yearBalanceOriginalAfterPatch.getExpenses()).isEqualTo("0.00");
        assertThat(yearBalanceOriginalAfterPatch.getResult()).isEqualTo("0.00");
        MonthBalanceDTO monthBalanceDTOOriginal = yearBalanceOriginalAfterPatch.getMonthBalances().iterator().next();
        assertThat(monthBalanceDTOOriginal.getExpenses()).isEqualTo("0.00");
        assertThat(monthBalanceDTOOriginal.getIncomes()).isEqualTo("0.00");
        assertThat(monthBalanceDTOOriginal.getExpenses()).isEqualTo("0.00");
        assertThat(monthBalanceDTOOriginal.getId()).isEqualTo(ID);
        assertThat(yearBalanceOriginalAfterPatch.getBankBalances().size()).isEqualTo(0);
        assertThat(monthBalanceDTOOriginal.getBankBalances().size()).isEqualTo(0);
    }

    @Test
    @Order(4)
    void saveBankBalance() throws Exception {
        /* Modifying expenses and bankName  */
        final String updatedBankName = "revolut";
        BankBalanceDTO bankBalanceDTO = BankBalanceDTO
                .builder()
                .bank(BankDTO.builder().name(updatedBankName).build())
                .expenses(new BigDecimal("500"))
                .yearBalanceId(ID)
                .monthBalanceId(ID)
                .build();
        MvcResult result = mockMvc
                .perform(put(BankBalanceController.BASE_URL + "/" + createdBankBalanceDTOId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(AbstractRestControllerTest.asJsonString(bankBalanceDTO)))
                .andReturn();

        int status = result.getResponse().getStatus();
        assertThat(status).isEqualTo(HttpStatus.OK.value());

        BankBalanceDTO bankBalanceDTOResult = new ObjectMapper().readValue(result.getResponse().getContentAsString(),
                BankBalanceDTO.class);
        assertThat(bankBalanceDTOResult.getId()).isEqualTo(createdBankBalanceDTOId);
        assertThat(bankBalanceDTOResult.getExpenses()).isEqualTo("500");
        assertThat(bankBalanceDTOResult.getResult()).isEqualTo("-500");
        assertThat(bankBalanceDTOResult.getIncomes()).isEqualTo("0");
        assertThat(bankBalanceDTOResult.getBank().getName()).isEqualTo(updatedBankName);
        assertThat(bankBalanceDTOResult.getYearBalanceId()).isEqualTo(createdBankBalanceDTO.getYearBalanceId());
        assertThat(bankBalanceDTOResult.getMonthBalanceId()).isEqualTo(createdBankBalanceDTO.getMonthBalanceId());
    }

    @Test
    @Order(5)
    void findAllBankBalancesByIds() throws Exception {
        /* Test Failure case */
        MvcResult resultFailure = mockMvc
                .perform(get(YearBalanceController.BASE_URL + "/" + 3 + "/monthBalances/" + 100 + "/bankBalances/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        int status = resultFailure.getResponse().getStatus();
        assertThat(status).isEqualTo(HttpStatus.OK.value());
        BankBalanceSetDTO bankBalanceSetDTO = new ObjectMapper().readValue(
                resultFailure.getResponse().getContentAsString(), BankBalanceSetDTO.class);
        assertThat(bankBalanceSetDTO.getBankBalances().size()).isEqualTo(0);

        /* Test Working case */
        MvcResult result = mockMvc
                .perform(get(YearBalanceController.BASE_URL + "/" + 3 + "/monthBalances/" + 3 + "/bankBalances/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        status = result.getResponse().getStatus();
        assertThat(status).isEqualTo(HttpStatus.OK.value());

        bankBalanceSetDTO = new ObjectMapper().readValue(result.getResponse().getContentAsString(),
                BankBalanceSetDTO.class);
        assertThat(bankBalanceSetDTO).isNotNull();
        assertThat(bankBalanceSetDTO.getBankBalances()).isNotNull();
        assertThat(bankBalanceSetDTO.getBankBalances().size()).isEqualTo(1);

        BankBalanceDTO bankBalanceDTO = bankBalanceSetDTO.getBankBalances().iterator().next();
        assertThat(bankBalanceDTO.getId()).isEqualTo(ID);
        assertThat(bankBalanceDTO.getIncomes()).isEqualTo("150.00");
        assertThat(bankBalanceDTO.getExpenses()).isEqualTo("100.00");
        assertThat(bankBalanceDTO.getMonthBalanceId()).isEqualTo(3);
        assertThat(bankBalanceDTO.getYearBalanceId()).isEqualTo(3);
        assertThat(bankBalanceDTO.getBank().getId()).isEqualTo(1);
        assertThat(bankBalanceDTO.getBank().getName()).isEqualTo("Revolut");
    }

    @Test
    @Order(6)
    void deleteBankBalance() throws Exception {
        MvcResult resultDeleteFailing = mockMvc
                .perform(delete(BankBalanceController.BASE_URL + "/" + 100).contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        int statusFailure = resultDeleteFailing.getResponse().getStatus();
        assertThat(statusFailure).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(resultDeleteFailing.getResolvedException().getMessage()).isEqualTo("BankBalance not found: 100");

        MvcResult resultDelete = mockMvc
                .perform(delete(BankBalanceController.BASE_URL + "/" + createdBankBalanceDTOId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        BankBalanceDTO bankBalanceDTO = new ObjectMapper().readValue(resultDelete.getResponse().getContentAsString(),
                BankBalanceDTO.class);
        long originalYearBalanceId = bankBalanceDTO.getYearBalanceId();

        MvcResult resultGet = mockMvc
                .perform(get(BankBalanceController.BASE_URL + "/" + createdBankBalanceDTOId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        int status = resultGet.getResponse().getStatus();
        assertThat(status).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(bankBalanceDTO.getId()).isEqualTo(createdBankBalanceDTOId);

        MvcResult resultYearBalanceAfterDelete = mockMvc
                .perform(get(YearBalanceController.BASE_URL + "/" + originalYearBalanceId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        YearBalanceDTO yearBalanceAfterDelete = new ObjectMapper().readValue(
                resultYearBalanceAfterDelete.getResponse().getContentAsString(), YearBalanceDTO.class);
        assertThat(yearBalanceAfterDelete.getId()).isEqualTo(originalYearBalanceId);
        assertThat(yearBalanceAfterDelete.getIncomes()).isEqualTo("0.00");
        assertThat(yearBalanceAfterDelete.getExpenses()).isEqualTo("0.00");
        assertThat(yearBalanceAfterDelete.getResult()).isEqualTo("0.00");
        MonthBalanceDTO monthBalanceDTO = yearBalanceAfterDelete.getMonthBalances().iterator().next();
        assertThat(monthBalanceDTO.getExpenses()).isEqualTo("0.00");
        assertThat(monthBalanceDTO.getIncomes()).isEqualTo("0.00");
        assertThat(monthBalanceDTO.getExpenses()).isEqualTo("0.00");
        assertThat(monthBalanceDTO.getId()).isEqualTo(originalYearBalanceId);
        assertThat(yearBalanceAfterDelete.getBankBalances().size()).isEqualTo(0);
        assertThat(monthBalanceDTO.getBankBalances().size()).isEqualTo(0);
    }

    @Test
    @Order(7)
    void findAllBankBalancesByYearBalanceId() throws Exception {
        /* Test Failure case */
        MvcResult resultFailure = mockMvc
                .perform(get(YearBalanceController.BASE_URL + "/" + 100 + "/bankBalances/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        int status = resultFailure.getResponse().getStatus();
        assertThat(status).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(resultFailure.getResolvedException().getMessage()).isEqualTo("YearBalance not found: 100");

        /* Test Working case */
        MvcResult result = mockMvc
                .perform(get(YearBalanceController.BASE_URL + "/" + 3 + "/bankBalances/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        status = result.getResponse().getStatus();
        assertThat(status).isEqualTo(HttpStatus.OK.value());

        BankBalanceSetDTO bankBalanceSetDTO = new ObjectMapper().readValue(result.getResponse().getContentAsString(),
                BankBalanceSetDTO.class);
        assertThat(bankBalanceSetDTO).isNotNull();
        assertThat(bankBalanceSetDTO.getBankBalances()).isNotNull();
        assertThat(bankBalanceSetDTO.getBankBalances().size()).isEqualTo(1);

        BankBalanceDTO bankBalanceDTO = bankBalanceSetDTO.getBankBalances().iterator().next();
        assertThat(bankBalanceDTO).isNotNull();
        assertThat(bankBalanceDTO.getId()).isEqualTo(1);
        assertThat(bankBalanceDTO.getIncomes()).isEqualTo("150.00");
        assertThat(bankBalanceDTO.getExpenses()).isEqualTo("100.00");
        assertThat(bankBalanceDTO.getMonthBalanceId()).isEqualTo(3);
        assertThat(bankBalanceDTO.getYearBalanceId()).isEqualTo(3);
        assertThat(bankBalanceDTO.getBank()).isNotNull();
        assertThat(bankBalanceDTO.getBank().getId()).isEqualTo(1);
        assertThat(bankBalanceDTO.getBank().getName()).isEqualTo("Revolut");
    }

    @Test
    @Order(7)
    void findAllBankBalancesTest() throws Exception {
        MvcResult result = mockMvc
                .perform(get(BankBalanceController.BASE_URL + "/").contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        int status = result.getResponse().getStatus();
        assertThat(status).isEqualTo(HttpStatus.OK.value());

        BankBalanceSetDTO bankBalanceSetDTO = new ObjectMapper().readValue(result.getResponse().getContentAsString(),
                BankBalanceSetDTO.class);
        assertThat(bankBalanceSetDTO).isNotNull();
        assertThat(bankBalanceSetDTO.getBankBalances()).isNotNull();
        assertThat(bankBalanceSetDTO.getBankBalances().size()).isEqualTo(1);

        BankBalanceDTO bankBalanceDTO = bankBalanceSetDTO.getBankBalances().iterator().next();
        assertThat(bankBalanceDTO.getId()).isEqualTo(1);
        assertThat(bankBalanceDTO.getIncomes()).isEqualTo("150.00");
        assertThat(bankBalanceDTO.getExpenses()).isEqualTo("100.00");
        assertThat(bankBalanceDTO.getMonthBalanceId()).isEqualTo(3);
        assertThat(bankBalanceDTO.getYearBalanceId()).isEqualTo(3);
        assertThat(bankBalanceDTO.getBank().getId()).isEqualTo(1);
        assertThat(bankBalanceDTO.getBank().getName()).isEqualTo("Revolut");
    }

    @Test
    @Order(8)
    void findBankBalanceByIdTest() throws Exception {
        /* Test Failure case */
        MvcResult resultFailure = mockMvc
                .perform(get(BankBalanceController.BASE_URL + "/" + 100).contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        int status = resultFailure.getResponse().getStatus();
        assertThat(status).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(resultFailure.getResolvedException().getMessage()).isEqualTo("BankBalance not found: 100");

        /* Test Working case */
        MvcResult result = mockMvc
                .perform(get(BankBalanceController.BASE_URL + "/" + ID).contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        status = result.getResponse().getStatus();
        assertThat(status).isEqualTo(HttpStatus.OK.value());

        String actualResponse = result.getResponse().getContentAsString();
        BankBalanceDTO bankBalanceDTO = new ObjectMapper().readValue(actualResponse, BankBalanceDTO.class);

        assertThat(bankBalanceDTO.getId()).isEqualTo(1);
        assertThat(bankBalanceDTO.getIncomes()).isEqualTo("150.00");
        assertThat(bankBalanceDTO.getExpenses()).isEqualTo("100.00");
        assertThat(bankBalanceDTO.getMonthBalanceId()).isEqualTo(3);
        assertThat(bankBalanceDTO.getYearBalanceId()).isEqualTo(3);
        assertThat(bankBalanceDTO.getBank().getId()).isEqualTo(1);
        assertThat(bankBalanceDTO.getBank().getName()).isEqualTo("Revolut");
    }

    @Test
    @Order(9)
    void findBankBalanceByName() throws Exception {
        /* Test Failure case */
        String missingBankName = "missingBankName";
        MvcResult resultFailure = mockMvc
                .perform(get(BankBalanceController.BASE_URL + "?bankName=" + missingBankName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        int status = resultFailure.getResponse().getStatus();
        assertThat(status).isEqualTo(HttpStatus.OK.value());
        BankBalanceSetDTO bankBalanceSetDTOFailure = new ObjectMapper().readValue(
                resultFailure.getResponse().getContentAsString(), BankBalanceSetDTO.class);
        assertThat(bankBalanceSetDTOFailure).isNotNull();
        assertThat(bankBalanceSetDTOFailure.getBankBalances()).isNotNull();
        assertThat(bankBalanceSetDTOFailure.getBankBalances().size()).isEqualTo(0);

        /* Test Working case */
        String bankName = "Revolut";
        MvcResult result = mockMvc
                .perform(get(BankBalanceController.BASE_URL + "?bankName=" + bankName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        status = result.getResponse().getStatus();
        assertThat(status).isEqualTo(HttpStatus.OK.value());

        BankBalanceSetDTO bankBalanceSetDTO = new ObjectMapper().readValue(result.getResponse().getContentAsString(),
                BankBalanceSetDTO.class);
        assertThat(bankBalanceSetDTO).isNotNull();
        assertThat(bankBalanceSetDTO.getBankBalances()).isNotNull();
        assertThat(bankBalanceSetDTO.getBankBalances().size()).isEqualTo(1);
        assertThat(bankBalanceSetDTO.getBankBalances().iterator().next().getBank().getName()).isEqualTo(bankName);
    }


}
