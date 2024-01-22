package ru.practicum.ewm.admin_api.users.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;

    @NotBlank(message = "Name is not be empty.")
    private String name;

    @Email
    @NotBlank(message = "Email is not be empty.")
    private String email;
}
