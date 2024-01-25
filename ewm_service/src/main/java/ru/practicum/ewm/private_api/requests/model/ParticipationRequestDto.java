package ru.practicum.ewm.private_api.requests.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import static ru.practicum.ewm.Constants.DATA_PATTERN;

@Getter
@Setter
@NoArgsConstructor
public class ParticipationRequestDto {

    private long id;

    @JsonFormat(pattern = DATA_PATTERN)
    private LocalDateTime created;
    private long event;
    private long requester;
    private StatusRequest status;
}
