package com.cs544;

import lombok.Data;

@Data
public class Vote {
    private String voterId;
    private String option;
    private Long sessionId;

    public Vote() {
    }

    public Vote(String voterId, String option) {
        this.voterId = voterId;
        this.option = option;
    }
}
