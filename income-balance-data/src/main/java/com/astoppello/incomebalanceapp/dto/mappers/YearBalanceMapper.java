package com.astoppello.incomebalanceapp.dto.mappers;

import com.astoppello.incomebalanceapp.dto.domain.YearBalanceDTO;
import com.astoppello.incomebalanceapp.model.BankBalance;
import com.astoppello.incomebalanceapp.model.YearBalance;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * Created by @author stopp on 16/11/2020
 */
@Mapper(
        uses = {MonthBalanceMapper.class, BankBalanceMapper.class},
        componentModel = "spring")
public interface YearBalanceMapper {

    @Mappings({
            @Mapping(source = "monthBalances", target = "monthBalanceList"),
            @Mapping(source = "bankBalances", target = "bankBalanceList"),
            @Mapping(source = "expenses", target = "expenses", qualifiedByName = "StringToBigDecimal"),
            @Mapping(source = "incomes", target = "incomes", qualifiedByName = "StringToBigDecimal"),
            @Mapping(source = "result", target = "result", qualifiedByName = "StringToBigDecimal"),
            @Mapping(source = "salary", target = "salary", qualifiedByName = "StringToBigDecimal"),
    })
    YearBalance yearBalanceDtoToYearBalance(YearBalanceDTO yearBalanceDTO);

    @Mappings({
            @Mapping(source = "bankBalanceList", target = "bankBalances"),
            @Mapping(source = "monthBalanceList", target = "monthBalances"),
            @Mapping(source = "expenses", target = "expenses", qualifiedByName = "BigDecimalToString"),
            @Mapping(source = "incomes", target = "incomes", qualifiedByName = "BigDecimalToString"),
            @Mapping(source = "result", target = "result", qualifiedByName = "BigDecimalToString"),
            @Mapping(source = "salary", target = "salary", qualifiedByName = "BigDecimalToString"),
    })
    YearBalanceDTO yearBalanceToYearBalanceDto(YearBalance yearBalance);
}
