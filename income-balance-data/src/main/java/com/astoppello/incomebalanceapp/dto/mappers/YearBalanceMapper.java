package com.astoppello.incomebalanceapp.dto.mappers;

import com.astoppello.incomebalanceapp.dto.domain.YearBalanceDTO;
import com.astoppello.incomebalanceapp.model.YearBalance;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/** Created by @author stopp on 16/11/2020 */
@Mapper(
    uses = {MonthBalanceMapper.class},
    componentModel = "spring")
public interface YearBalanceMapper {

  @Mappings({
    @Mapping(source = "monthBalances", target = "monthBalanceList"),
    @Mapping(source = "bankBalances", target = "bankBalanceList")
  })
  YearBalance yearBalanceDtoToYearBalance(YearBalanceDTO yearBalanceDTO);

  @InheritInverseConfiguration
  YearBalanceDTO yearBalanceToYearBalanceDto(YearBalance yearBalance);
}
