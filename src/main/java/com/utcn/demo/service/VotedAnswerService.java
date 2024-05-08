package com.utcn.demo.service;

import com.utcn.demo.entity.User;
import com.utcn.demo.entity.Answer;
import com.utcn.demo.entity.VoteType;
import com.utcn.demo.entity.VotedAnswer;
import com.utcn.demo.repository.UserRepository;
import com.utcn.demo.repository.AnswerRepository;
import com.utcn.demo.repository.VotedAnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VotedAnswerService {

    private final VotedAnswerRepository votedAnswerRepository;
    private final UserRepository userRepository;
    private final AnswerRepository answerRepository;

    @Transactional
    public VotedAnswer insertVotedAnswer(Long userId, Long answerId, String voteType) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new RuntimeException("Answer not found"));
        VotedAnswer votedAnswer = new VotedAnswer();
        votedAnswer.setUser(user);
        votedAnswer.setAnswer(answer);
        votedAnswer.setVoteType(VoteType.valueOf(voteType.toUpperCase()));
        return votedAnswerRepository.save(votedAnswer);
    }

    @Transactional
    public void deleteVotedAnswerById(Long id) {
        if (!votedAnswerRepository.existsById(id)) {
            throw new RuntimeException("Voted answer not found");
        } else {
            votedAnswerRepository.deleteById(id);
        }
    }

    @Transactional
    public VotedAnswer updateVotedAnswer(Long id, String voteType) {
        VotedAnswer votedAnswer = votedAnswerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Voted answer not found"));
        votedAnswer.setVoteType(VoteType.valueOf(voteType.toUpperCase()));
        return votedAnswerRepository.save(votedAnswer);
    }
}
