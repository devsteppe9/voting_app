package com.cs544;

import lombok.Data;

@Data
public class Vote {
    private String voterId;
    private String option;
    private Session session;

    public Vote() {
    }

    public Vote(String voterId, String option, Session session) {
        this.voterId = voterId;
        this.option = option;
        this.session = session;
    }
}
