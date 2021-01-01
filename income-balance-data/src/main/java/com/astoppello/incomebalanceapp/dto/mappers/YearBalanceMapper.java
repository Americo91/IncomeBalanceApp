package com.astoppello.incomebalanceapp.dto.mappers;

import com.astoppello.incomebalanceapp.dto.domain.YearBalanceDTO;
import com.astoppello.incomebalanceapp.model.YearBalance;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/** Created by @author stopp on 16/11/2020 */
@Mapper(
    uses = {MonthBalanceMapper.class},
    componentModel = "spring")
public interface YearBalanceMapper {

  @Mapping(source = "monthBalanceDTOList", target = "monthBalanceList")
  YearBalance yearBalanceDtoToYearBalance(YearBalanceDTO yearBalanceDTO);

  @InheritInverseConfiguration
  YearBalanceDTO yearBalanceToYearBalanceDto(YearBalance yearBalance);
}
