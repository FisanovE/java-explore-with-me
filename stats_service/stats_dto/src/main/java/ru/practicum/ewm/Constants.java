package ru.practicum.ewm;

import org.springframework.data.domain.Sort;

import java.time.format.DateTimeFormatter;

public class Constants {
    public static final String DATA_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(DATA_PATTERN);
    public static final Sort SORT_BY_EVENT_DATE = Sort.by(Sort.Direction.ASC, "eventDate");
    public static final Sort SORT_BY_VIEWS = Sort.by(Sort.Direction.ASC, "views");
}
