package com.taitly.tennisscoreboard.dao;

import com.taitly.tennisscoreboard.util.HibernateUtil;
import jakarta.persistence.criteria.CriteriaQuery;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public abstract class BaseDao<K extends Serializable, E> implements CrudDao<K, E> {

    private final Class<E> clazz;
    protected final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    @Override
    public E save(E entity) {
        try(Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(entity);
            session.getTransaction().commit();
            return entity;
        }
    }

    @Override
    public Optional<E> findById(K id) {
        try(Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.find(clazz, id));
        }
    }

    @Override
    public List<E> findAll() {
        try(Session session = sessionFactory.openSession()) {
            CriteriaQuery<E> criteriaQuery = session.getCriteriaBuilder().createQuery(clazz);
            criteriaQuery.from(clazz);
            return session.createQuery(criteriaQuery).getResultList();
        }
    }
}