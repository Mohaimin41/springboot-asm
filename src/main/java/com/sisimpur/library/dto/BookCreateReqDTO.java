package com.sisimpur.library.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookCreateReqDTO {
    
    @NotBlank(message = "Title is required")
    private String title;

    @NotNull(message = "Author ID is required")
    private Long author_id;

    @NotBlank(message = "Genre is required")
    private String genre;

    @NotNull(message = "Published year is required")
    @Min(value = 0, message = "Year must be positive")
    private Integer published_year;
}