package com.taitly.tennisscoreboard.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface CrudDao<K extends Serializable, E> {
    E save(E entity);

    Optional<E> findById(K id);

    List<E> findAll();
}