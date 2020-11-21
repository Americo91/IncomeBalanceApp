package com.astoppello.incomebalanceapp.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by @author stopp on 21/11/2020
 */
@Data
@NoArgsConstructor
@Entity
public class Bank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Builder
    public Bank(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
