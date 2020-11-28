package com.astoppello.incomebalanceapp.services;

import java.util.List;

/**
 * Created by @author stopp on 28/11/2020
 */
public interface CrudService<T, ID> {

    List<T> findAll();
    T findById(ID id);
}
