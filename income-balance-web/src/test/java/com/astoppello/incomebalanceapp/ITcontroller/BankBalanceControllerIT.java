package com.astoppello.incomebalanceapp.ITcontroller;

import com.astoppello.incomebalanceapp.controllers.AbstractRestControllerTest;
import com.astoppello.incomebalanceapp.controllers.BankBalanceController;
import com.astoppello.incomebalanceapp.controllers.YearBalanceController;
import com.astoppello.incomebalanceapp.dto.domain.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

/**
 * Created by @author stopp on 31/05/2021
 */
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BankBalanceControllerIT {

    @Autowired
    private MockMvc mockMvc;
    private Long ID = 1L;

    @Test
    void createBankBalanceTest() throws Exception {
        BankBalanceDTO bankBalanceDTO = BankBalanceDTO.builder().bank(BankDTO.builder().name("bank").build())
                                                      .expenses("200").yearBalanceId(ID).monthBalanceId(ID).build();
        /* Test Failure case */
        MvcResult resultFailure = mockMvc.perform(
                post(YearBalanceController.BASE_URL + "/" + ID + "/monthBalances/" + 100 + "/bankBalances")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(AbstractRestControllerTest.asJsonString(bankBalanceDTO))).andReturn();
        int status = resultFailure.getResponse().getStatus();
        assertThat(status).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(resultFailure.getResolvedException().getMessage())
                .isEqualTo("MonthBalance 100 is not linked to " + "YearBalance 1");

        /* Test working case */
        MvcResult result = mockMvc.perform(
                post(YearBalanceController.BASE_URL + "/" + ID + "/monthBalances/" + ID + "/bankBalances")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(AbstractRestControllerTest.asJsonString(bankBalanceDTO))).andReturn();

        status = result.getResponse().getStatus();
        assertThat(status).isEqualTo(HttpStatus.CREATED.value());

        BankBalanceDTO bankBalanceDTOResult = new ObjectMapper()
                .readValue(result.getResponse().getContentAsString(), BankBalanceDTO.class);

        assertThat(bankBalanceDTOResult).isNotNull();
        assertThat(bankBalanceDTOResult.getExpenses()).isEqualTo("200");
        assertThat(bankBalanceDTOResult.getYearBalanceId()).isEqualTo(ID);
        assertThat(bankBalanceDTOResult.getMonthBalanceId()).isEqualTo(ID);
        assertThat(bankBalanceDTOResult.getBank()).isNotNull();
        assertThat(bankBalanceDTOResult.getBank().getName()).isEqualTo("bank");
    }

    @Test
    void findAllBankBalancesByIds() throws Exception {
        /* Test Failure case */
        MvcResult resultFailure = mockMvc.perform(
                get(YearBalanceController.BASE_URL + "/" + 3 + "/monthBalances/" + 100 + "/bankBalances/")
                        .contentType(MediaType.APPLICATION_JSON)).andReturn();
        int status = resultFailure.getResponse().getStatus();
        assertThat(status).isEqualTo(HttpStatus.OK.value());
        BankBalanceSetDTO bankBalanceSetDTO = new ObjectMapper()
                .readValue(resultFailure.getResponse().getContentAsString(), BankBalanceSetDTO.class);
        assertThat(bankBalanceSetDTO.getBankBalances().size()).isEqualTo(0);

        /* Test Working case */
        MvcResult result = mockMvc.perform(
                get(YearBalanceController.BASE_URL + "/" + 3 + "/monthBalances/" + 3 + "/bankBalances/")
                        .contentType(MediaType.APPLICATION_JSON)).andReturn();

        status = result.getResponse().getStatus();
        assertThat(status).isEqualTo(HttpStatus.OK.value());

        bankBalanceSetDTO = new ObjectMapper()
                .readValue(result.getResponse().getContentAsString(), BankBalanceSetDTO.class);
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
    void findAllBankBalancesByYearBalanceId() throws Exception {
        /* Test Failure case */
        MvcResult resultFailure = mockMvc.perform(get(YearBalanceController.BASE_URL + "/" + 100 + "/bankBalances/")
                .contentType(MediaType.APPLICATION_JSON)).andReturn();
        int status = resultFailure.getResponse().getStatus();
        assertThat(status).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(resultFailure.getResolvedException().getMessage()).isEqualTo("YearBalance not found: 100");

        /* Test Working case */
        MvcResult result = mockMvc.perform(get(YearBalanceController.BASE_URL + "/" + 3 + "/bankBalances/")
                .contentType(MediaType.APPLICATION_JSON)).andReturn();

        status = result.getResponse().getStatus();
        assertThat(status).isEqualTo(HttpStatus.OK.value());

        BankBalanceSetDTO bankBalanceSetDTO = new ObjectMapper()
                .readValue(result.getResponse().getContentAsString(), BankBalanceSetDTO.class);
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
    void getAllBankBalancesTest() throws Exception {
        MvcResult result = mockMvc
                .perform(get(BankBalanceController.BASE_URL + "/").contentType(MediaType.APPLICATION_JSON)).andReturn();

        int status = result.getResponse().getStatus();
        assertThat(status).isEqualTo(HttpStatus.OK.value());

        BankBalanceSetDTO bankBalanceSetDTO = new ObjectMapper()
                .readValue(result.getResponse().getContentAsString(), BankBalanceSetDTO.class);
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
    void getBankBalanceByIdTest() throws Exception {
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
    void findBankBalanceByName() throws Exception {
        /* Test Failure case */
        String missingBankName = "missingBankName";
        MvcResult resultFailure = mockMvc.perform(get(BankBalanceController.BASE_URL + "?bankName=" + missingBankName)
                .contentType(MediaType.APPLICATION_JSON)).andReturn();
        int status = resultFailure.getResponse().getStatus();
        assertThat(status).isEqualTo(HttpStatus.OK.value());
        BankBalanceSetDTO bankBalanceSetDTOFailure = new ObjectMapper()
                .readValue(resultFailure.getResponse().getContentAsString(), BankBalanceSetDTO.class);
        assertThat(bankBalanceSetDTOFailure).isNotNull();
        assertThat(bankBalanceSetDTOFailure.getBankBalances()).isNotNull();
        assertThat(bankBalanceSetDTOFailure.getBankBalances().size()).isEqualTo(0);

        /* Test Working case */
        String bankName = "Revolut";
        MvcResult result = mockMvc.perform(
                get(BankBalanceController.BASE_URL + "?bankName=" + bankName).contentType(MediaType.APPLICATION_JSON))
                                  .andReturn();

        status = result.getResponse().getStatus();
        assertThat(status).isEqualTo(HttpStatus.OK.value());

        BankBalanceSetDTO bankBalanceSetDTO = new ObjectMapper()
                .readValue(result.getResponse().getContentAsString(), BankBalanceSetDTO.class);
        assertThat(bankBalanceSetDTO).isNotNull();
        assertThat(bankBalanceSetDTO.getBankBalances()).isNotNull();
        assertThat(bankBalanceSetDTO.getBankBalances().size()).isEqualTo(1);
        assertThat(bankBalanceSetDTO.getBankBalances().iterator().next().getBank().getName()).isEqualTo(bankName);
    }

    @Test
    void saveBankBalance() throws Exception {
        BankBalanceDTO bankBalanceDTOpost = BankBalanceDTO.builder().bank(BankDTO.builder().name("bank").build())
                                                          .incomes("100").expenses("200").yearBalanceId(ID)
                                                          .monthBalanceId(ID).build();
        MvcResult resultPost = mockMvc.perform(
                post(YearBalanceController.BASE_URL + "/" + ID + "/monthBalances/" + ID + "/bankBalances")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(AbstractRestControllerTest.asJsonString(bankBalanceDTOpost))).andReturn();

        BankBalanceDTO bankBalanceDTOPostResult = new ObjectMapper()
                .readValue(resultPost.getResponse().getContentAsString(), BankBalanceDTO.class);

        /* Modifying expenses and bankName  */
        final String updatedBankName = "revolut";
        BankBalanceDTO bankBalanceDTO = BankBalanceDTO.builder().bank(BankDTO.builder().name(updatedBankName).build())
                                                      .expenses("500").yearBalanceId(ID).monthBalanceId(ID).build();
        MvcResult result = mockMvc.perform(put(BankBalanceController.BASE_URL + "/" + bankBalanceDTOPostResult.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(AbstractRestControllerTest.asJsonString(bankBalanceDTO))).andReturn();

        int status = result.getResponse().getStatus();
        assertThat(status).isEqualTo(HttpStatus.OK.value());

        BankBalanceDTO bankBalanceDTOResult = new ObjectMapper()
                .readValue(result.getResponse().getContentAsString(), BankBalanceDTO.class);
        assertThat(bankBalanceDTOResult.getId()).isEqualTo(bankBalanceDTOPostResult.getId());
        assertThat(bankBalanceDTOResult.getExpenses()).isEqualTo("500");
        assertThat(bankBalanceDTOResult.getResult()).isEqualTo("-500");
        assertThat(bankBalanceDTOResult.getIncomes()).isEqualTo("0");
        assertThat(bankBalanceDTOResult.getBank().getName()).isEqualTo(updatedBankName);
        assertThat(bankBalanceDTOResult.getYearBalanceId()).isEqualTo(bankBalanceDTOPostResult.getYearBalanceId());
        assertThat(bankBalanceDTOResult.getMonthBalanceId()).isEqualTo(bankBalanceDTOPostResult.getMonthBalanceId());
    }

    @Test
    void updateBankBalance() throws Exception {
        BankBalanceDTO bankBalanceDTOpost = BankBalanceDTO.builder().bank(BankDTO.builder().name("bank").build())
                                                          .incomes("100.00").expenses("200").yearBalanceId(ID)
                                                          .monthBalanceId(ID).build();
        MvcResult resultPost = mockMvc.perform(
                post(YearBalanceController.BASE_URL + "/" + ID + "/monthBalances/" + ID + "/bankBalances")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(AbstractRestControllerTest.asJsonString(bankBalanceDTOpost))).andReturn();

        BankBalanceDTO bankBalanceDTOPostResult = new ObjectMapper()
                .readValue(resultPost.getResponse().getContentAsString(), BankBalanceDTO.class);

        /* Modifying expenses. Other variable should be the same */
        BankBalanceDTO bankBalanceDTO = BankBalanceDTO.builder().expenses("150").build();
        MvcResult result = mockMvc.perform(
                patch(BankBalanceController.BASE_URL + "/" + bankBalanceDTOPostResult.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(AbstractRestControllerTest.asJsonString(bankBalanceDTO))).andReturn();

        int status = result.getResponse().getStatus();
        assertThat(status).isEqualTo(HttpStatus.OK.value());

        BankBalanceDTO bankBalanceDTOResult = new ObjectMapper()
                .readValue(result.getResponse().getContentAsString(), BankBalanceDTO.class);
        assertThat(bankBalanceDTOResult.getId()).isEqualTo(bankBalanceDTOPostResult.getId());
        assertThat(bankBalanceDTOResult.getExpenses()).isEqualTo("150");
        assertThat(bankBalanceDTOResult.getResult()).isEqualTo("-50.00");
        assertThat(bankBalanceDTOResult.getIncomes()).isEqualTo(bankBalanceDTOPostResult.getIncomes());
        assertThat(bankBalanceDTOResult.getBank().getName()).isEqualTo(bankBalanceDTOPostResult.getBank().getName());
        assertThat(bankBalanceDTOResult.getYearBalanceId()).isEqualTo(bankBalanceDTOPostResult.getYearBalanceId());
        assertThat(bankBalanceDTOResult.getMonthBalanceId()).isEqualTo(bankBalanceDTOPostResult.getMonthBalanceId());
    }

    @Test
    void updateBankBalanceChangingYearBalance() throws Exception {
        MvcResult resultGet = mockMvc
                .perform(get(BankBalanceController.BASE_URL + "/" + ID).contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        BankBalanceDTO bankBalanceDTOGetResult = new ObjectMapper()
                .readValue(resultGet.getResponse().getContentAsString(), BankBalanceDTO.class);
        long originalYearBalanceId = bankBalanceDTOGetResult.getYearBalanceId();

        /* Modifying yearBalanceId and monthBalanceId */
        MvcResult result = mockMvc.perform(
                patch(BankBalanceController.BASE_URL + "/" + ID).contentType(MediaType.APPLICATION_JSON).content(
                        AbstractRestControllerTest
                                .asJsonString(BankBalanceDTO.builder().yearBalanceId(ID).monthBalanceId(ID))))
                                  .andReturn();

        int status = result.getResponse().getStatus();
        assertThat(status).isEqualTo(HttpStatus.OK.value());

        BankBalanceDTO bankBalanceDTOResult = new ObjectMapper()
                .readValue(result.getResponse().getContentAsString(), BankBalanceDTO.class);
        assertThat(bankBalanceDTOResult.getId()).isEqualTo(ID);
        assertThat(bankBalanceDTOResult.getIncomes()).isEqualTo(bankBalanceDTOGetResult.getIncomes());
        assertThat(bankBalanceDTOResult.getExpenses()).isEqualTo(bankBalanceDTOGetResult.getExpenses());
        assertThat(bankBalanceDTOResult.getResult()).isEqualTo(bankBalanceDTOGetResult.getResult());
        assertThat(bankBalanceDTOResult.getBank().getName()).isEqualTo(bankBalanceDTOGetResult.getBank().getName());
        assertThat(bankBalanceDTOResult.getYearBalanceId()).isEqualTo(ID);
        assertThat(bankBalanceDTOResult.getMonthBalanceId()).isEqualTo(ID);


        MvcResult resultGetYearBalanceModifiedAfterPatch = mockMvc
                .perform(get(YearBalanceController.BASE_URL + "/" + ID).contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        YearBalanceDTO yearBalanceModifiedAfterPatch = new ObjectMapper()
                .readValue(resultGetYearBalanceModifiedAfterPatch.getResponse().getContentAsString(),
                        YearBalanceDTO.class);
        assertThat(yearBalanceModifiedAfterPatch.getId()).isEqualTo(ID);
        assertThat(yearBalanceModifiedAfterPatch.getIncomes()).isEqualTo(bankBalanceDTOResult.getIncomes());
        assertThat(yearBalanceModifiedAfterPatch.getExpenses()).isEqualTo(bankBalanceDTOResult.getExpenses());
        assertThat(yearBalanceModifiedAfterPatch.getResult()).isEqualTo(bankBalanceDTOResult.getResult());
        MonthBalanceDTO monthBalanceDTO = yearBalanceModifiedAfterPatch.getMonthBalances().iterator().next();
        assertThat(monthBalanceDTO.getExpenses()).isEqualTo(bankBalanceDTOResult.getExpenses());
        assertThat(monthBalanceDTO.getIncomes()).isEqualTo(bankBalanceDTOResult.getIncomes());
        assertThat(monthBalanceDTO.getExpenses()).isEqualTo(bankBalanceDTOResult.getExpenses());
        assertThat(monthBalanceDTO.getId()).isEqualTo(ID);
        assertThat(yearBalanceModifiedAfterPatch.getBankBalances().size()).isEqualTo(1);
        assertThat(monthBalanceDTO.getBankBalances().size()).isEqualTo(1);

        MvcResult resultGetYearBalanceOriginalAfterPatch = mockMvc.perform(
                get(YearBalanceController.BASE_URL + "/" + originalYearBalanceId)
                        .contentType(MediaType.APPLICATION_JSON)).andReturn();
        YearBalanceDTO yearBalanceOriginalAfterPatch = new ObjectMapper()
                .readValue(resultGetYearBalanceOriginalAfterPatch.getResponse().getContentAsString(),
                        YearBalanceDTO.class);
        assertThat(yearBalanceOriginalAfterPatch.getId()).isEqualTo(originalYearBalanceId);
        assertThat(yearBalanceOriginalAfterPatch.getIncomes()).isEqualTo("0.00");
        assertThat(yearBalanceOriginalAfterPatch.getExpenses()).isEqualTo("0.00");
        assertThat(yearBalanceOriginalAfterPatch.getResult()).isEqualTo("0.00");
        MonthBalanceDTO monthBalanceDTOOriginal = yearBalanceOriginalAfterPatch.getMonthBalances().iterator().next();
        assertThat(monthBalanceDTOOriginal.getExpenses()).isEqualTo("0.00");
        assertThat(monthBalanceDTOOriginal.getIncomes()).isEqualTo("0.00");
        assertThat(monthBalanceDTOOriginal.getExpenses()).isEqualTo("0.00");
        assertThat(monthBalanceDTOOriginal.getId()).isEqualTo(originalYearBalanceId);
        assertThat(yearBalanceOriginalAfterPatch.getBankBalances().size()).isEqualTo(0);
        assertThat(monthBalanceDTOOriginal.getBankBalances().size()).isEqualTo(0);
    }

    @Test
    void deleteBankBalance() throws Exception {
        MvcResult resultDelete = mockMvc
                .perform(delete(BankBalanceController.BASE_URL + "/" + ID).contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        BankBalanceDTO bankBalanceDTO = new ObjectMapper()
                .readValue(resultDelete.getResponse().getContentAsString(), BankBalanceDTO.class);
        long originalYearBalanceId = bankBalanceDTO.getYearBalanceId();

        MvcResult resultGet = mockMvc
                .perform(get(BankBalanceController.BASE_URL + "/" + ID).contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        int status = resultGet.getResponse().getStatus();
        assertThat(status).isEqualTo(HttpStatus.NOT_FOUND.value());

        assertThat(bankBalanceDTO.getId()).isEqualTo(ID);

        MvcResult resultYearBalanceAfterDelete = mockMvc
                .perform(get(YearBalanceController.BASE_URL + "/" + originalYearBalanceId).contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        YearBalanceDTO yearBalanceAfterDelete = new ObjectMapper()
                .readValue(resultYearBalanceAfterDelete.getResponse().getContentAsString(),
                        YearBalanceDTO.class);
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
}
