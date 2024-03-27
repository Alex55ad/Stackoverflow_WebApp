package com.utcn.demo.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "questions")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "author", referencedColumnName = "username")
    private User author;

    @Column(name = "title",nullable = false)
    private String title;

    @Column(name = "text",nullable = false, columnDefinition = "TEXT")
    private String text;

    @Column(name = "creation_datetime", nullable = false)
    private LocalDateTime creationDatetime = LocalDateTime.now();

    @Column(name = "picture_url", nullable = false)
    private String pictureUrl = "";

    @Column(name = "tags")
    private String tags;

    @Column(name = "upvotes",nullable = false)
    private int upvotes = 0;

    @Column(name = "downvotes",nullable = false)
    private int downvotes = 0;
/*
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Answer> answers;

 */

    public Question() {
    }

    public Question(Long id, User author, String title, String text, LocalDateTime creationDatetime, String pictureUrl, String tags, int upvotes, int downvotes) {
        this.id = id;
        this.author = author;
        this.title = title;
        this.text = text;
        this.creationDatetime = creationDatetime;
        this.pictureUrl = pictureUrl;
        this.tags = tags;
        this.upvotes = upvotes;
        this.downvotes = downvotes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDateTime getCreationDatetime() {
        return creationDatetime;
    }

    public void setCreationDatetime(LocalDateTime creationDatetime) {
        this.creationDatetime = creationDatetime;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
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
