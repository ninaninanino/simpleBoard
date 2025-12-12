package com.example.simpleBoard.repository;

import com.example.simpleBoard.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
