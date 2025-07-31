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
public class TennisSet {
    private Player firstPlayer;
    private Player secondPlayer;

    private List<TennisGame> games = new ArrayList<>();
    private int firstPlayerGamesWon = 0;
    private int secondPlayerGamesWon = 0;

    private boolean tieBreakActive = false;
    private TennisTieBreakPoint tieBreakPoint;

    private Player winner = null;

    public TennisSet(Player firstPlayer, Player secondPlayer) {
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
        startNewGame();
    }

    public void startNewGame() {
        if (tieBreakActive) {
            return;
        }
        TennisGame game = new TennisGame(firstPlayer, secondPlayer);
        games.add(game);
    }

    public void pointWonBy(Player player) {
        if (isFinished()) {
            throw new IllegalStateException("Set is already finished");
        }

        if (tieBreakActive) {
            tieBreakPoint.pointWonBy(player);

            if (tieBreakPoint.isFinished()) {
                winner = tieBreakPoint.getWinner();
            }
        } else {
            TennisGame currentGame = getCurrentGame();
            currentGame.pointWonBy(player);

            if (currentGame.isFinished()) {
                Player gameWinner = currentGame.getWinner();
                if (gameWinner.equals(firstPlayer)) {
                    firstPlayerGamesWon++;
                } else {
                    secondPlayerGamesWon++;
                }

                if (firstPlayerGamesWon == 6 && secondPlayerGamesWon == 6) {
                    tieBreakPoint = new TennisTieBreakPoint(firstPlayer, secondPlayer);
                    tieBreakActive = true;
                } else {
                    checkForSetWinner();
                    if (!isFinished()) {
                        startNewGame();
                    }
                }
            }
        }
    }

    public boolean isFinished() {
        return winner != null;
    }

    public TennisGame getCurrentGame() {
        if (games.isEmpty()) {
            startNewGame();
        }
        return games.get(games.size() - 1);
    }

    private void checkForSetWinner() {
        if ((firstPlayerGamesWon >= 6 || secondPlayerGamesWon >= 6) &&
            Math.abs(firstPlayerGamesWon - secondPlayerGamesWon) >= 2) {
            winner = firstPlayerGamesWon > secondPlayerGamesWon ? firstPlayer : secondPlayer;
        }
    }
}