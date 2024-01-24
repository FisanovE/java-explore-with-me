package ru.practicum.ewm.admin_api.events.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.ewm.admin_api.categories.model.Category;
import ru.practicum.ewm.admin_api.events.model.enums.StateEvent;
import ru.practicum.ewm.admin_api.users.model.User;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

import static ru.practicum.ewm.Constants.DATA_PATTERN;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Size(min = 20, max = 2000)
    private String annotation;

    @Column(name = "confirmed_request")
    private int confirmedRequest;

    @JsonFormat(pattern = DATA_PATTERN)
    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @Size(min = 20, max = 7000)
    private String description;

    @NotNull
    @JsonFormat(pattern = DATA_PATTERN)
    @Column(name = "event_date", columnDefinition = "TIMESTAMP")
    private LocalDateTime eventDate;

    @NotNull
    private Boolean paid = false;

    @Column(name = "participant_limit")
    private int participantLimit = 0;

    @JsonFormat(pattern = DATA_PATTERN)
    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    @Column(name = "request_moderation")
    private Boolean requestModeration = true;

    @Enumerated(EnumType.STRING)
    private StateEvent state;

    @Size(min = 3, max = 120)
    private String title;
    private int views;

    @NotNull
    @Embedded
    private Location location;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "initiator_id")
    private User initiator;
}
