package com.astoppello.incomebalanceapp.dto.mappers;

import com.astoppello.incomebalanceapp.dto.domain.MonthBalanceDTO;
import com.astoppello.incomebalanceapp.model.MonthBalance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by @author stopp on 20/12/2020
 */
@Mapper(
        uses = {BankBalanceMapper.class},
        componentModel = "spring")
public interface MonthBalanceMapper {

    @Mappings({
            @Mapping(source = "expenses", target = "expenses", qualifiedByName = "StringToBigDecimal"),
            @Mapping(source = "incomes", target = "incomes", qualifiedByName = "StringToBigDecimal"),
            @Mapping(source = "result", target = "result", qualifiedByName = "StringToBigDecimal"),
            @Mapping(source = "salary", target = "salary", qualifiedByName = "StringToBigDecimal"),
    })
    MonthBalance toEntity(MonthBalanceDTO monthBalanceDto);

    @Mappings({
            @Mapping(source = "bankBalanceSet", target = "bankBalances"),
            @Mapping(source = "yearBalance.id", target = "yearBalanceId"),
            @Mapping(source = "expenses", target = "expenses", qualifiedByName = "BigDecimalToString"),
            @Mapping(source = "incomes", target = "incomes", qualifiedByName = "BigDecimalToString"),
            @Mapping(source = "result", target = "result", qualifiedByName = "BigDecimalToString"),
            @Mapping(source = "salary", target = "salary", qualifiedByName = "BigDecimalToString"),
    })
    MonthBalanceDTO toDto(MonthBalance monthBalance);
}
