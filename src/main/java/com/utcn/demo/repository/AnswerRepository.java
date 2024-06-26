package com.utcn.demo.repository;


import com.utcn.demo.entity.Answer;
import com.utcn.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {

    Answer getAnswerById(Long id);
    List<Answer> findByQuestionIdOrderByCreationDatetimeDesc(Long questionId);

    List<Answer> findByAuthor(User author);
}
