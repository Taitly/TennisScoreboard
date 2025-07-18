package com.taitly.tennisscoreboard.service;

import com.taitly.tennisscoreboard.dao.MatchDao;
import com.taitly.tennisscoreboard.entity.Match;
import lombok.Getter;

import java.util.List;

public class PaginationService {
    @Getter
    private static final PaginationService INSTANCE = new PaginationService();

    private final MatchDao matchDao = MatchDao.getINSTANCE();
    private static final int PAGE_SIZE = 5;

    private PaginationService() {

    }

    public List<Match> getMatchesPaged(int page) {
        return matchDao.findAllPaged(page, PAGE_SIZE);
    }

    public List<Match> getMatchesPagedByPlayerName(int page, String playerName) {
        return matchDao.findAllPagedByName(page, PAGE_SIZE, playerName);
    }

    public long getTotalMatchPages() {
        long totalRecords = matchDao.countAll();
        return (long) Math.ceil((double) totalRecords / PAGE_SIZE);
    }

    public long getTotalMatchPagesByPlayerName(String playerName) {
        long totalRecords = matchDao.countByPlayerName(playerName);
        return (long) Math.ceil((double) totalRecords / PAGE_SIZE);
    }
}