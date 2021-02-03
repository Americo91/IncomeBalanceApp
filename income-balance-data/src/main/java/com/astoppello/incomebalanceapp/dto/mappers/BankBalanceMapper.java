package com.astoppello.incomebalanceapp.dto.mappers;

import com.astoppello.incomebalanceapp.dto.domain.BankBalanceDTO;
import com.astoppello.incomebalanceapp.model.BankBalance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/** Created by @author stopp on 28/11/2020 */
@Mapper(
    uses = {BankMapper.class},
    componentModel = "spring")
public interface BankBalanceMapper {

  BankBalance bankBalanceDtoToBankBalance(BankBalanceDTO bankBalanceDto);

  @Mappings({
    @Mapping(source = "monthBalance.id", target = "monthBalanceId"),
    @Mapping(source = "yearBalance.id", target = "yearBalanceId")
  })
  BankBalanceDTO bankBalanceToBankBalanceDTO(BankBalance bankBalance);
}
