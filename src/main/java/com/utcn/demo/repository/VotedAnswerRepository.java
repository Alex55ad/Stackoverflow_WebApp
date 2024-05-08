package com.utcn.demo.repository;

import com.utcn.demo.entity.Answer;
import com.utcn.demo.entity.User;
import com.utcn.demo.entity.VotedAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VotedAnswerRepository extends JpaRepository<VotedAnswer, Long> {
    Optional<VotedAnswer> findByUserAndAnswer(User user, Answer answer);
}
