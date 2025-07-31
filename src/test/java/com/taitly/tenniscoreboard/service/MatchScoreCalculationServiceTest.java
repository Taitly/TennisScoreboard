package com.taitly.tenniscoreboard.service;

import com.taitly.tennisscoreboard.entity.Player;
import com.taitly.tennisscoreboard.model.MatchScore;
import com.taitly.tennisscoreboard.model.TennisPoint;
import com.taitly.tennisscoreboard.model.TennisSet;
import com.taitly.tennisscoreboard.model.TennisTieBreakPoint;
import com.taitly.tennisscoreboard.service.MatchScoreCalculationService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Tests for Tennis Match Score Calculation Service")
class MatchScoreCalculationServiceTest {
    private Player firstPlayer;
    private Player secondPlayer;
    private MatchScore matchScore;
    private MatchScoreCalculationService matchScoreCalculationService;

    @BeforeAll
    void setupOnce() {
        firstPlayer = Player.builder()
                .name("First player")
                .build();

        secondPlayer = Player.builder()
                .name("Second player")
                .build();

        matchScoreCalculationService = MatchScoreCalculationService.getINSTANCE();
    }

    @BeforeEach
    void init() {
        matchScore = new MatchScore(firstPlayer, secondPlayer);
        matchScore.getSets().add(new TennisSet(firstPlayer, secondPlayer));
    }

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    @DisplayName("Game Scoring Mechanics — incrementing points and winning games")
    class GameScoreTest {
        @ParameterizedTest(name = "{0} gets a point in the game when scoring")
        @MethodSource("scoringPlayerProvider")
        @DisplayName("Player gets a new point after scoring a point")
        void playerShouldGetNewPointWhenScores(Player scoringPlayer) {
            TennisPoint previousPoint = getCurrentPointByPlayer(scoringPlayer);

            matchScoreCalculationService.playerScores(matchScore, scoringPlayer);

            TennisPoint actualPoint = getCurrentPointByPlayer(scoringPlayer);

            assertNotEquals(previousPoint, actualPoint,
                    String.format("%s should get new point", scoringPlayer.getName()));
        }

        @ParameterizedTest(name = "{0} wins the game after he scores with the score in game: points - {1}:{2}")
        @MethodSource("playerAndPointsForGameWinProvider")
        @DisplayName("Player wins the game when scoring from a winning point score")
        void playerShouldWinGameWhenPlayerScores(Player scoringPlayer, TennisPoint firstPlayerPoint, TennisPoint secondPlayerPoint) {
            setScoreInGame(firstPlayerPoint, secondPlayerPoint);

            matchScoreCalculationService.playerScores(matchScore, scoringPlayer);

            int expectedGamesWon = 1;
            int actualGamesWon = getGamesWonByPlayer(scoringPlayer);

            assertEquals(expectedGamesWon, actualGamesWon,
                    String.format("%s should get 1 point in games", scoringPlayer.getName()));
        }

        private Stream<Player> scoringPlayerProvider() {
            return Stream.of(firstPlayer, secondPlayer);
        }

        private Stream<Arguments> playerAndPointsForGameWinProvider() {
            return Stream.of(
                    Arguments.of(firstPlayer, TennisPoint.FORTY, TennisPoint.ZERO),
                    Arguments.of(firstPlayer, TennisPoint.FORTY, TennisPoint.FIFTEEN),
                    Arguments.of(firstPlayer, TennisPoint.FORTY, TennisPoint.THIRTY),

                    Arguments.of(secondPlayer, TennisPoint.ZERO, TennisPoint.FORTY),
                    Arguments.of(secondPlayer, TennisPoint.FIFTEEN, TennisPoint.FORTY),
                    Arguments.of(secondPlayer, TennisPoint.THIRTY, TennisPoint.FORTY)
            );
        }
    }

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    @DisplayName("Handling Advantage and Deuce states in game play")
    class AdvantageAndDeuceStateTest {
        @ParameterizedTest(name = "{0} gets an 'ADVANTAGE' point after he scores with the score in game: points - {1}:{2}")
        @MethodSource("playerAndPointsForGetAdvantageProvider")
        @DisplayName("Player gets ADVANTAGE point when scoring at DEUCE")
        void playerShouldGetAdvantageWhenWinsPointAtDeuce(Player scoringPlayer, TennisPoint firstPlayerPoint, TennisPoint secondPlayerPoint) {
            setScoreInGame(firstPlayerPoint, secondPlayerPoint);

            matchScoreCalculationService.playerScores(matchScore, scoringPlayer);
            boolean isAdvantage = matchScore.getCurrentSet().getCurrentGame().isAdvantage();

            TennisPoint expectedPoint = TennisPoint.ADVANTAGE;
            TennisPoint actualPoint = getCurrentPointByPlayer(scoringPlayer);

            assertAll("Advantage point after deuce",
                    () -> assertEquals(expectedPoint, actualPoint, String.format("%s should get 'ADVANTAGE' point", scoringPlayer.getName())),
                    () -> assertTrue(isAdvantage, "Game should be in ADVANTAGE state")
            );
        }

