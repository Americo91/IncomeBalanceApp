package com.astoppello.incomebalanceapp.dto.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * Created by @author stopp on 16/11/2020
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class YearBalanceSetDTO {
    @JsonProperty("yearBalances")
    private Set<YearBalanceDTO> yearBalances;
}
