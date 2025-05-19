package com.cs544;

import lombok.Data;

@Data
public class Vote {
    private String voterId;
    private String vote;

    public Vote() {
    }

    public Vote(String voterId, String vote) {
        this.voterId = voterId;
        this.vote = vote;
    }
}
