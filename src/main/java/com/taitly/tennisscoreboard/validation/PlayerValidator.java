package com.taitly.tennisscoreboard.validation;

import com.taitly.tennisscoreboard.exception.InvalidDataException;
import lombok.experimental.UtilityClass;

import java.util.regex.Pattern;

@UtilityClass
public class PlayerValidator {
    private static final String INVALID_PLAYER_NAME = """
            Invalid player name. Enter the full name with a first name (at least 2 letters) and a last name (at least 2 letters), separated by a space.
            Optional middle name or initial is allowed.
            Example: "Bob Miles" or "Emily O'Connor" or "John Smith J".
            """;

    private static final int MAX_PLAYER_NAME_LENGTH = 70;

    private static final Pattern PLAYER_NAME_PATTERN = Pattern.compile("^([a-zA-Z]{2,}\\s[a-zA-Z]+'?-?[a-zA-Z]{2,}\\s?([a-zA-Z]+)?)$");

    public void validatePlayerName(String playerName) {
        if (playerName == null || playerName.isEmpty()) {
            throw new InvalidDataException("The player's name should not be empty.");
        }

        if (!PLAYER_NAME_PATTERN.matcher(playerName).matches()) {
            throw new InvalidDataException(INVALID_PLAYER_NAME);
        }

        if (playerName.length() > MAX_PLAYER_NAME_LENGTH) {
            throw new InvalidDataException("Player's name is too long. Maximum length allowed is " + MAX_PLAYER_NAME_LENGTH + " characters.");
        }
    }

    public void validatePlayersNamesAreDifferent(String name1, String name2) {
        if (name1.equalsIgnoreCase(name2)) {
            throw new InvalidDataException("Players cannot have the same name. Please enter different names.");
        }
    }
}