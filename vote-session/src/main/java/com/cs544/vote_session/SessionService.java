package com.cs544.vote_session;

import java.util.List;
import java.util.Optional;

import com.cs544.Session;

public interface SessionService {

    public List<Session> getAll();

    public Optional<Session> getOne(Long id);

    public Session create(Session session);

    public void delete(Long id);
}
