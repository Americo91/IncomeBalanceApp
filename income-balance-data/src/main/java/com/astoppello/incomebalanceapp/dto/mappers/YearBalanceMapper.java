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

    YearBalance toEntity(YearBalanceDTO yearBalanceDTO);

    @Mappings({
            @Mapping(source = "bankBalanceSet", target = "bankBalances"),
            @Mapping(source = "monthBalanceSet", target = "monthBalances"),
    })
    YearBalanceDTO toDto(YearBalance yearBalance);
}
