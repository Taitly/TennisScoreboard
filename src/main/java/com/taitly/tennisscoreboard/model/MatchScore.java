package com.taitly.tennisscoreboard.model;

import com.taitly.tennisscoreboard.entity.Player;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class MatchScore {
    private PlayerScore firstPlayerScore;
    private PlayerScore secondPlayerScore;

    private List<TennisSet> sets = new ArrayList<>();

    private Player winner = null;

    public MatchScore(Player firstPlayer, Player secondPlayer) {
        this.firstPlayerScore = new PlayerScore(firstPlayer);
        this.secondPlayerScore = new PlayerScore(secondPlayer);
    }

    public TennisSet getCurrentSet() {
        return sets.get(sets.size() - 1);
    }

    public boolean isFinished() {
        return winner != null;
    }

    public void pointWonBy(Player player) {
        if (isFinished()) {
            throw new IllegalStateException("Match is already finished");
        }

        TennisSet currentSet = getCurrentSet();
        currentSet.pointWonBy(player);

        updatePlayerScores();

        if (currentSet.isFinished()) {
            checkMatchWinner();
            if (!isFinished()) {
                sets.add(new TennisSet(firstPlayerScore.getPlayer(), secondPlayerScore.getPlayer()));
            }
        }
    }

    public Player getPlayerByName(String playerName) {
        if (playerName.equals(firstPlayerScore.getPlayer().getName())) {
            return firstPlayerScore.getPlayer();
        } else if (playerName.equals(secondPlayerScore.getPlayer().getName())) {
            return secondPlayerScore.getPlayer();
        } else {
            throw new IllegalArgumentException("Player with name '" + playerName + "' not found in this match");
        }
    }

    private void updatePlayerScores() {
        int firstSetsWon = (int) sets.stream()
                .filter(s -> s.isFinished() && s.getWinner().equals(firstPlayerScore.getPlayer()))
                .count();

        int secondSetsWon = (int) sets.stream()
                .filter(s -> s.isFinished() && s.getWinner().equals(secondPlayerScore.getPlayer()))
                .count();

        firstPlayerScore.updateSetsWon(firstSetsWon);
        secondPlayerScore.updateSetsWon(secondSetsWon);

        TennisSet currentSet = getCurrentSet();

        if (!currentSet.isFinished()) {
            firstPlayerScore.updateGamesWon(currentSet.getFirstPlayerGamesWon());
            secondPlayerScore.updateGamesWon(currentSet.getSecondPlayerGamesWon());

            if (currentSet.isTieBreakActive()) {
                TennisTieBreakPoint tieBreak = currentSet.getTieBreakPoint();
                firstPlayerScore.updateTieBreakPoints(tieBreak.getFirstPlayerTieBreakPoints());
                secondPlayerScore.updateTieBreakPoints(tieBreak.getSecondPlayerTieBreakPoints());

                firstPlayerScore.updateCurrentPoint(TennisPoint.ZERO);
                secondPlayerScore.updateCurrentPoint(TennisPoint.ZERO);
            } else {
                TennisGame currentGame = currentSet.getCurrentGame();
                if (!currentGame.isFinished()) {
                    firstPlayerScore.updateCurrentPoint(currentGame.getFirstPlayerPoint());
                    secondPlayerScore.updateCurrentPoint(currentGame.getSecondPlayerPoint());
                } else {
                    firstPlayerScore.updateCurrentPoint(TennisPoint.ZERO);
                    secondPlayerScore.updateCurrentPoint(TennisPoint.ZERO);
                }
                firstPlayerScore.updateTieBreakPoints(0);
                secondPlayerScore.updateTieBreakPoints(0);
            }
        } else {
            firstPlayerScore.updateGamesWon(0);
            secondPlayerScore.updateGamesWon(0);
            firstPlayerScore.updateCurrentPoint(TennisPoint.ZERO);
            secondPlayerScore.updateCurrentPoint(TennisPoint.ZERO);
            firstPlayerScore.updateTieBreakPoints(0);
            secondPlayerScore.updateTieBreakPoints(0);
        }
    }

    private void checkMatchWinner() {
        int setsToWin = 2;
        if (firstPlayerScore.getSetsWon() == setsToWin) {
            winner = firstPlayerScore.getPlayer();
        } else if (secondPlayerScore.getSetsWon() == setsToWin) {
            winner = secondPlayerScore.getPlayer();
        }
    }
}