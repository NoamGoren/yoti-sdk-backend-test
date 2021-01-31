package com.noam.goren.technical.enums;

public enum HooverError {
    FORMAT_NOT_VALID("Format of the request is invalid."),
    INVALID_ROOM_DIMENSION("Invalid Room Size."),
    INVALID_START_POSITION("The initial position is invalid."),
    INVALID_PATCH_POSITION("The position of Patch is invalid"),
    INVALID_INSTRUCTION("The string can only contain the following characters: N,S,W,E.");

    private final String message;

    HooverError(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}