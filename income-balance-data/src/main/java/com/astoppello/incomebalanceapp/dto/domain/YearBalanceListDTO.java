package com.astoppello.incomebalanceapp.dto.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * Created by @author stopp on 16/11/2020
 */
@Data
@AllArgsConstructor
public class YearBalanceListDTO {
    @JsonProperty("yearbalances")
    private List<YearBalanceDTO> yearBalanceDTOList;
}
