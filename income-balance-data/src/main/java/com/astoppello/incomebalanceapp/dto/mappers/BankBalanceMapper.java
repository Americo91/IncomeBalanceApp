package com.astoppello.incomebalanceapp.dto.mappers;

import com.astoppello.incomebalanceapp.dto.domain.BankBalanceDTO;
import com.astoppello.incomebalanceapp.model.BankBalance;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Created by @author stopp on 28/11/2020
 */
@Mapper
public interface BankBalanceMapper {
    BankBalanceMapper INSTANCE = Mappers.getMapper(BankBalanceMapper.class);

    public BankBalance bankBalanceDtoToBankBalance(BankBalanceDTO bankBalanceDto);
    public BankBalanceDTO bankBalanceToBankBalanceDTO(BankBalance bankBalance);
}
