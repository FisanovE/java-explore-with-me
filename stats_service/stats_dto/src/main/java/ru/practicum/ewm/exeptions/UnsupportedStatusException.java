package ru.practicum.ewm.exeptions;

public class UnsupportedStatusException extends RuntimeException {

    public UnsupportedStatusException(final String message) {
        super(message);
    }
}
