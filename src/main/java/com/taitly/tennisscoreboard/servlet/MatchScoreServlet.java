package com.taitly.tennisscoreboard.servlet;

import com.taitly.tennisscoreboard.dto.MatchScoreDto;
import com.taitly.tennisscoreboard.entity.Player;
import com.taitly.tennisscoreboard.mapper.MatchScoreMapper;
import com.taitly.tennisscoreboard.model.MatchScore;
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
    private final MatchScoreMapper matchScoreMapper = MatchScoreMapper.INSTANCE;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UUID uuid = UUID.fromString(req.getParameter("uuid"));

        MatchValidator.validateMatchExists(uuid);

        MatchScore matchScore = ongoingMatchesService.getMatchScore(uuid);

        MatchScoreDto matchScoreDto = matchScoreMapper.toDto(matchScore);

        req.setAttribute("matchScoreDto", matchScoreDto);
        req.setAttribute("uuid", uuid);

        req.getRequestDispatcher("match-score.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UUID uuid = UUID.fromString(req.getParameter("uuid"));
        String playerName = req.getParameter("playerName");

        MatchValidator.validateMatchExists(uuid);

        MatchScore matchScore = ongoingMatchesService.getMatchScore(uuid);
        Player scoringPlayer = matchScore.getPlayerByName(playerName);

        ongoingMatchesService.updateMatchScore(uuid, scoringPlayer);

        MatchScore updatedMatchScore = ongoingMatchesService.getMatchScore(uuid);
        MatchScoreDto updatedMatchScoreDto = matchScoreMapper.toDto(updatedMatchScore);

        if (!updatedMatchScore.isFinished()) {
            req.setAttribute("matchScoreDto", updatedMatchScoreDto);
            req.setAttribute("uuid", uuid);

            req.getRequestDispatcher("match-score.jsp").forward(req, resp);
        } else {

            finishedMatchesPersistenceService.saveMatchScore(updatedMatchScore);
            ongoingMatchesService.removeMatchScore(uuid);

            resp.sendRedirect(req.getContextPath() + "/matches");
        }
    }
}