package com.cs544.worker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.cs544.Vote;

@Component
@KafkaListener(topics = "voter")
public class VoteListener {

    @Autowired
    private VoteService voteService;

    @KafkaHandler
    public void save(@Payload Vote vote) {
        // System.out.println("Received Vote: " + vote.toString());

        try {
            voteService.add(vote);
            // System.out.println("Vote persisted into db");
        } catch (Exception e) {
            System.err.println("Exception while persisting vote: ");
            System.err.println(e.toString());
        }
    }

}
