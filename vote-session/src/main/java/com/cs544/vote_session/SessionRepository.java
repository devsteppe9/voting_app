package com.cs544.vote_session;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cs544.Session;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {

}
