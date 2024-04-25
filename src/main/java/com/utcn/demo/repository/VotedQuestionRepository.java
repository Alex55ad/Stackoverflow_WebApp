package com.utcn.demo.repository;

import com.utcn.demo.entity.VotedQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VotedQuestionRepository extends JpaRepository<VotedQuestion, Long> {
}
