package com.example.simpleBoard.controller;

import com.example.simpleBoard.dto.PostDtos;
import com.example.simpleBoard.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    @PostMapping
    public PostDtos.Response create(@RequestBody @Valid PostDtos.CreateRequest request) {
        return postService.create(request);
    }

    @GetMapping("/{id}")
    public PostDtos.Response getById(@PathVariable("id") Long id) {
        return postService.getById(id);
    }

    @GetMapping
    public PostDtos.ListResponse getList(
            @RequestParam(value="page", defaultValue = "0") int page,
            @RequestParam(value="size", defaultValue = "10") int size
    ) {
        return postService.getList(page, size);
    }

    @PutMapping("/{id}")
    public PostDtos.Response update(
            @PathVariable("id") Long id,
            @RequestBody @Valid PostDtos.UpdateRequest request
    ) {
        return postService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        postService.delete(id);
    }
}
