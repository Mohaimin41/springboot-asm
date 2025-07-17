package com.sisimpur.library.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthorUpdateReqDTO {
    @NotBlank(message = "Name is required")
    private String name;

    private String biography;
}