        @ParameterizedTest(name = "Game returns to DEUCE scenario after {0} scores with the score in game: points - {1}:{2}")
        @MethodSource("playerAndPointsForReturnToDeuceProvider")
        @DisplayName("Game returns to DEUCE state when opponent scores during ADVANTAGE")
        void playerShouldReturnToFortyPointAfterAdvantagePoint(Player scoringPlayer, TennisPoint firstPlayerPoint, TennisPoint secondPlayerPoint) {
            setScoreInGame(firstPlayerPoint, secondPlayerPoint);

            matchScoreCalculationService.playerScores(matchScore, scoringPlayer);

            boolean isDeuce = matchScore.getCurrentSet().getCurrentGame().isDeuce();

            TennisPoint expectedPoint = TennisPoint.FORTY;
            TennisPoint actualPoint = getCurrentPointByPlayer(scoringPlayer);

            assertAll("Return to deuce after advantage",
                    () -> assertEquals(expectedPoint, actualPoint, String.format("%s should have FORTY point", scoringPlayer.getName())),
                    () -> assertTrue(isDeuce, "Game should be in DEUCE state")
            );
        }

        @ParameterizedTest(name = "{0} wins game after he scores with the score in game: points - {1}:{2}")
        @MethodSource("playerAndPointsForWinAfterAdvantageProvider")
        @DisplayName("Player wins the game when scoring while having ADVANTAGE")
        void playerShouldWinGameWhenWinsPointAtAdvantageState(Player scoringPlayer, TennisPoint firstPlayerPoint, TennisPoint secondPlayerPoint) {
            setScoreInGame(firstPlayerPoint, secondPlayerPoint);

            matchScoreCalculationService.playerScores(matchScore, scoringPlayer);

            int expectedGamesWon = 1;
            int actualGamesWon = getGamesWonByPlayer(scoringPlayer);

            assertEquals(expectedGamesWon, actualGamesWon, String.format("%s should get 1 point in games", scoringPlayer.getName()));
        }

        private Stream<Arguments> playerAndPointsForGetAdvantageProvider() {
            return Stream.of(
                    Arguments.of(firstPlayer, TennisPoint.FORTY, TennisPoint.FORTY),

                    Arguments.of(secondPlayer, TennisPoint.FORTY, TennisPoint.FORTY)
            );
        }

        private Stream<Arguments> playerAndPointsForReturnToDeuceProvider() {
            return Stream.of(
                    Arguments.of(firstPlayer, TennisPoint.FORTY, TennisPoint.ADVANTAGE),

                    Arguments.of(secondPlayer, TennisPoint.ADVANTAGE, TennisPoint.FORTY)
            );
        }

