package project.plant_report.controller.community;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import project.plant_report.dto.community.request.CommentRequestDto;
import project.plant_report.dto.community.response.CommentResponseDto;
import project.plant_report.service.community.CommentService;

@RestController
@RequestMapping("/api/community/posts/{postId}/comments")
@CrossOrigin(origins = {"http://localhost:3000"})
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // 댓글 작성
    @PostMapping
    public ResponseEntity<Long> createComment(
            @PathVariable Long postId,
            @Valid @RequestBody CommentRequestDto request,
            Authentication authentication) {
        Long userId = (Long) authentication.getDetails();
        Long commentId = commentService.createComment(postId, request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(commentId);
    }

    // 특정 게시글의 댓글 목록 조회
    @GetMapping
    public ResponseEntity<Page<CommentResponseDto>> getCommentsByPostId(
            @PathVariable Long postId,
            @PageableDefault(size = 50, sort = "createdAt", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<CommentResponseDto> comments = commentService.getCommentsByPostId(postId, pageable);
        return ResponseEntity.ok(comments);
    }

    // 댓글 수정
    @PutMapping("/{commentId}")
    public ResponseEntity<Void> updateComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @Valid @RequestBody CommentRequestDto request,
            Authentication authentication) {
        Long userId = (Long) authentication.getDetails();
        commentService.updateComment(commentId, request, userId);
        return ResponseEntity.noContent().build();
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            Authentication authentication) {
        Long userId = (Long) authentication.getDetails();
        commentService.deleteComment(commentId, userId);
        return ResponseEntity.noContent().build();
    }

    // 특정 게시글의 댓글 개수 조회
    @GetMapping("/count")
    public ResponseEntity<Long> getCommentCount(@PathVariable Long postId) {
        long count = commentService.getCommentCountByPostId(postId);
        return ResponseEntity.ok(count);
    }
}
