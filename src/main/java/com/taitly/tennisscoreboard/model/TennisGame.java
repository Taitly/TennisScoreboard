package com.taitly.tennisscoreboard.model;

import com.taitly.tennisscoreboard.entity.Player;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TennisGame {
    private static final TennisPoint START_POINT = TennisPoint.ZERO;

    private Player firstPlayer;
    private Player secondPlayer;

    private TennisPoint firstPlayerPoint;
    private TennisPoint secondPlayerPoint;

    private Player winner = null;

    public TennisGame(Player firstPlayer, Player secondPlayer) {
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
        this.firstPlayerPoint = START_POINT;
        this.secondPlayerPoint = START_POINT;
    }

    public boolean isFinished() {
        return winner != null;
    }

    public boolean isDeuce() {
        return firstPlayerPoint == TennisPoint.FORTY && secondPlayerPoint == TennisPoint.FORTY;
    }

    public boolean isAdvantage() {
        return firstPlayerPoint == TennisPoint.ADVANTAGE || secondPlayerPoint == TennisPoint.ADVANTAGE;
    }

    public void pointWonBy(Player player) {
        if (isFinished()) {
            throw new IllegalStateException("Game is already finished");
        }

        boolean isFirstPlayer = player.equals(firstPlayer);

        if (isDeuce()) {
            handleDeucePoint(isFirstPlayer);
        } else if (isAdvantage()) {
            handleAdvantagePoint(isFirstPlayer);
        } else {
            normalPointWon(isFirstPlayer);
        }
    }

    private void normalPointWon(boolean isFirstPlayer) {
        TennisPoint prevPoint, newPoint, opponentPoint;

        if (isFirstPlayer) {
            prevPoint = firstPlayerPoint;
            opponentPoint = secondPlayerPoint;
            newPoint = nextPoint(prevPoint, opponentPoint);
            firstPlayerPoint = newPoint;
        } else {
            prevPoint = secondPlayerPoint;
            opponentPoint = firstPlayerPoint;
            newPoint = nextPoint(prevPoint, opponentPoint);
            secondPlayerPoint = newPoint;
        }

        if (prevPoint == TennisPoint.FORTY && opponentPoint.ordinal() <= TennisPoint.THIRTY.ordinal()) {
            winner = isFirstPlayer ? firstPlayer : secondPlayer;
        }
    }

    private TennisPoint nextPoint(TennisPoint current, TennisPoint opponent) {
        if (current == TennisPoint.THIRTY && opponent.ordinal() < TennisPoint.FORTY.ordinal()) {
            return TennisPoint.FORTY;
        }
        return current.next();
    }


    private void handleDeucePoint(boolean isFirstPlayer) {
        if (isFirstPlayer) {
            firstPlayerPoint = TennisPoint.ADVANTAGE;
            secondPlayerPoint = TennisPoint.FORTY;
        } else {
            secondPlayerPoint = TennisPoint.ADVANTAGE;
            firstPlayerPoint = TennisPoint.FORTY;
        }
    }

    private void handleAdvantagePoint(boolean isFirstPlayer) {
        if (isFirstPlayer) {
            if (firstPlayerPoint == TennisPoint.ADVANTAGE) {
                winner = firstPlayer;
            } else if (secondPlayerPoint == TennisPoint.ADVANTAGE) {
                firstPlayerPoint = TennisPoint.FORTY;
                secondPlayerPoint = TennisPoint.FORTY;
            }
        } else {
            if (secondPlayerPoint == TennisPoint.ADVANTAGE) {
                winner = secondPlayer;
            } else if (firstPlayerPoint == TennisPoint.ADVANTAGE) {
                firstPlayerPoint = TennisPoint.FORTY;
                secondPlayerPoint = TennisPoint.FORTY;
            }
        }
    }
}