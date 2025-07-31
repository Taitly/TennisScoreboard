package com.taitly.tennisscoreboard.servlet;

import com.taitly.tennisscoreboard.dto.MatchDto;
import com.taitly.tennisscoreboard.entity.Match;
import com.taitly.tennisscoreboard.mapper.MatchMapper;
import com.taitly.tennisscoreboard.service.PaginationService;
import com.taitly.tennisscoreboard.validation.PageValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/matches")
public class MatchesServlet extends HttpServlet {
    private final PaginationService paginationService = PaginationService.getINSTANCE();
    private final MatchMapper matchMapper = MatchMapper.INSTANCE;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pageParameter = req.getParameter("page");
        String playerName = req.getParameter("filter_by_player_name");

        int page = PageValidator.validatePageNumber(pageParameter);

        List<Match> matches;
        long totalPages;

        if (playerName != null && !playerName.trim().isEmpty()) {
            String trimmedName = playerName.trim();
            matches = paginationService.getMatchesPagedByPlayerName(page, trimmedName);
            totalPages = paginationService.getTotalMatchPagesByPlayerName(trimmedName);
        } else {
            matches = paginationService.getMatchesPaged(page);
            totalPages = paginationService.getTotalMatchPages();
        }

        PageValidator.validatePageAndPlayers(page, totalPages, !matches.isEmpty());

        List<MatchDto> matchDtos = matches.stream()
                .map(matchMapper::toDto)
                .collect(Collectors.toList());

        req.setAttribute("matches", matchDtos);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("page", page);

        req.getRequestDispatcher("/matches.jsp").forward(req, resp);
    }
}