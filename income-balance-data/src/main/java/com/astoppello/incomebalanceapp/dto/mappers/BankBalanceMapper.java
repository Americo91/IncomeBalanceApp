package com.astoppello.incomebalanceapp.dto.mappers;

import com.astoppello.incomebalanceapp.dto.domain.BankBalanceDTO;
import com.astoppello.incomebalanceapp.model.BankBalance;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.*;
import org.springframework.util.NumberUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by @author stopp on 28/11/2020
 */
@Mapper(
        uses = {BankMapper.class},
        componentModel = "spring")
public interface BankBalanceMapper {

    @Mappings({
            @Mapping(source = "expenses", target = "expenses", qualifiedByName = "StringToBigDecimal"),
            @Mapping(source = "incomes", target = "incomes", qualifiedByName = "StringToBigDecimal"),
            @Mapping(source = "result", target = "result", qualifiedByName = "StringToBigDecimal")
    })
    BankBalance bankBalanceDtoToBankBalance(BankBalanceDTO bankBalanceDto);

    @Mappings({
            @Mapping(source = "monthBalance.id", target = "monthBalanceId"),
            @Mapping(source = "yearBalance.id", target = "yearBalanceId"),
            @Mapping(source = "expenses", target = "expenses", qualifiedByName = "BigDecimalToString"),
            @Mapping(source = "incomes", target = "incomes", qualifiedByName = "BigDecimalToString"),
            @Mapping(source = "result", target = "result", qualifiedByName = "BigDecimalToString")
    })
    BankBalanceDTO bankBalanceToBankBalanceDTO(BankBalance bankBalance);

    @Named("StringToBigDecimal")
    public static BigDecimal stringToBigDecimal(String amount) {
        if (StringUtils.isEmpty(amount)) {
            return null;
        }
        return NumberUtils.parseNumber(amount, BigDecimal.class);
    }

    @Named("BigDecimalToString")
    public static String bigDecimalToString(BigDecimal amount) {
        if (amount == null) {
            return null;
        }
        return amount.toString();
    }
}
