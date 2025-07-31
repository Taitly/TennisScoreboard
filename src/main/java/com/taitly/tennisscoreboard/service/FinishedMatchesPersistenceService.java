package com.taitly.tennisscoreboard.service;

import com.taitly.tennisscoreboard.dao.MatchDao;
import com.taitly.tennisscoreboard.dao.PlayerDao;
import com.taitly.tennisscoreboard.entity.Match;
import com.taitly.tennisscoreboard.entity.Player;
import com.taitly.tennisscoreboard.exception.PlayerAlreadyExistsException;
import com.taitly.tennisscoreboard.model.MatchScore;
import jakarta.persistence.PersistenceException;
import jakarta.validation.ConstraintViolationException;
import lombok.Getter;

import java.util.Optional;

public class FinishedMatchesPersistenceService {
    @Getter
    private static final FinishedMatchesPersistenceService INSTANCE = new FinishedMatchesPersistenceService();

    private final MatchDao matchDao = MatchDao.getINSTANCE();
    private final PlayerDao playerDao = PlayerDao.getINSTANCE();
    private final static String PLAYER_ALREADY_EXISTS_MSG_TEMPLATE = "Player with name '%s' already exists in database (constraint violation)," +
            " but could not be found during retrieval.";

    private FinishedMatchesPersistenceService() {

    }

    public void saveMatchScore(MatchScore matchScore) {
        Player playerOne = saveOrGetPlayerByName(matchScore.getFirstPlayerScore().getPlayer().getName());
        Player playerTwo = saveOrGetPlayerByName(matchScore.getSecondPlayerScore().getPlayer().getName());

        Player winner = matchScore.getWinner();

        if (winner.getName().equals(playerOne.getName())) {
            winner = playerOne;
        } else {
            winner = playerTwo;
        }

        Match match = Match.builder()
                .player1(playerOne)
                .player2(playerTwo)
                .winner(winner)
                .build();

        matchDao.save(match);
    }

    private Player saveOrGetPlayerByName(String playerName) {
        Optional<Player> playerFromDb = playerDao.findByName(playerName);
        if (playerFromDb.isPresent()) {
            return playerFromDb.get();
        }
        try {
            return playerDao.save(
                    Player.builder()
                            .name(playerName)
                            .build()
            );
        } catch (ConstraintViolationException | PersistenceException e) {
            String message = String.format(PLAYER_ALREADY_EXISTS_MSG_TEMPLATE, playerName);
            return playerDao.findByName(playerName)
                    .orElseThrow(() -> new PlayerAlreadyExistsException(message));
        }
    }
}