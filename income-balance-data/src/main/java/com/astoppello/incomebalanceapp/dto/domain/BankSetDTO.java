package com.astoppello.incomebalanceapp.dto.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * Created by @author stopp on 21/11/2020
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankSetDTO {
    @JsonProperty("banks")
    private Set<BankDTO> banks;
}
