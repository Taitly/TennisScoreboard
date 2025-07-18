package com.taitly.tennisscoreboard.dao;

import com.taitly.tennisscoreboard.entity.Match;
import lombok.Getter;
import org.hibernate.Session;

import java.util.List;


public class MatchDao extends BaseDao<Long, Match> {
    @Getter
    private static final MatchDao INSTANCE = new MatchDao();

    private MatchDao() {
        super(Match.class);
    }

    public List<Match> findAllPaged(int page, int pageSize) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("SELECT m FROM Match m ORDER BY m.id DESC", Match.class)
                    .setFirstResult((page - 1) * pageSize)
                    .setMaxResults(pageSize)
                    .getResultList();
        }
    }

    public List<Match> findAllPagedByName(int page, int pageSize, String playerName) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("""
                            SELECT m FROM Match m
                            WHERE lower(m.player1.name) LIKE lower(:playerName)
                            OR lower(m.player2.name) LIKE lower(:playerName)
                            ORDER BY m.id DESC
                            """, Match.class)
                    .setParameter("playerName", "%" + playerName + "%")
                    .setFirstResult((page - 1) * pageSize)
                    .setMaxResults(pageSize)
                    .getResultList();
        }
    }

    public long countAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("SELECT COUNT(m) FROM Match m", Long.class).getSingleResult();
        }
    }

    public long countByPlayerName(String playerName) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("""
                            SELECT COUNT(m) FROM Match m
                            WHERE lower(m.player1.name) LIKE lower(:playerName)
                            OR lower(m.player2.name) LIKE lower(:playerName)
                            """, Long.class)
                    .setParameter("playerName", "%" + playerName + "%")
                    .getSingleResult();
        }
    }
}