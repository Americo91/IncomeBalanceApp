package com.astoppello.incomebalanceapp.dto.mappers;

import com.astoppello.incomebalanceapp.dto.domain.MonthBalanceDTO;
import com.astoppello.incomebalanceapp.model.MonthBalance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/** Created by @author stopp on 20/12/2020 */
@Mapper(
    uses = {BankBalanceMapper.class},
    componentModel = "spring")
public interface MonthBalanceMapper {

  @Mapping(source = "bankBalances", target = "bankBalanceList")
  MonthBalance monthBalanceDtoToMonthBalance(MonthBalanceDTO monthBalanceDto);

  @Mappings({
    @Mapping(source = "bankBalanceList", target = "bankBalances"),
    @Mapping(source = "yearBalance.id", target = "yearBalanceId")
  })
  MonthBalanceDTO monthBalanceToMonthBalanceDto(MonthBalance monthBalance);
}
