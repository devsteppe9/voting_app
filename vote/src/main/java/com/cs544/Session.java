package com.cs544;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class Session {
    public Session() {
    }

    private Long id;

    @NotBlank(message = "Title is mandatory")
    private String title;

    @NotBlank(message = "Option A is mandatory")
    @Size(min = 1, max = 6, message = "Option should have a length between 1 and 6 characters")
    private String optionA;

    @NotBlank(message = "Option B is mandatory")
    @Size(min = 1, max = 6, message = "Option should have a length between 1 and 6 characters")
    private String optionB;
}
