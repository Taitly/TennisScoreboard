package com.taitly.tennisscoreboard.model;

import com.taitly.tennisscoreboard.entity.Player;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TennisTieBreakPoint {
    private static final int POINTS_TO_WIN = 7;
    private static final int MIN_LEAD = 2;

    private Player firstPlayer;
    private Player secondPlayer;

    private int firstPlayerTieBreakPoints = 0;
    private int secondPlayerTieBreakPoints = 0;

    private Player winner = null;

    public TennisTieBreakPoint(Player firstPlayer, Player secondPlayer) {
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
    }

    public void pointWonBy(Player player) {
        if (isFinished()) {
            throw new IllegalStateException("Tie-break is already finished");
        }

        if (player.equals(firstPlayer)) {
            firstPlayerTieBreakPoints++;
        } else if (player.equals(secondPlayer)) {
            secondPlayerTieBreakPoints++;
        } else {
            throw new IllegalArgumentException("Unknown player");
        }

        checkWinner();
    }

    public boolean isFinished() {
        return winner != null;
    }

    private void checkWinner() {
        if (firstPlayerTieBreakPoints >= POINTS_TO_WIN ||
            secondPlayerTieBreakPoints >= POINTS_TO_WIN) {
            int diff = Math.abs(firstPlayerTieBreakPoints - secondPlayerTieBreakPoints);
            if (diff >= MIN_LEAD) {
                winner = firstPlayerTieBreakPoints > secondPlayerTieBreakPoints ? firstPlayer : secondPlayer;
            }
        }
    }
}