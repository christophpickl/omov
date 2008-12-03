package at.phudy.dao;

import java.io.Serializable;
import java.util.List;

interface IAbstractDao<I extends Serializable, E> {

    E getById(I id);

    List<E> getAll();

    void delete(E entity);

    void saveOrUpdate(E entity);

} 