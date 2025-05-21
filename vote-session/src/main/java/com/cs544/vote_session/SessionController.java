package com.cs544.vote_session;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.cs544.Session;

@RestController
public class SessionController {

    @Autowired
    SessionService sessionService;

    @GetMapping("/sessions")
    public List<Session> getAll() {
        return sessionService.getAll();
    }

    @GetMapping("/sessions/{id}")
    public ResponseEntity<Session> getOne(@PathVariable Long id) {
        Optional<Session> session = sessionService.getOne(id);
        return session.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/sessions")
    @ResponseStatus(HttpStatus.CREATED)
    public Session create(@RequestBody Session session) {
        return sessionService.create(session);
    }

    @DeleteMapping("/sessions/{id}")
    public void delete(@PathVariable Long id) {
        sessionService.delete(id);
    }

}
