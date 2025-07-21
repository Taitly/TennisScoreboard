package com.taitly.tennisscoreboard.service;

import com.taitly.tennisscoreboard.dto.MatchScoreDto;
import com.taitly.tennisscoreboard.entity.Player;
import com.taitly.tennisscoreboard.mapper.MatchScoreMapper;
import com.taitly.tennisscoreboard.model.MatchScore;
import com.taitly.tennisscoreboard.model.TennisPoint;
import lombok.Getter;

public class MatchScoreCalculationService {
    @Getter
    private static final MatchScoreCalculationService INSTANCE = new MatchScoreCalculationService();

    private final MatchScoreMapper matchScoreMapper = MatchScoreMapper.INSTANCE;
    private static final int SETS_TO_WIN = 2;
    private static final int MINIMAL_GAMES_TO_WIN = 6;
    private static final int TIE_BREAK_POINTS_TO_WIN = 7;

    private MatchScoreCalculationService() {

    }

    public MatchScoreDto playerScores(MatchScoreDto matchScoreDto, Player player) {
        MatchScore matchScore = matchScoreMapper.toEntity(matchScoreDto);

        if (isTieBreak(matchScore)) {

            updateTieBreakPoints(matchScore, player);

            if (isTieBreakFinished(matchScore)) {
                updateSets(matchScore, player);
                resetGames(matchScore);
                resetTieBreakPoints(matchScore);
                resetPoints(matchScore);
            }
        } else {

            TennisPoint oldPlayerPoint = getPlayerPoint(matchScore, player);
            TennisPoint oldOpponentPoint = getOpponentPoint(matchScore, player);

            updateNormalPoints(matchScore, player);

            TennisPoint newPlayerPoint = getPlayerPoint(matchScore, player);

            if (oldPlayerPoint == TennisPoint.FORTY && oldOpponentPoint == TennisPoint.FORTY &&
                    newPlayerPoint == TennisPoint.ADVANTAGE) {
                return matchScoreMapper.toDto(matchScore);
            }

            if (oldPlayerPoint == TennisPoint.THIRTY && newPlayerPoint == TennisPoint.FORTY) {
                return matchScoreMapper.toDto(matchScore);
            }

            if (isGameWon(matchScore, player)) {

                updateGames(matchScore, player);
                resetPoints(matchScore);

                if (isSetWon(matchScore)) {

                    updateSets(matchScore, player);
                    resetGames(matchScore);

                }
            }
        }
        return matchScoreMapper.toDto(matchScore);
    }

    public Player getWinner(MatchScoreDto matchScoreDto) {
        MatchScore matchScore = matchScoreMapper.toEntity(matchScoreDto);
        if (matchScore.getFirstPlayerSets() == SETS_TO_WIN) {
            return matchScore.getFirstPlayer();
        }
        return matchScore.getSecondPlayer();
    }

    public boolean isMatchOver(MatchScoreDto matchScoreDto) {
        MatchScore matchScore = matchScoreMapper.toEntity(matchScoreDto);
        return matchScore.getFirstPlayerSets() == SETS_TO_WIN ||
                matchScore.getSecondPlayerSets() == SETS_TO_WIN;
    }

    private void updateNormalPoints(MatchScore matchScore, Player player) {
        boolean isFirstPlayer = player.equals(matchScore.getFirstPlayer());

        TennisPoint firstPlayerPoint = matchScore.getFirstPlayerPoint();
        TennisPoint secondPlayerPoint = matchScore.getSecondPlayerPoint();

        if (isDeuce(firstPlayerPoint, secondPlayerPoint)) {

            handleDeuce(matchScore, isFirstPlayer);

        } else if (isAdvantage(firstPlayerPoint, secondPlayerPoint)) {

            handleAdvantage(matchScore, isFirstPlayer);

        } else {
            if (isFirstPlayer) {
                matchScore.setFirstPlayerPoint(nextPoint(firstPlayerPoint, secondPlayerPoint));
            } else {
                matchScore.setSecondPlayerPoint(nextPoint(secondPlayerPoint, firstPlayerPoint));
            }
        }
    }

    private TennisPoint nextPoint(TennisPoint current, TennisPoint opponent) {
        if (current == TennisPoint.THIRTY && opponent.ordinal() < TennisPoint.FORTY.ordinal()) {
            return TennisPoint.FORTY;
        }
        return current.next();
    }

    private void updateTieBreakPoints(MatchScore matchScore, Player player) {
        int firstPlayerTieBreakPoints = matchScore.getFirstPlayerTieBreakPoints();
        int secondPlayerTieBreakPoints = matchScore.getSecondPlayerTieBreakPoints();

        if (player.equals(matchScore.getFirstPlayer())) {
            matchScore.setFirstPlayerTieBreakPoints(firstPlayerTieBreakPoints + 1);
        } else if (player.equals(matchScore.getSecondPlayer())) {
            matchScore.setSecondPlayerTieBreakPoints(secondPlayerTieBreakPoints + 1);
        }
    }

    private void updateGames(MatchScore matchScore, Player player) {
        int firstPlayerGames = matchScore.getFirstPlayerGames();
        int secondPlayerGames = matchScore.getSecondPlayerGames();

        if (player.equals(matchScore.getFirstPlayer())) {
            matchScore.setFirstPlayerGames(firstPlayerGames + 1);
        } else if (player.equals(matchScore.getSecondPlayer())) {
            matchScore.setSecondPlayerGames(secondPlayerGames + 1);
        }
    }

