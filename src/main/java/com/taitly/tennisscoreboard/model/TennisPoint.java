package com.taitly.tennisscoreboard.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TennisPoint {
    ZERO("0"),
    FIFTEEN("15"),
    THIRTY("30"),
    FORTY("40"),
    ADVANTAGE("AD");

    private final String point;

    public TennisPoint next() {
        switch (this) {
            case ZERO -> {
                return FIFTEEN;
            }
            case FIFTEEN -> {
                return THIRTY;
            }
            case THIRTY -> {
                return FORTY;
            }
            case FORTY -> {
                return ADVANTAGE;
            }

            default -> {
                return this;
            }
        }
    }
}