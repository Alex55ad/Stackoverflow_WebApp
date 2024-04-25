package com.utcn.demo.repository;

import com.utcn.demo.entity.VotedAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VotedAnswerRepository extends JpaRepository<VotedAnswer, Long> {
}
