package com.taitly.tennisscoreboard.servlet;

import com.taitly.tennisscoreboard.dto.MatchScoreDto;
import com.taitly.tennisscoreboard.entity.Player;
import com.taitly.tennisscoreboard.model.TennisPoint;
import com.taitly.tennisscoreboard.service.OngoingMatchesService;
import com.taitly.tennisscoreboard.validation.PlayerValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.UUID;

@WebServlet("/new-match")
public class NewMatchServlet extends HttpServlet {
    private final OngoingMatchesService ongoingMatchesService = OngoingMatchesService.getINSTANCE();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("new-match.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String firstPlayerName = req.getParameter("playerOne").trim();
        String secondPlayerName = req.getParameter("playerTwo").trim();

        PlayerValidator.validatePlayerName(firstPlayerName);
        PlayerValidator.validatePlayerName(secondPlayerName);
        PlayerValidator.validatePlayersNamesAreDifferent(firstPlayerName, secondPlayerName);

        Player firstPlayer = Player.builder()
                .name(firstPlayerName)
                .build();

        Player secondPlayer = Player.builder()
                .name(secondPlayerName)
                .build();

        MatchScoreDto matchScoreDto = new MatchScoreDto(firstPlayer, secondPlayer, 0, 0,
                0, 0, TennisPoint.ZERO, TennisPoint.ZERO, 0, 0);

        UUID uuid = ongoingMatchesService.setMatchScore(matchScoreDto);

        resp.sendRedirect(req.getContextPath() + "/match-score?uuid=" + uuid);
    }
}