        private Stream<Arguments> playerAndPointsForWinAfterAdvantageProvider() {
            return Stream.of(
                    Arguments.of(firstPlayer, TennisPoint.ADVANTAGE, TennisPoint.FORTY),

                    Arguments.of(secondPlayer, TennisPoint.FORTY, TennisPoint.ADVANTAGE)
            );
        }
    }

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    @DisplayName("Set Scoring and Transition to Tiebreak scenarios")
    class SetScoreTest {
        @ParameterizedTest(name = "{0} wins set after he scores with the score in game: sets - 0:0, games - {1}:{2}, points - {3}:{4}")
        @MethodSource("playerAndPointsForWinSetProvider")
        @DisplayName("Player wins the set when winning a game at a critical set score")
        void shouldWinSetWhenPlayerWinGame(Player scoringPlayer, int firstPlayerGames, int secondPlayerGames, TennisPoint firstPlayerPoint, TennisPoint secondPlayerPoint) {
            setScoreInSet(firstPlayerGames, secondPlayerGames);
            setScoreInGame(firstPlayerPoint, secondPlayerPoint);

            matchScoreCalculationService.playerScores(matchScore, scoringPlayer);

            int expectedSetsWon = 1;
            int actualSetsWon = getSetsWonByPlayer(scoringPlayer);

            assertEquals(expectedSetsWon, actualSetsWon, String.format("%s should get 1 point in sets", scoringPlayer.getName()));
        }

        @ParameterizedTest(name = "Set goes to a 'TIEBREAK' state after {0} scores with the score: sets - 0:0, games - {1}:{2}, points - {3}:{4}")
        @MethodSource("playerAndPointsForGoToTiebreakProvider")
        @DisplayName("Set enters TIEBREAK state at appropriate game score")
        void shouldStartTiebreakWhenPlayerWinGame(Player scoringPlayer, int firstPlayerGames, int secondPlayerGames, TennisPoint firstPlayerPoint, TennisPoint secondPlayerPoint) {
            setScoreInSet(firstPlayerGames, secondPlayerGames);
            setScoreInGame(firstPlayerPoint, secondPlayerPoint);

            matchScoreCalculationService.playerScores(matchScore, scoringPlayer);

            boolean isTiebreakActive = matchScore.getCurrentSet().isTieBreakActive();

            assertTrue(isTiebreakActive, "The tiebreak should be active in the set");
        }

        private Stream<Arguments> playerAndPointsForWinSetProvider() {
            return Stream.of(
                    Arguments.of(firstPlayer, 5, 0, TennisPoint.FORTY, TennisPoint.ZERO),
                    Arguments.of(firstPlayer, 6, 5, TennisPoint.FORTY, TennisPoint.ZERO),

                    Arguments.of(secondPlayer, 0, 5, TennisPoint.ZERO, TennisPoint.FORTY),
                    Arguments.of(secondPlayer, 5, 6, TennisPoint.ZERO, TennisPoint.FORTY)
            );
        }

        private Stream<Arguments> playerAndPointsForGoToTiebreakProvider() {
            return Stream.of(
                    Arguments.of(firstPlayer, 5, 6, TennisPoint.FORTY, TennisPoint.ZERO),

                    Arguments.of(secondPlayer, 6, 5, TennisPoint.ZERO, TennisPoint.FORTY)
            );
        }
    }

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    @DisplayName("Tiebreak Mechanics — scoring points and deciding set outcome")
    class TiebreakTest {
        @ParameterizedTest(name = "{0} gets a tiebreak point in the game when scoring in 'TIEBREAK' state")
        @MethodSource("scoringPlayerProvider")
        @DisplayName("Player receives a tiebreak point after scoring during TIEBREAK")
        void playerShouldGetTiebreakPointWhenScores(Player scoringPlayer) {
            setupTiebreakState();

            int previousTiebreakPoint = getTiebreakPointWonByPlayer(scoringPlayer);
            matchScoreCalculationService.playerScores(matchScore, scoringPlayer);
            int actualTiebreakPoint = getTiebreakPointWonByPlayer(scoringPlayer);

            assertNotEquals(previousTiebreakPoint, actualTiebreakPoint, String.format("%s should get new tiebreak point", scoringPlayer.getName()));
        }