    private void updateSets(MatchScore matchScore, Player player) {
        int firstPlayerSets = matchScore.getFirstPlayerSets();
        int secondPlayerSets = matchScore.getSecondPlayerSets();

        if (player.equals(matchScore.getFirstPlayer())) {
            matchScore.setFirstPlayerSets(firstPlayerSets + 1);
        } else if (player.equals(matchScore.getSecondPlayer())) {
            matchScore.setSecondPlayerSets(secondPlayerSets + 1);
        }
    }

    private void handleDeuce(MatchScore matchScore, boolean isFirstPlayer) {
        if (isFirstPlayer) {
            matchScore.setFirstPlayerPoint(TennisPoint.ADVANTAGE);
            matchScore.setSecondPlayerPoint(TennisPoint.FORTY);
        } else {
            matchScore.setSecondPlayerPoint(TennisPoint.ADVANTAGE);
            matchScore.setFirstPlayerPoint(TennisPoint.FORTY);
        }
    }

    private void handleAdvantage(MatchScore matchScore, boolean isFirstPlayer) {
        if (isFirstPlayer) {
            if (matchScore.getFirstPlayerPoint() == TennisPoint.ADVANTAGE) {
                resetPoints(matchScore);
                updateGames(matchScore, matchScore.getFirstPlayer());
            } else if (matchScore.getSecondPlayerPoint() == TennisPoint.ADVANTAGE) {
                matchScore.setFirstPlayerPoint(TennisPoint.FORTY);
                matchScore.setSecondPlayerPoint(TennisPoint.FORTY);
            }
        } else {
            if (matchScore.getSecondPlayerPoint() == TennisPoint.ADVANTAGE) {
                resetPoints(matchScore);
                updateGames(matchScore, matchScore.getSecondPlayer());
            } else if (matchScore.getFirstPlayerPoint() == TennisPoint.ADVANTAGE) {
                matchScore.setFirstPlayerPoint(TennisPoint.FORTY);
                matchScore.setSecondPlayerPoint(TennisPoint.FORTY);
            }
        }
    }

    private void resetPoints(MatchScore matchScore) {
        matchScore.setFirstPlayerPoint(TennisPoint.ZERO);
        matchScore.setSecondPlayerPoint(TennisPoint.ZERO);
    }

    private void resetGames(MatchScore matchScore) {
        matchScore.setFirstPlayerGames(0);
        matchScore.setSecondPlayerGames(0);
    }

    private void resetTieBreakPoints(MatchScore matchScore) {
        matchScore.setFirstPlayerTieBreakPoints(0);
        matchScore.setSecondPlayerTieBreakPoints(0);
    }

    private boolean isGameWon(MatchScore matchScore, Player player) {
        TennisPoint playerPoint = getPlayerPoint(matchScore, player);
        TennisPoint opponentPoint = getOpponentPoint(matchScore, player);

        if (isDeuce(playerPoint, opponentPoint)) {
            return false;
        }

        if (playerPoint == TennisPoint.ADVANTAGE) {
            return true;
        }

        return playerPoint == TennisPoint.FORTY && opponentPoint.ordinal() < TennisPoint.FORTY.ordinal();
    }

    private boolean isSetWon(MatchScore matchScore) {
        int firstPlayerGames = matchScore.getFirstPlayerGames();
        int secondPlayerGames = matchScore.getSecondPlayerGames();

        if (isTieBreak(firstPlayerGames, secondPlayerGames)) {
            return isTieBreakFinished(matchScore);
        }

        return (firstPlayerGames >= MINIMAL_GAMES_TO_WIN && firstPlayerGames - secondPlayerGames >= 2) ||
                (secondPlayerGames >= MINIMAL_GAMES_TO_WIN && secondPlayerGames - firstPlayerGames >= 2);
    }

    private boolean isTieBreakFinished(MatchScore matchScore) {
        int firstPlayerTieBreakPoints = matchScore.getFirstPlayerTieBreakPoints();
        int secondPlayerTieBreakPoints = matchScore.getSecondPlayerTieBreakPoints();
        return (firstPlayerTieBreakPoints >= TIE_BREAK_POINTS_TO_WIN && firstPlayerTieBreakPoints - secondPlayerTieBreakPoints >= 2) ||
                (secondPlayerTieBreakPoints >= TIE_BREAK_POINTS_TO_WIN && secondPlayerTieBreakPoints - firstPlayerTieBreakPoints >= 2);
    }

    private boolean isTieBreak(MatchScore matchScore) {
        return isTieBreak(matchScore.getFirstPlayerGames(), matchScore.getSecondPlayerGames());
    }

    private boolean isTieBreak(int firstPlayerGames, int secondPlayersGames) {
        return firstPlayerGames == MINIMAL_GAMES_TO_WIN && secondPlayersGames == MINIMAL_GAMES_TO_WIN;
    }

    private boolean isDeuce(TennisPoint firstPlayerPoint, TennisPoint secondPlayerPoint) {
        return firstPlayerPoint == TennisPoint.FORTY && secondPlayerPoint == TennisPoint.FORTY;
    }

    private boolean isAdvantage(TennisPoint firstPlayerPoint, TennisPoint secondPlayerPoint) {
        return firstPlayerPoint == TennisPoint.ADVANTAGE || secondPlayerPoint == TennisPoint.ADVANTAGE;
    }

    private TennisPoint getPlayerPoint(MatchScore matchScore, Player player) {
        if (player.equals(matchScore.getFirstPlayer())) {
            return matchScore.getFirstPlayerPoint();
        } else {
            return matchScore.getSecondPlayerPoint();
        }
    }

    private TennisPoint getOpponentPoint(MatchScore matchScore, Player player) {
        if (player.equals(matchScore.getFirstPlayer())) {
            return matchScore.getSecondPlayerPoint();
        } else {
            return matchScore.getFirstPlayerPoint();
        }
    }
}