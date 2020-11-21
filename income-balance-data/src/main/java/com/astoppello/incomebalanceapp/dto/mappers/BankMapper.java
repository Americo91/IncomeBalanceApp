package com.astoppello.incomebalanceapp.dto.mappers;

import com.astoppello.incomebalanceapp.dto.domain.BankDTO;
import com.astoppello.incomebalanceapp.model.Bank;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Created by @author stopp on 21/11/2020
 */
@Mapper
public interface BankMapper {

    BankMapper INSTANCE = Mappers.getMapper(BankMapper.class);

    public Bank bankDtoToBank(BankDTO bankDTO);
    public BankDTO bankToBankDto(Bank bank);

}
