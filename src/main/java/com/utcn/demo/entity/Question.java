package com.utcn.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
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

    public String getFormattedCreationDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        return creationDatetime.format(formatter);
    }

}
