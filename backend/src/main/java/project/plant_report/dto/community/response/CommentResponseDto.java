package project.plant_report.dto.community.response;

import lombok.Getter;
import project.plant_report.domain.community.Comment;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {
    
    private Long id;
    private String content;
    private Long postId;
    private Long authorId;
    private String authorName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 기본 생성자
    public CommentResponseDto() {}

    // Comment 엔티티로부터 생성
    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.postId = comment.getPost().getId();
        this.authorId = comment.getAuthor().getId();
        this.authorName = comment.getAuthor().getName();
        this.createdAt = comment.getCreatedAt();
        this.updatedAt = comment.getUpdatedAt();
    }
}
