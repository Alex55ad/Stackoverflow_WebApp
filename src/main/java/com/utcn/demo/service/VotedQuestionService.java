package com.utcn.demo.service;

import com.utcn.demo.entity.User;
import com.utcn.demo.entity.Question;
import com.utcn.demo.entity.VoteType;
import com.utcn.demo.entity.VotedQuestion;
import com.utcn.demo.repository.UserRepository;
import com.utcn.demo.repository.QuestionRepository;
import com.utcn.demo.repository.VotedQuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VotedQuestionService {

    private final VotedQuestionRepository votedQuestionRepository;
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;

    @Transactional
    public VotedQuestion insertVotedQuestion(Long userId, Long questionId, String voteType) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));
        VotedQuestion votedQuestion = new VotedQuestion();
        votedQuestion.setUser(user);
        votedQuestion.setQuestion(question);
        votedQuestion.setVoteType(VoteType.valueOf(voteType.toUpperCase()));
        return votedQuestionRepository.save(votedQuestion);
    }

    @Transactional
    public void deleteVotedQuestionById(Long id) {
        if (!votedQuestionRepository.existsById(id)) {
            throw new RuntimeException("Voted question not found");
        } else {
            votedQuestionRepository.deleteById(id);
        }
    }

    @Transactional
    public VotedQuestion updateVotedQuestion(Long id, String voteType) {
        VotedQuestion votedQuestion = votedQuestionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Voted question not found"));
        votedQuestion.setVoteType(VoteType.valueOf(voteType.toUpperCase()));
        return votedQuestionRepository.save(votedQuestion);
    }
}