        @ParameterizedTest(name = "{0} wins a set when scores with a score in tiebreak: tiebreakPoints - {1}:{2}")
        @MethodSource("playerAndTiebreakPointsForWinInTiebreakProvider")
        @DisplayName("Player wins the set when winning in TIEBREAK with winning score")
        void shouldWinSetInTiebreakWhenPlayerScores(Player scoringPlayer, int firstPlayerTiebreakPoints, int secondPlayerTiebreakPoints) {
            setupTiebreakState();
            setScoreInTiebreak(firstPlayerTiebreakPoints, secondPlayerTiebreakPoints);

            matchScoreCalculationService.playerScores(matchScore, scoringPlayer);
            int expectedSetsWon = 1;
            int actualSetsWon = getSetsWonByPlayer(scoringPlayer);

            assertEquals(expectedSetsWon, actualSetsWon, String.format("%s should get 1 point in sets", scoringPlayer.getName()));
        }

        @ParameterizedTest(name = "The tiebreak continues when a {0} scores with a score in tiebreak: 6-6")
        @MethodSource("scoringPlayerProvider")
        @DisplayName("Tiebreak continues when score is tied 6:6 after scoring point")
        void tiebreakShouldNotEndedWhenPlayerWinPointInTiebreak(Player scoringPlayer) {
            setupTiebreakState();
            setScoreInTiebreak(6, 6);

            matchScoreCalculationService.playerScores(matchScore, scoringPlayer);

            boolean isTiebreakActive = matchScore.getCurrentSet().isTieBreakActive();

            assertTrue(isTiebreakActive, "The tiebreak should be active in the set");
        }

        private Stream<Player> scoringPlayerProvider() {
            return Stream.of(firstPlayer, secondPlayer);
        }

        private Stream<Arguments> playerAndTiebreakPointsForWinInTiebreakProvider() {
            return Stream.of(
                    Arguments.of(firstPlayer, 6, 0),
                    Arguments.of(firstPlayer, 6, 5),

                    Arguments.of(secondPlayer, 0, 6),
                    Arguments.of(secondPlayer, 5, 6)
            );
        }
    }

    private void setScoreInGame(TennisPoint firstPlayerPoint, TennisPoint secondPlayerPoint) {
        matchScore.getCurrentSet().getCurrentGame().setFirstPlayerPoint(firstPlayerPoint);
        matchScore.getCurrentSet().getCurrentGame().setSecondPlayerPoint(secondPlayerPoint);
    }

    private void setScoreInSet(int firstPlayerGames, int secondPlayerGames) {
        matchScore.getCurrentSet().setFirstPlayerGamesWon(firstPlayerGames);
        matchScore.getCurrentSet().setSecondPlayerGamesWon(secondPlayerGames);
    }

    private void setScoreInTiebreak(int firstPlayerTiebreakPoint, int secondPlayerTiebreakPoint) {
        matchScore.getCurrentSet().getTieBreakPoint().setFirstPlayerTieBreakPoints(firstPlayerTiebreakPoint);
        matchScore.getCurrentSet().getTieBreakPoint().setSecondPlayerTieBreakPoints(secondPlayerTiebreakPoint);
    }

    private void setupTiebreakState() {
        matchScore.getCurrentSet().setTieBreakActive(true);
        matchScore.getCurrentSet().setTieBreakPoint(new TennisTieBreakPoint(firstPlayer, secondPlayer));
    }

    private TennisPoint getCurrentPointByPlayer(Player player) {
        return player.equals(firstPlayer) ?
                matchScore.getFirstPlayerScore().getCurrentPoint() : matchScore.getSecondPlayerScore().getCurrentPoint();
    }

    private int getGamesWonByPlayer(Player player) {
        return player.equals(firstPlayer) ?
                matchScore.getFirstPlayerScore().getGamesWon() : matchScore.getSecondPlayerScore().getGamesWon();
    }

    private int getSetsWonByPlayer(Player player) {
        return player.equals(firstPlayer) ?
                matchScore.getFirstPlayerScore().getSetsWon() : matchScore.getSecondPlayerScore().getSetsWon();
    }

    private int getTiebreakPointWonByPlayer(Player player) {
        return player.equals(firstPlayer) ?
                matchScore.getFirstPlayerScore().getTieBreakPoints() : matchScore.getSecondPlayerScore().getTieBreakPoints();
    }
}