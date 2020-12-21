package com.astoppello.incomebalanceapp.dto.mappers;

import com.astoppello.incomebalanceapp.dto.domain.MonthBalanceDTO;
import com.astoppello.incomebalanceapp.model.MonthBalance;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Created by @author stopp on 20/12/2020
 */
@Mapper(uses = {BankBalanceMapper.class}, componentModel = "spring")
public interface MonthBalanceMapper {

    @Mapping(source = "bankBalanceDTOList", target = "bankBalanceList")
    MonthBalance monthBalanceDtoToMonthBalance(MonthBalanceDTO monthBalanceDto);
    @InheritInverseConfiguration
    MonthBalanceDTO monthBalanceToMonthBalanceDto(MonthBalance monthBalance);
}
