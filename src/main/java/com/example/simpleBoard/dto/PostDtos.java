package com.example.simpleBoard.dto;

import com.example.simpleBoard.domain.Post;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class PostDtos {

/**
 * - 생성 요청 : PostDtos.CreateRequest
 * - 수정 요청 : PostDtos.UpdateRequest
 * - 단건 응답 : PostDtos.Response
 * - 목록 응답 : PostDtos.ListResponse + PostDtos.ListItem
 */


    /**
     * 생성 요청 : PostDtos.CreateRequest
     */
    @Getter
    @NoArgsConstructor
    public static class CreateRequest{
        @NotBlank
        @Size(max=200)
        private String title;

        @NotBlank
        private String content;

        @NotBlank
        @Size(max=50)
        private String writer;

        @Builder
        public CreateRequest(String title, String content, String writer){
            this.title = title;
            this.content = content;
            this.writer = writer;
        }

    }

    /**
     * 수정 요청 : PostDtos.UpdateRequest
     */
    @Getter
    @NoArgsConstructor
    public static class UpdateRequest {
        @NotBlank
        @Size(max=200)
        private String title;

        @NotBlank
        private String content;

        @Builder
        public UpdateRequest(String title, String content){
            this.title = title;
            this.content = content;
        }

    }

    /**
     * 단건 응답 : PostDtos.Response
     */
    @Getter
    @Builder
    public static class Response {
        private Long id;
        private String title;
        private String content;
        private String writer;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static Response from(Post post) {
            return Response.builder()
                    .id(post.getId())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .writer(post.getWriter())
                    .createdAt(post.getCreatedAt())
                    .updatedAt(post.getUpdatedAt())
                    .build();
        }
    }

    /**
     * 목록 응답 : PostDtos.ListResponse + PostDtos.ListItem
     */
    @Getter
    @Builder
    public static class ListItem {
        private Long id;
        private String title;
        private String writer;
        private LocalDateTime createdAt;

        public static ListItem from(Post post){
            return ListItem.builder()
                    .id(post.getId())
                    .title(post.getTitle())
                    .writer(post.getWriter())
                    .createdAt(post.getCreatedAt())
                    .build();
        }

    }

    /**
     * 목록 응답 : PostDtos.ListResponse + PostDtos.ListItem
     */
    @Getter
    @Builder
    public static class ListResponse {
        private long totalCount;
        private List<ListItem> posts;
    }
}
