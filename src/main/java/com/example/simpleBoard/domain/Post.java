package com.example.simpleBoard.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@ToString
@Table(name="posts")
public class Post {
    /**
     * POSTS
     * ------------------------------
     * ID (PK)
     * TITLE
     * CONTENT
     * WRITER
     * CREATED_AT
     * UPDATED_AT
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Lob
    @Column(nullable = false, length = 4000)
    private String content;

    @Column(nullable = false, length = 50)
    private String writer;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;


    /**
     * 생성용 팩토리 메서드 (new Post() 대신 깔끔하게)
     */
    public static Post create(String title, String content, String writer){
        Post post = new Post();
        post.title = title;
        post.content = content;
        post.writer = writer;
        return post;
    }

    /**
     * 수정로직
     */
    public void update(String title, String content){
        this.title = title;
        this.content = content;
    }
}
