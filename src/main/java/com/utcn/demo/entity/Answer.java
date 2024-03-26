package com.utcn.demo.entity;

import jakarta.persistence.*;

import java.util.List;

import java.time.LocalDateTime;

@Entity
@Table(name = "answers")
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "question_id", referencedColumnName = "id")
    private Question question;

    @ManyToOne
    @JoinColumn(name = "author", referencedColumnName = "username")
    private User author;

    @Column(name = "title",nullable = false)
    private String title;

    @Column(name = "text",nullable = false, columnDefinition = "TEXT")
    private String text;

    @Column(name = "image_url", nullable = false)
    private String imageUrl = "";

    @Column(name = "creation_datetime", nullable = false)
    private LocalDateTime creationDatetime = LocalDateTime.now();

    @Column(name = "upvotes",nullable = false)
    private int upvotes = 0;

    @Column(name = "downvotes",nullable = false)
    private int downvotes = 0;


    public Answer() {
    }

    public Answer(Long id, Question question, User author, String title, String text, String imageUrl, LocalDateTime creationDatetime, int upvotes, int downvotes) {
        this.id = id;
        this.question = question;
        this.author = author;
        this.title = title;
        this.text = text;
        this.imageUrl = imageUrl;
        this.creationDatetime = creationDatetime;
        this.upvotes = upvotes;
        this.downvotes = downvotes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public LocalDateTime getCreationDatetime() {
        return creationDatetime;
    }

    public void setCreationDatetime(LocalDateTime creationDatetime) {
        this.creationDatetime = creationDatetime;
    }

    public int getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(int upvotes) {
        this.upvotes = upvotes;
    }

    public int getDownvotes() {
        return downvotes;
    }

    public void setDownvotes(int downvotes) {
        this.downvotes = downvotes;
    }
}
