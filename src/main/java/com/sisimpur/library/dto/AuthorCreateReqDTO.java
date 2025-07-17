package com.sisimpur.library.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthorCreateReqDTO {
    @NotBlank(message = "Name is required")
    private String name;

    private String biography;

    @Size(min = 0)
    private List<BookCreateNestedDTO> books;
}
