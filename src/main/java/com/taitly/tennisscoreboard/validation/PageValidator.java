package com.taitly.tennisscoreboard.validation;

import com.taitly.tennisscoreboard.exception.PageNotFoundException;
import com.taitly.tennisscoreboard.exception.PlayerNotFoundException;
import lombok.experimental.UtilityClass;

import java.util.regex.Pattern;

@UtilityClass
public class PageValidator {
    private static final String PAGE_NOT_FOUND_MESSAGE = "This page does not exist.";
    private static final String INVALID_PAGE_NUMBER = "Page number is too large or invalid.";
    private static final Pattern PAGE_PATTERN = Pattern.compile("^[1-9]\\d*$");
    private static final int MAX_PAGE_NUMBER = 10000;

    public int validatePageNumber(String pageParam) {
        if (pageParam == null || pageParam.isEmpty()) {
            return 1;
        }

        if (!PAGE_PATTERN.matcher(pageParam).matches()) {
            throw new PageNotFoundException(PAGE_NOT_FOUND_MESSAGE);
        }

        try {
            int pageNumber = Integer.parseInt(pageParam);
            if (pageNumber < 1 || pageNumber > MAX_PAGE_NUMBER) {
                throw new PageNotFoundException(PAGE_NOT_FOUND_MESSAGE);
            }
            return pageNumber;
        } catch (NumberFormatException e) {
            throw new PageNotFoundException(INVALID_PAGE_NUMBER);
        }
    }

    public void validatePageAndPlayers(int page, long totalPage, boolean hasPlayers) {
        if (totalPage > 0 && page > totalPage) {
            throw new PageNotFoundException(PAGE_NOT_FOUND_MESSAGE);
        }
        if (!hasPlayers) {
            throw new PlayerNotFoundException("No players were found.");
        }
    }
}