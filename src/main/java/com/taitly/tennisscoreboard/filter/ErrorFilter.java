package com.taitly.tennisscoreboard.filter;

import com.taitly.tennisscoreboard.exception.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;

import java.io.IOException;

import static jakarta.servlet.http.HttpServletResponse.SC_NOT_FOUND;

@WebFilter("/*")
public class ErrorFilter extends HttpFilter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        try {
            super.doFilter(req, resp, chain);
        } catch (InvalidDataException e) {
            req.setAttribute("error", e.getMessage());
            req.getRequestDispatcher("new-match.jsp").forward(req, resp);
        } catch (PageNotFoundException | MatchNotFoundException | PlayerAlreadyExistsException e) {
            req.setAttribute("errorCode", SC_NOT_FOUND);
            req.setAttribute("errorMessage", e.getMessage());
            req.getRequestDispatcher("error.jsp").forward(req, resp);
        } catch (PlayerNotFoundException e) {
            req.setAttribute("errorMessage", e.getMessage());
            req.getRequestDispatcher("matches.jsp").forward(req, resp);
        }
    }
}