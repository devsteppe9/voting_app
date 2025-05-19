package com.cs544;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Data
public class Vote {
    @Id
    private String voterId;

    @NotBlank
    private String vote;

    public Vote() {
    }

    public Vote(String voterId, String vote) {
        this.voterId = voterId;
        this.vote = vote;
    }
}
