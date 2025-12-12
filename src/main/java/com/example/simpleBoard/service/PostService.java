package com.example.simpleBoard.service;

import com.example.simpleBoard.domain.Post;
import com.example.simpleBoard.dto.PostDtos;
import com.example.simpleBoard.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {
    private final PostRepository postRepository;

    public PostDtos.Response create(PostDtos.CreateRequest request) {
        Post post = Post.create(request.getTitle(), request.getContent(), request.getWriter());
        Post saved = postRepository.save(post);
        return PostDtos.Response.from(saved);
    }

    @Transactional(readOnly = true)
    public PostDtos.Response getById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 없습니다. id=" + id));
        return PostDtos.Response.from(post);
    }

    @Transactional(readOnly = true)
    public PostDtos.ListResponse getList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Post> result = postRepository.findAll(pageable);

        return PostDtos.ListResponse.builder()
                .totalCount(result.getTotalElements())
                .posts(result.getContent()
                        .stream()
                        .map(PostDtos.ListItem::from)
                        .collect(Collectors.toList()))
                .build();
    }

    public PostDtos.Response update(Long id, PostDtos.UpdateRequest request) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 없습니다. id=" + id));
        post.update(request.getTitle(), request.getContent());
        return PostDtos.Response.from(post);
    }

    public void delete(Long id) {
        if (!postRepository.existsById(id)) {
            throw new IllegalArgumentException("이미 삭제되었거나 존재하지 않는 게시글입니다. id=" + id);
        }
        postRepository.deleteById(id);
    }
}
