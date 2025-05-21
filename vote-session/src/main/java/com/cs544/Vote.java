package com.cs544;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class Vote {
    @Id
    private String voterId;
    private String option;

    @ManyToOne
    @JoinColumn(name = "session_id")
    private Session session;

    public Vote() {
    }

    public Vote(String voterId, String option, Session session) {
        this.voterId = voterId;
        this.option = option;
        this.session = session;
    }
}
