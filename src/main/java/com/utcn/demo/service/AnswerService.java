package com.utcn.demo.service;

import com.utcn.demo.entity.Answer;
import com.utcn.demo.entity.Question;
import com.utcn.demo.entity.User;
import com.utcn.demo.repository.AnswerRepository;
import com.utcn.demo.repository.QuestionRepository;
import com.utcn.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;

    public List<Answer> retrieveAnswers() {
        return answerRepository.findAll();
    }

    public Answer insertAnswer(Answer answer) {
        return answerRepository.save(answer);
    }

    public List<Answer> getAnswersSortedByScore(Long questionId) {
        List<Answer> answers = answerRepository.findByQuestionIdOrderByCreationDatetimeDesc(questionId);

        Collections.sort(answers, Comparator.comparingInt(answer -> answer.getUpvotes() - answer.getDownvotes()));

        return answers;
    }

    public Answer createAnswer(Long question, String author, String text, String pictureUrl) {
        Answer answer = new Answer();
        Optional<Question> question1 = questionRepository.findById(question);
        if (question1.isPresent()) {
            Optional<User> user = userRepository.findByUsername(author);
            if(user.isPresent()) {
                answer.setQuestion(question1.get());
                answer.setAuthor(user.get());
                answer.setText(text);
                answer.setImageUrl(pictureUrl);
                answer.setCreationDatetime(LocalDateTime.now());
                return answerRepository.save(answer);
            }
            else throw new RuntimeException("User not found");
        }
        else throw new RuntimeException("Question not found");
    }

    public Answer updateAnswer(Long id, String text, String pictureUrl) {
        Optional<Answer> optionalAnswer = answerRepository.findById(id);
        if (optionalAnswer.isPresent()) {
            Answer answer = optionalAnswer.get();
            answer.setText(text);
            answer.setImageUrl(pictureUrl);
            return answerRepository.save(answer);
        } else {
            throw new RuntimeException("Answer not found");
        }
    }

    public Answer upvoteAnswer(Long answerId, String username) {
        Optional<Answer> optionalAnswer = answerRepository.findById(answerId);
        if (optionalAnswer.isPresent()) {
            Answer answer = optionalAnswer.get();
            if (!answer.getAuthor().getUsername().equals(username)) {
                int upvotes = answer.getUpvotes();
                answer.setUpvotes(upvotes + 1);
                return answerRepository.save(answer);
            } else {
                throw new RuntimeException("You cannot upvote your own answer");
            }
        }
        return null;
    }

    public Answer downvoteAnswer(Long answerId, String username) {
        Optional<Answer> optionalAnswer = answerRepository.findById(answerId);
        if (optionalAnswer.isPresent()) {
            Answer answer = optionalAnswer.get();
            if (!answer.getAuthor().getUsername().equals(username)) {
                int downvotes = answer.getDownvotes();
                answer.setDownvotes(downvotes + 1);
                return answerRepository.save(answer);
            } else {
                throw new RuntimeException("You cannot downvote your own answer");
            }
        }
        return null;
    }

    public double calculateAnswerScore(String author) {
        double score = 0;
        Optional<User> user = userRepository.findByUsername(author);
        if(user.isPresent()) {
            List<Answer> userAnswers = answerRepository.findByAuthor(user.get());
            if(!userAnswers.isEmpty())
                for (Answer answer : userAnswers) {
                    int upvotes = answer.getUpvotes();
                    int downvotes = answer.getDownvotes();
                    score += (upvotes * 5) - (downvotes * 2.5);
                }

        }
        return score;
    }

    @Transactional
    public void deleteAnswerById(Long id) {
        if (!answerRepository.existsById(id)) {
            throw new RuntimeException("Answer not found");
        } else {
            answerRepository.deleteById(id);
        }
    }
}