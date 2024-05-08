package com.utcn.demo.repository;

import com.utcn.demo.entity.Question;
import com.utcn.demo.entity.User;
import com.utcn.demo.entity.VotedQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VotedQuestionRepository extends JpaRepository<VotedQuestion, Long> {
    Optional<VotedQuestion> findByUserAndQuestion(User user, Question question);
}
