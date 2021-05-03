package com.astoppello.incomebalanceapp.dto.mappers;

import com.astoppello.incomebalanceapp.dto.domain.YearBalanceDTO;
import com.astoppello.incomebalanceapp.model.BankBalance;
import com.astoppello.incomebalanceapp.model.MonthBalance;
import com.astoppello.incomebalanceapp.model.YearBalance;
import com.astoppello.incomebalanceapp.utils.ModelEqualUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.astoppello.incomebalanceapp.utils.ModelEqualUtils.assertYearBalanceAndYearBalanceDtoAreEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = {YearBalanceMapperImpl.class, BankBalanceMapperImpl.class, BankMapperImpl.class,
        MonthBalanceMapperImpl.class})
public class YearBalanceMapperTest {

    @Autowired
    private YearBalanceMapper mapper;
    private static final Integer YEAR = 2020;
    private static final Long ID = 1L;

    @Test
    void yearBalanceDtoToYearBalance() {
        YearBalanceDTO yearBalanceDTO = new YearBalanceDTO();
        yearBalanceDTO.setId(ID);
        yearBalanceDTO.setYear(YEAR);

        YearBalance yearBalance = mapper.yearBalanceDtoToYearBalance(yearBalanceDTO);
        assertNotNull(yearBalance);
        assertYearBalanceAndYearBalanceDtoAreEquals(yearBalance, yearBalanceDTO);
    }

    @Test
    void yearBalanceToYearBalanceDto() {
        YearBalance yearBalance = YearBalance.builder()
                                             .id(ID)
                                             .year(YEAR)
                                             .build()
                                             .addMonthBalance(MonthBalance.builder().id(ID).month("September").build())
                .addBankBalance(BankBalance.builder().id(ID).build());
        YearBalanceDTO yearBalanceDTO = mapper.yearBalanceToYearBalanceDto(yearBalance);
        assertNotNull(yearBalanceDTO);
        assertEquals(yearBalance.getId(), yearBalanceDTO.getId());
        assertEquals(yearBalance.getYear(), yearBalanceDTO.getYear());
        assertEquals(yearBalance.getBankBalanceSet().size(), yearBalanceDTO.getBankBalances().size());
        assertEquals(yearBalance.getMonthBalanceSet().size(), yearBalanceDTO.getMonthBalances().size());
    }
}