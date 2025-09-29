package com.emirhg.sistema.ecommerce.dao.repository;

import java.util.List;
import java.util.Optional;

public interface RepositoryCataloghi<T> {

    List<T> findAll() throws Exception;

    List<T> findByNome(String q) throws Exception;

    Optional<T> findById(int id) throws Exception;
    
     int creare(T t) throws Exception;
    
    int modificare(T t) throws Exception;

    Integer save(T t) throws Exception;

    boolean deleteById(int id) throws Exception;

}
