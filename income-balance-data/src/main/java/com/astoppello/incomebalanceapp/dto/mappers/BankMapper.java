package com.astoppello.incomebalanceapp.dto.mappers;

import com.astoppello.incomebalanceapp.dto.domain.BankDTO;
import com.astoppello.incomebalanceapp.model.Bank;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Created by @author stopp on 21/11/2020
 */
@Mapper(componentModel = "spring")
public interface BankMapper {

    Bank bankDtoToBank(BankDTO bankDTO);
    BankDTO bankToBankDto(Bank bank);

}
