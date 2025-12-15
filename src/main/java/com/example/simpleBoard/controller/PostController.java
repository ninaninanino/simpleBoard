package com.example.simpleBoard.controller;

import com.example.simpleBoard.dto.PostDtos;
import com.example.simpleBoard.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

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
    public ResponseEntity<?> getById(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(postService.getById(id));
        } catch (IllegalArgumentException e) { // 서비스에서 "게시글이 없습니다" 던지는 케이스
            Map<String, Object> body = new LinkedHashMap<>();
            body.put("status", 404);
            body.put("error", "Not Found");
            body.put("message", e.getMessage());
            body.put("path", "/api/posts/" + id);
            return ResponseEntity.status(404).body(body);
        }
    }

    @GetMapping
    public PostDtos.ListResponse getList(
            @RequestParam(value="page", defaultValue = "0") int page,
            @RequestParam(value="size", defaultValue = "10") int size
    ) {
        return postService.getList(page, size);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable("id") Long id,
            @RequestBody @Valid PostDtos.UpdateRequest request
    ) {
        try {
            return ResponseEntity.ok(postService.update(id, request));
        } catch (IllegalArgumentException e) {
            Map<String, Object> body = new LinkedHashMap<>();
            body.put("status", 404);
            body.put("error", "Not Found");
            body.put("message", e.getMessage());
            body.put("path", "/api/posts/" + id);
            return ResponseEntity.status(404).body(body);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        try {
            postService.delete(id);
            return ResponseEntity.noContent().build(); // 204
        } catch (IllegalArgumentException e) {
            Map<String, Object> body = new LinkedHashMap<>();
            body.put("status", 404);
            body.put("error", "Not Found");
            body.put("message", e.getMessage());
            body.put("path", "/api/posts/" + id);
            return ResponseEntity.status(404).body(body);
        }
    }
}
