package com.astoppello.incomebalanceapp.dto.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by @author stopp on 21/11/2020
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BankDTO implements Comparable {
    private Long id;
    private String name;

    @Override
    public int compareTo(Object o) {
        return name.compareTo(((BankDTO) o).getName());
    }
}
