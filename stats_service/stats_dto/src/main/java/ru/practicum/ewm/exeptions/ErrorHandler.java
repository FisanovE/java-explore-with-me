package ru.practicum.ewm.exeptions;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.ewm.Constants.FORMATTER;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handle(final ValidationException e) throws UnsupportedEncodingException {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST; // 400
        ErrorResponse errorResponse = new ErrorResponse();
        log.error("{} {}", httpStatus.value(), e.getMessage());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        e.printStackTrace(new PrintStream(out, true, "UTF-8"));
        String stackTrace = out.toString(Charset.forName("UTF-8"));
        errorResponse.setMessage(e.getMessage());
        errorResponse.setReason(httpStatus.getReasonPhrase());
        errorResponse.setStatus(httpStatus.name());
        errorResponse.setTimestamp(LocalDateTime.now().format(FORMATTER));
        errorResponse.setErrors(List.of(stackTrace));
        return errorResponse;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handle(final NotFoundException e) throws UnsupportedEncodingException {
        HttpStatus httpStatus = HttpStatus.NOT_FOUND; // 404
        ErrorResponse errorResponse = new ErrorResponse();
        log.error("{} {}", httpStatus.value(), e.getMessage());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        e.printStackTrace(new PrintStream(out, true, "UTF-8"));
        String stackTrace = out.toString(Charset.forName("UTF-8"));
        errorResponse.setMessage(e.getMessage());
        errorResponse.setReason(httpStatus.getReasonPhrase());
        errorResponse.setStatus(httpStatus.name());
        errorResponse.setTimestamp(LocalDateTime.now().format(FORMATTER));
        errorResponse.setErrors(List.of(stackTrace));
        return errorResponse;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handle(ConstraintViolationException e) throws UnsupportedEncodingException {
        HttpStatus httpStatus = HttpStatus.CONFLICT; // 409
        ErrorResponse errorResponse = new ErrorResponse();
        log.error("{} {}", httpStatus.value(), e.getMessage());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        e.printStackTrace(new PrintStream(out, true, "UTF-8"));
        String stackTrace = out.toString(Charset.forName("UTF-8"));
        errorResponse.setMessage(e.getMessage());
        errorResponse.setReason(httpStatus.getReasonPhrase());
        errorResponse.setStatus(httpStatus.name());
        errorResponse.setTimestamp(LocalDateTime.now().format(FORMATTER));
        errorResponse.setErrors(List.of(stackTrace));
        return errorResponse;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handle(ConflictDataException e) throws UnsupportedEncodingException {
        HttpStatus httpStatus = HttpStatus.CONFLICT; // 409
        ErrorResponse errorResponse = new ErrorResponse();
        log.error("{} {}", httpStatus.value(), e.getMessage());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        e.printStackTrace(new PrintStream(out, true, "UTF-8"));
        String stackTrace = out.toString(Charset.forName("UTF-8"));
        errorResponse.setMessage(e.getMessage());
        errorResponse.setReason(httpStatus.getReasonPhrase());
        errorResponse.setStatus(httpStatus.name());
        errorResponse.setTimestamp(LocalDateTime.now().format(FORMATTER));
        errorResponse.setErrors(List.of(stackTrace));
        return errorResponse;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handle(final UnsupportedStatusException e) throws UnsupportedEncodingException {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR; // 500
        ErrorResponse errorResponse = new ErrorResponse();
        log.error("{} {}", httpStatus.value(), e.getMessage());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        e.printStackTrace(new PrintStream(out, true, "UTF-8"));
        String stackTrace = out.toString(Charset.forName("UTF-8"));
        errorResponse.setMessage(e.getMessage());
        errorResponse.setReason(httpStatus.getReasonPhrase());
        errorResponse.setStatus(httpStatus.name());
        errorResponse.setTimestamp(LocalDateTime.now().format(FORMATTER));
        errorResponse.setErrors(List.of(stackTrace));
        return errorResponse;
    }
}
