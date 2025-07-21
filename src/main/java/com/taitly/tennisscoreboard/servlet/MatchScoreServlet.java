package com.taitly.tennisscoreboard.servlet;

import com.taitly.tennisscoreboard.dto.MatchScoreDto;
import com.taitly.tennisscoreboard.entity.Player;
import com.taitly.tennisscoreboard.service.FinishedMatchesPersistenceService;
import com.taitly.tennisscoreboard.service.MatchScoreCalculationService;
import com.taitly.tennisscoreboard.service.OngoingMatchesService;
import com.taitly.tennisscoreboard.validation.MatchValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.UUID;

@WebServlet("/match-score")
public class MatchScoreServlet extends HttpServlet {
    private final OngoingMatchesService ongoingMatchesService = OngoingMatchesService.getINSTANCE();
    private final MatchScoreCalculationService matchScoreCalculationService = MatchScoreCalculationService.getINSTANCE();
    private final FinishedMatchesPersistenceService finishedMatchesPersistenceService = FinishedMatchesPersistenceService.getINSTANCE();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UUID uuid = UUID.fromString(req.getParameter("uuid"));

        MatchValidator.validateMatchExists(uuid);

        MatchScoreDto matchScoreDto = ongoingMatchesService.getMatchScore(uuid);

        req.setAttribute("matchScoreDto", matchScoreDto);
        req.setAttribute("uuid", uuid);

        req.getRequestDispatcher("match-score.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UUID uuid = UUID.fromString(req.getParameter("uuid"));
        String playerName = req.getParameter("playerName");

        Player player = Player.builder()
                .name(playerName)
                .build();

        MatchValidator.validateMatchExists(uuid);

        MatchScoreDto currentMatchScoreDto = ongoingMatchesService.getMatchScore(uuid);
        MatchScoreDto updatedMatchScoreDto = matchScoreCalculationService.playerScores(currentMatchScoreDto, player);

        ongoingMatchesService.updateMatchScore(uuid, updatedMatchScoreDto);

        if (!matchScoreCalculationService.isMatchOver(updatedMatchScoreDto)) {
            req.setAttribute("matchScoreDto", updatedMatchScoreDto);
            req.setAttribute("uuid", uuid);

            req.getRequestDispatcher("match-score.jsp").forward(req, resp);
        } else {
            Player winner = matchScoreCalculationService.getWinner(updatedMatchScoreDto);

            finishedMatchesPersistenceService.saveMatchScore(updatedMatchScoreDto, winner);
            ongoingMatchesService.removeMatchScore(uuid);

            resp.sendRedirect(req.getContextPath() + "/matches");
        }
    }
}