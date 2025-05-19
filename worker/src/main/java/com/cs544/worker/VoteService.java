package com.cs544.worker;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs544.Vote;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class VoteService {

    @Autowired
    private VoteRepository voteRepository;

    public List<Vote> getAll() {
        return voteRepository.findAll();
    }

    public void add(Vote vote) {
        voteRepository.save(vote);
    }

    public Optional<Vote> get(String id) {
        return voteRepository.findById(id);
    }

    public void update(Vote vote, String voterId) throws Throwable {
        Optional<Vote> v = voteRepository.findById(voterId);
        if (v.isEmpty())
            throw new Exception("Vote is not exist with id:" + voterId);

        voteRepository.save(vote);
    }

    public void delete(String voterId) throws Throwable {
        Optional<Vote> v = voteRepository.findById(voterId);
        if (v.isEmpty())
            throw new Exception("Vote is not exist with id:" + voterId);

        voteRepository.delete(v.get());
    }

}
