package com.taitly.tennisscoreboard.dao;

import com.taitly.tennisscoreboard.entity.Player;
import lombok.Getter;
import org.hibernate.Session;

import java.util.Optional;

public class PlayerDao extends BaseDao<Long, Player> {
    @Getter
    private static final PlayerDao INSTANCE = new PlayerDao();

    private PlayerDao() {
        super(Player.class);
    }

    public Optional<Player> findByName(String name) {
        try (Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(
                    session.createQuery("SELECT p FROM Player p WHERE p.name = :name", Player.class)
                            .setParameter("name", name)
                            .uniqueResult()
            );
        }
    }
}