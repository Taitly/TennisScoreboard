package com.taitly.tennisscoreboard.dao;

import com.taitly.tennisscoreboard.entity.Match;


public class MatchDao extends BaseDao<Long, Match> {

    private static final MatchDao INSTANCE = new MatchDao();

    private MatchDao() {
        super(Match.class);
    }
}