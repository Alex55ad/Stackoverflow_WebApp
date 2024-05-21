package com.utcn.demo.service;

import com.utcn.demo.entity.*;
import com.utcn.demo.repository.AnswerRepository;
import com.utcn.demo.repository.QuestionRepository;
import com.utcn.demo.repository.UserRepository;
import com.utcn.demo.repository.VotedAnswerRepository;
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
    private final VotedAnswerRepository votedAnswerRepository;

    public List<Answer> retrieveAnswers() {
        return answerRepository.findAll();
    }

    public Answer getAnswerById(Long id){
        return answerRepository.getAnswerById(id);
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

    public Answer updateAnswer(Answer answer) {
        Optional<Answer> optionalAnswer = answerRepository.findById(answer.getId());
        if (optionalAnswer.isPresent()) {
            Answer ans = optionalAnswer.get();
            ans.setTitle(answer.getTitle());
            ans.setText(answer.getText());
            ans.setImageUrl(answer.getImageUrl());
            return answerRepository.save(ans);
        } else {
            throw new RuntimeException("Answer not found");
        }
    }

    @Transactional
    public Answer upvoteAnswer(Long answerId, String username) {
        Optional<Answer> optionalAnswer = answerRepository.findById(answerId);
        if (optionalAnswer.isPresent()) {
            Answer answer = optionalAnswer.get();
            if (!answer.getAuthor().getUsername().equals(username)) {
                User user = userRepository.findByUsername(username)
                        .orElseThrow(() -> new RuntimeException("User not found"));
                Optional<VotedAnswer> existingVote = votedAnswerRepository.findByUserAndAnswer(user, answer);
                if (existingVote.isPresent()) {
                    VotedAnswer votedAnswer = existingVote.get();
                    if (votedAnswer.getVoteType() == VoteType.UPVOTE) {
                        // Remove upvote
                        votedAnswerRepository.delete(votedAnswer);
                        answer.setUpvotes(answer.getUpvotes() - 1);
                    } else {
                        // Change downvote to upvote
                        votedAnswer.setVoteType(VoteType.UPVOTE);
                        votedAnswerRepository.save(votedAnswer);
                        answer.setUpvotes(answer.getUpvotes() + 1);
                        answer.setDownvotes(answer.getDownvotes() - 1);
                    }
                } else {
                    // Insert new upvote
                    VotedAnswer newVote = new VotedAnswer();
                    newVote.setUser(user);
                    newVote.setAnswer(answer);
                    newVote.setVoteType(VoteType.UPVOTE);
                    votedAnswerRepository.save(newVote);
                    answer.setUpvotes(answer.getUpvotes() + 1);
                }
                answerRepository.save(answer);
                return answer;
            } else {
                throw new RuntimeException("You cannot upvote your own answer");
            }
        }
        return null;
    }

    @Transactional
    public Answer downvoteAnswer(Long answerId, String username) {
        Optional<Answer> optionalAnswer = answerRepository.findById(answerId);
        if (optionalAnswer.isPresent()) {
            Answer answer = optionalAnswer.get();
            if (!answer.getAuthor().getUsername().equals(username)) {
                User user = userRepository.findByUsername(username)
                        .orElseThrow(() -> new RuntimeException("User not found"));
                Optional<VotedAnswer> existingVote = votedAnswerRepository.findByUserAndAnswer(user, answer);
                if (existingVote.isPresent()) {
                    VotedAnswer votedAnswer = existingVote.get();
                    if (votedAnswer.getVoteType() == VoteType.DOWNVOTE) {
                        // Remove downvote
                        votedAnswerRepository.delete(votedAnswer);
                        answer.setDownvotes(answer.getDownvotes() - 1);
                    } else {
                        // Change upvote to downvote
                        votedAnswer.setVoteType(VoteType.DOWNVOTE);
                        votedAnswerRepository.save(votedAnswer);
                        answer.setDownvotes(answer.getDownvotes() + 1);
                        answer.setUpvotes(answer.getUpvotes() - 1);
                    }
                } else {
                    // Insert new downvote
                    VotedAnswer newVote = new VotedAnswer();
                    newVote.setUser(user);
                    newVote.setAnswer(answer);
                    newVote.setVoteType(VoteType.DOWNVOTE);
                    votedAnswerRepository.save(newVote);
                    answer.setDownvotes(answer.getDownvotes() + 1);
                }
                answerRepository.save(answer);
                return answer;
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