package com.patternpedia.api.exception;

public class NullDesignModelException extends RuntimeException {

    public NullDesignModelException() {
        super("DesignModel is null");
    }

    public NullDesignModelException(String message) {
        super(message);
    }
}
