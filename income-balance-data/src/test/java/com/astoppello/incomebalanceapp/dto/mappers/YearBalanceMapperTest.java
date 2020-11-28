package com.astoppello.incomebalanceapp.dto.mappers;

import com.astoppello.incomebalanceapp.dto.domain.YearBalanceDTO;
import com.astoppello.incomebalanceapp.model.YearBalance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class YearBalanceMapperTest {

    private YearBalanceMapper mapper;
    private static final Integer YEAR = 2020;
    private static final Long ID = 1L;

    @BeforeEach
    void setUp() {
        mapper = YearBalanceMapper.INSTANCE;
    }

    @Test
    void yearBalanceDtoToYearBalance() {
        YearBalanceDTO yearBalanceDTO = new YearBalanceDTO();
        yearBalanceDTO.setId(ID);
        yearBalanceDTO.setYear(YEAR);

        YearBalance yearBalance = mapper.yearBalanceDtoToYearBalance(yearBalanceDTO);
        assertNotNull(yearBalance);
        assertEquals(yearBalanceDTO.getId(), yearBalance.getId());
        assertEquals(yearBalanceDTO.getYear(), yearBalance.getYear());
    }

    @Test
    void yearBalanceToYearBalanceDto() {
        YearBalance yearBalance = YearBalance.builder().id(ID).year(YEAR).build();
        YearBalanceDTO yearBalanceDTO = mapper.yearBalanceToYearBalanceDto(yearBalance);
        assertNotNull(yearBalanceDTO);
        assertEquals(yearBalance.getId(), yearBalanceDTO.getId());
        assertEquals(yearBalance.getYear(), yearBalanceDTO.getYear());
    }
}