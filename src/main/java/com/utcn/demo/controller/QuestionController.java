package com.utcn.demo.controller;

import com.utcn.demo.entity.Question;
import com.utcn.demo.entity.User;
import com.utcn.demo.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("/questions")
@RestController
@CrossOrigin
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/getAll")
    public List<Question> retrieveAllQuestions() {
        return questionService.retrieveQuestions();
    }

    @PostMapping("/insert")
    public Question insertQuestion(@RequestBody Question question) {
        return questionService.insertQuestion(question);
    }

    @DeleteMapping("/deleteById")
    public void deleteQuestionById(@RequestParam Long id) {
        questionService.deleteQuestionById(id);
    }

    @GetMapping("/byTag/{tag}")
    public List<Question> getQuestionsByTag(@PathVariable String tag) {
        return questionService.getQuestionsByTag(tag);
    }

    @GetMapping("/byUser/{username}")
    public List<Question> getQuestionsByUser(@PathVariable String username) {
        return questionService.getQuestionsByUser(username);
    }

    @PostMapping("/create")
    public Question createQuestion(@RequestParam String author,
                                   @RequestParam String title,
                                   @RequestParam String text,
                                   @RequestParam String pictureUrl,
                                   @RequestParam String tags) {
        return questionService.createQuestion(author, title, text, pictureUrl, tags);
    }

    @PutMapping("/update/{id}")
    public Question updateQuestion(@PathVariable Long id,
                                   @RequestParam String title,
                                   @RequestParam String text,
                                   @RequestParam String pictureUrl,
                                   @RequestParam String tags) {
        return questionService.updateQuestion(id, title, text, pictureUrl, tags);
    }

    @PostMapping("/upvote/{questionId}")
    public Question upvoteQuestion(@PathVariable Long questionId,
                                             @RequestParam String username) {
        return questionService.upvoteQuestion(questionId, username);
    }

    @PostMapping("/downvote/{questionId}")
    public Question downvoteQuestion(@PathVariable Long questionId,
                                               @RequestParam String username) {
        return questionService.downvoteQuestion(questionId, username);
    }

}
