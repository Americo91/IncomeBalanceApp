package com.astoppello.incomebalanceapp.dto.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

/**
 * Created by @author stopp on 28/11/2020
 */
@Data
@AllArgsConstructor
public class BankBalanceSetDTO {
    @JsonProperty("bankBalances")
    Set<BankBalanceDTO> bankBalances;
}
