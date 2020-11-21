package com.astoppello.incomebalanceapp.dto.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * Created by @author stopp on 21/11/2020
 */
@Data
@AllArgsConstructor
public class BankListDTO {
    @JsonProperty("banks")
    private List<BankDTO> bankDTOList;
}
