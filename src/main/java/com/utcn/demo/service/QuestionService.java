package com.utcn.demo.service;

import com.utcn.demo.entity.Question;
import com.utcn.demo.entity.User;
import com.utcn.demo.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    public List<Question> retrieveQuestions() {
        return questionRepository.findAllByOrderByCreationDatetimeDesc();
    }

    public List<Question> getQuestionsByTag(String tag) {
        return questionRepository.findByTagsContainingOrderByCreationDatetimeDesc(tag);
    }

    public List<Question> getQuestionsByUser(String username) {
        return questionRepository.findByAuthorOrderByCreationDatetimeDesc(username);
    }

    public Question createQuestion(User author, String title, String text, String pictureUrl, String tags) {
        // Create new question
        Question question = new Question();
        question.setAuthor(author);
        question.setTitle(title);
        question.setText(text);
        question.setCreationDatetime(LocalDateTime.now());
        question.setPictureUrl(pictureUrl);
        question.setTags(tags);

        return questionRepository.save(question);
    }

    public Question updateQuestion(Long id, String title, String text, String pictureUrl, String tags) {
        Optional<Question> optionalQuestion = questionRepository.findById(id);
        if (optionalQuestion.isPresent()) {
            Question question = optionalQuestion.get();
            question.setTitle(title);
            question.setText(text);
            question.setPictureUrl(pictureUrl);
            question.setTags(tags);

            return questionRepository.save(question);
        } else {
            throw new RuntimeException("Question not found");
        }
    }

    public Question upvoteQuestion(Long questionId, String username) {
        Optional<Question> optionalQuestion = questionRepository.findById(questionId);
        if (optionalQuestion.isPresent()) {
            Question question = optionalQuestion.get();
            if (!question.getAuthor().equals(username)) {
                    int upvotes = question.getUpvotes();
                    question.setUpvotes(upvotes + 1);
                    return questionRepository.save(question);
                }
                else {
                throw new RuntimeException("You cannot upvote your own question");
            }
        }
        return null;
    }

    public Question downvoteQuestion(Long questionId, String username) {
        Optional<Question> optionalQuestion = questionRepository.findById(questionId);
        if (optionalQuestion.isPresent()) {
            Question question = optionalQuestion.get();
            if (!question.getAuthor().equals(username)) {
                int downvotes = question.getDownvotes();
                question.setUpvotes(downvotes + 1);
                return questionRepository.save(question);
            }
            else {
                throw new RuntimeException("You cannot upvote your own question");
            }
        }
        return null;
    }

    @Transactional
    public Question insertQuestion(Question question) {
        return questionRepository.save(question);
    }

    public double calculateQuestionScore(String author) {
        double score = 0;
        List<Question> userQuestions = questionRepository.findByAuthor(author);
        for (Question question : userQuestions) {
            int upvotes = question.getUpvotes();
            int downvotes = question.getDownvotes();
            score += (upvotes * 2.5) - (downvotes * 1.5);
        }
        return score;
    }
    @Transactional
    public void deleteQuestionById(Long id) {
        if (!questionRepository.existsById(id)) {
            throw new RuntimeException("Question not found");
        } else {
            questionRepository.deleteById(id);
        }
    }
}