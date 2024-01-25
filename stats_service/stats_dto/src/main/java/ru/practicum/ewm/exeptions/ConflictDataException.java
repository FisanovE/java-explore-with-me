package ru.practicum.ewm.exeptions;

public class ConflictDataException extends RuntimeException {

    public ConflictDataException(final String message) {
        super(message);
    }
}
