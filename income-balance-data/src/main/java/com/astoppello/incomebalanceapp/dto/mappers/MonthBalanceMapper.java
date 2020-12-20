package com.astoppello.incomebalanceapp.dto.mappers;

import com.astoppello.incomebalanceapp.dto.domain.MonthBalanceDTO;
import com.astoppello.incomebalanceapp.model.MonthBalance;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Created by @author stopp on 20/12/2020
 */
@Mapper
public interface MonthBalanceMapper {

    MonthBalanceMapper INSTANCE = Mappers.getMapper(MonthBalanceMapper.class);

    public MonthBalance monthBalanceDtoToMonthBalance(MonthBalanceDTO monthBalanceDto);
    public MonthBalanceDTO monthBalanceToMonthBalanceDto(MonthBalance monthBalance);

}
