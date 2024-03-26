package com.utcn.demo.controller;

import com.utcn.demo.entity.Answer;
import com.utcn.demo.entity.Question;
import com.utcn.demo.entity.User;
import com.utcn.demo.service.AnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("/answers")
@RestController
public class AnswerController {

    @Autowired
    private AnswerService answerService;

    @GetMapping("/getAllAnswers")
    public List<Answer> retrieveAllAnswers() {
        return answerService.retrieveAnswers();
    }

    @GetMapping("/{questionId}/sortedByScore")
    public List<Answer> getAnswersSortedByScore(@PathVariable Long questionId) {
        return answerService.getAnswersSortedByScore(questionId);
    }

    @PostMapping("/create")
    public Answer createAnswer(@RequestBody Question question,
                               @RequestBody User author,
                               @RequestParam String text,
                               @RequestParam String pictureUrl) {
        return answerService.createAnswer(question, author, text, pictureUrl);
    }

    @PutMapping("/{id}/update")
    public Answer updateAnswer(@PathVariable Long id,
                               @RequestParam String text,
                               @RequestParam String pictureUrl) {
        return answerService.updateAnswer(id, text, pictureUrl);
    }

    @PostMapping("/insertAnswer")
    public Answer insertAnswer(@RequestBody Answer answer) {
        return answerService.insertAnswer(answer);
    }

    @PostMapping("/{answerId}/upvote")
    public Answer upvoteAnswer(@PathVariable Long answerId,
                                         @RequestParam String username) {
        return answerService.upvoteAnswer(answerId, username);
    }

    @PostMapping("/{answerId}/downvote")
    public Answer downvoteAnswer(@PathVariable Long answerId,
                                           @RequestParam String username) {
        return answerService.downvoteAnswer(answerId, username);
    }

    @DeleteMapping("/deleteAnswerById")
    public void deleteAnswerById(@RequestParam Long id) {
        answerService.deleteAnswerById(id);
    }

}
