package com.utcn.demo.repository;


import com.utcn.demo.entity.Question;
import com.utcn.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    Question getQuestionById(Long id);
    List<Question> findAllByOrderByCreationDatetimeDesc();

    List<Question> findByTagsContainingOrderByCreationDatetimeDesc(String tag);

    List<Question> findByAuthorOrderByCreationDatetimeDesc(User username);

    List<Question> findByAuthor(User author);
}
