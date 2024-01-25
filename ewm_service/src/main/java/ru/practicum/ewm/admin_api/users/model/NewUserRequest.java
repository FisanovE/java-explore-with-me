package ru.practicum.ewm.admin_api.users.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class NewUserRequest {

    @NotBlank(message = "Name is not be empty.")
    @Size(min = 2, max = 250)
    private String name;

    @Email
    @NotBlank(message = "Email is not be empty.")
    @Size(min = 6, max = 254)
    private String email;
}
