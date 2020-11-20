package com.astoppello.incomebalanceapp.dto.mappers;

import com.astoppello.incomebalanceapp.dto.domain.YearBalanceDTO;
import com.astoppello.incomebalanceapp.model.YearBalance;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Created by @author stopp on 16/11/2020
 */
@Mapper
public interface YearBalanceMapper {

    YearBalanceMapper INSTANCE = Mappers.getMapper(YearBalanceMapper.class);

    public YearBalance yearBalanceDtoToYearBalance(YearBalanceDTO yearBalanceDTO);
    public YearBalanceDTO yearBalanceToYearBalanceDto(YearBalance yearBalance);
}
