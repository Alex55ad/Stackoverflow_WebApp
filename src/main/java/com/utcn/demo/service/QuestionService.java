package com.utcn.demo.service;

import com.utcn.demo.entity.Question;
import com.utcn.demo.entity.User;
import com.utcn.demo.entity.VoteType;
import com.utcn.demo.entity.VotedQuestion;
import com.utcn.demo.repository.QuestionRepository;
import com.utcn.demo.repository.UserRepository;
import com.utcn.demo.repository.VotedQuestionRepository;
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
    private final VotedQuestionRepository votedQuestionRepository;

    public List<Question> retrieveQuestions() {
        return questionRepository.findAllByOrderByCreationDatetimeDesc();
    }

    public Question getQuestionById(Long id){
        return questionRepository.getQuestionById(id);
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

    public Question updateQuestion(Question question) {
        Optional<Question> optionalQuestion = questionRepository.findById(question.getId());
        if (optionalQuestion.isPresent()) {
            Question quest = optionalQuestion.get();
            quest.setTitle(question.getTitle());
            quest.setText(question.getText());
            quest.setPictureUrl(question.getPictureUrl());
            quest.setTags(question.getTags());
            return questionRepository.save(quest);
        } else {
            throw new RuntimeException("Question not found");
        }
    }


    @Transactional
    public Question upvoteQuestion(Long questionId, String username) {
        Optional<Question> optionalQuestion = questionRepository.findById(questionId);
        if (optionalQuestion.isPresent()) {
            Question question = optionalQuestion.get();
            if (!question.getAuthor().getUsername().equals(username)) {
                User user = userRepository.findByUsername(username)
                        .orElseThrow(() -> new RuntimeException("User not found"));
                Optional<VotedQuestion> existingVote = votedQuestionRepository.findByUserAndQuestion(user, question);
                if (existingVote.isPresent()) {
                    VotedQuestion votedQuestion = existingVote.get();
                    if (votedQuestion.getVoteType() == VoteType.UPVOTE) {
                        // Remove upvote
                        votedQuestionRepository.delete(votedQuestion);
                        question.setUpvotes(question.getUpvotes() - 1);
                    } else {
                        // Change downvote to upvote
                        votedQuestion.setVoteType(VoteType.UPVOTE);
                        votedQuestionRepository.save(votedQuestion);
                        question.setUpvotes(question.getUpvotes() + 1);
                        question.setDownvotes(question.getDownvotes() - 1);
                    }
                } else {
                    // Insert new upvote
                    VotedQuestion newVote = new VotedQuestion();
                    newVote.setUser(user);
                    newVote.setQuestion(question);
                    newVote.setVoteType(VoteType.UPVOTE);
                    votedQuestionRepository.save(newVote);
                    question.setUpvotes(question.getUpvotes() + 1);
                }
                questionRepository.save(question);
                return question;
            } else {
                throw new RuntimeException("You cannot upvote your own question");
            }
        }
        return null;
    }

    @Transactional
    public Question downvoteQuestion(Long questionId, String username) {
        Optional<Question> optionalQuestion = questionRepository.findById(questionId);
        if (optionalQuestion.isPresent()) {
            Question question = optionalQuestion.get();
            if (!question.getAuthor().getUsername().equals(username)) {
                User user = userRepository.findByUsername(username)
                        .orElseThrow(() -> new RuntimeException("User not found"));
                Optional<VotedQuestion> existingVote = votedQuestionRepository.findByUserAndQuestion(user, question);
                if (existingVote.isPresent()) {
                    VotedQuestion votedQuestion = existingVote.get();
                    if (votedQuestion.getVoteType() == VoteType.DOWNVOTE) {
                        // Remove downvote
                        votedQuestionRepository.delete(votedQuestion);
                        question.setDownvotes(question.getDownvotes() - 1);
                    } else {
                        // Change upvote to downvote
                        votedQuestion.setVoteType(VoteType.DOWNVOTE);
                        votedQuestionRepository.save(votedQuestion);
                        question.setDownvotes(question.getDownvotes() + 1);
                        question.setUpvotes(question.getUpvotes() - 1);
                    }
                } else {
                    // Insert new downvote
                    VotedQuestion newVote = new VotedQuestion();
                    newVote.setUser(user);
                    newVote.setQuestion(question);
                    newVote.setVoteType(VoteType.DOWNVOTE);
                    votedQuestionRepository.save(newVote);
                    question.setDownvotes(question.getDownvotes() + 1);
                }
                questionRepository.save(question);
                return question;
            } else {
                throw new RuntimeException("You cannot downvote your own question");
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