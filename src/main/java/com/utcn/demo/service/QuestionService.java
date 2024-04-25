package com.utcn.demo.service;

import com.utcn.demo.entity.Question;
import com.utcn.demo.entity.User;
import com.utcn.demo.repository.QuestionRepository;
import com.utcn.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;

    public List<Question> retrieveQuestions() {
        return questionRepository.findAllByOrderByCreationDatetimeDesc();
    }

    public List<Question> getQuestionsByTag(String tag) {
        return questionRepository.findByTagsContainingOrderByCreationDatetimeDesc(tag);
    }

    public List<Question> getQuestionsByUser(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent())
            return questionRepository.findByAuthorOrderByCreationDatetimeDesc(user.get());
        else
            throw new RuntimeException("User not found");
    }

    public Question createQuestion(String author, String title, String text, String pictureUrl, String tags) {
        // Create new question
        Optional<User> user = userRepository.findByUsername(author);
        if(user.isPresent()) {
            Question question = new Question();
            question.setAuthor(user.get());
            question.setTitle(title);
            question.setText(text);
            question.setCreationDatetime(LocalDateTime.now());
            question.setPictureUrl(pictureUrl);
            question.setTags(tags);

            return questionRepository.save(question);
        }
        else throw new RuntimeException("User not found");
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
            if (!question.getAuthor().getUsername().equals(username)) {
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
            if (!question.getAuthor().getUsername().equals(username)) {
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
        Optional<User> user = userRepository.findByUsername(author);
        if(user.isPresent()) {
            List<Question> userQuestions = questionRepository.findByAuthor(user.get());
            if (!userQuestions.isEmpty())
                for (Question question : userQuestions) {
                    int upvotes = question.getUpvotes();
                    int downvotes = question.getDownvotes();
                    score += (upvotes * 2.5) - (downvotes * 1.5);
                }
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