package com.utcn.demo.controller;

import com.utcn.demo.entity.Answer;
import com.utcn.demo.entity.Question;
import com.utcn.demo.entity.User;
import com.utcn.demo.service.AnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("/answers")
@RestController
@CrossOrigin
@RequiredArgsConstructor
public class AnswerController {
    private final AnswerService answerService;

    @GetMapping("/getAll")
    public List<Answer> retrieveAllAnswers() {
        return answerService.retrieveAnswers();
    }

    @GetMapping("/sortedByScore/{questionId}")
    public List<Answer> getAnswersSortedByScore(@PathVariable Long questionId) {
        return answerService.getAnswersSortedByScore(questionId);
    }

    @PostMapping("/create")
    public Answer createAnswer(@RequestParam Long question,
                               @RequestParam String author,
                               @RequestParam String text,
                               @RequestParam String pictureUrl) {
        return answerService.createAnswer(question, author, text, pictureUrl);
    }

    @PutMapping("/update")
    public Answer updateAnswer(@RequestBody Answer answer) {
        return answerService.updateAnswer(answer);
    }

    @PostMapping("/insert")
    public Answer insertAnswer(@RequestBody Answer answer) {
        return answerService.insertAnswer(answer);
    }

    @PostMapping("/upvote/{answerId}")
    public Answer upvoteAnswer(@PathVariable Long answerId,
                                         @RequestParam String username) {
        return answerService.upvoteAnswer(answerId, username);
    }

    @PostMapping("/downvote/{answerId}")
    public Answer downvoteAnswer(@PathVariable Long answerId,
                                           @RequestParam String username) {
        return answerService.downvoteAnswer(answerId, username);
    }

    @GetMapping("/getById/{answerId}")
    public Answer getAnswerById(@PathVariable Long answerId){
       return answerService.getAnswerById(answerId);
    }

    @DeleteMapping("/deleteById")
    public void deleteAnswerById(@RequestParam Long id) {
        answerService.deleteAnswerById(id);
    }

}
