package com.noam.goren.technical.exception;
import com.noam.goren.technical.enums.HooverError;


public class HooverException extends Exception {
    final HooverError hooverError;

    public HooverException(final HooverError hooverError) {
        this.hooverError = hooverError;
    }

    public HooverError getError() {
        return hooverError;
    }

    @Override
    public String getMessage() {
        return hooverError.getMessage();
    }
}
