package ru.practicum.ewm.admin_api.comments.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentNew {

    @NotBlank
    @Column(name = "text", length = 7000)
    private String text;
}
