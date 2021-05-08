package com.astoppello.incomebalanceapp.dto.domain;

import lombok.Data;

/**
 * Created by @author stopp on 21/11/2020
 */
@Data
public class BankDTO implements Comparable {
    private Long id;
    private String name;

    @Override
    public int compareTo(Object o) {
        return name.compareTo(((BankDTO) o).getName());
    }
}
