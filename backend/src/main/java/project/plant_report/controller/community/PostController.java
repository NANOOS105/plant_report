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
import project.plant_report.domain.community.PostCategory;
import project.plant_report.dto.community.request.PostRequestDto;
import project.plant_report.dto.community.response.PostResponseDto;
import project.plant_report.service.community.PostService;

@RestController
@RequestMapping("/api/community/posts")
@CrossOrigin(origins = {"http://localhost:3000"})
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    // 게시글 작성
    @PostMapping
    public ResponseEntity<Long> createPost(
            @Valid @RequestBody PostRequestDto request,
            Authentication authentication) {
        Long userId = (Long) authentication.getDetails();
        Long postId = postService.createPost(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(postId);
    }

    // 게시글 목록 조회 (전체)
    @GetMapping
    public ResponseEntity<Page<PostResponseDto>> getAllPosts(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<PostResponseDto> posts = postService.getAllPosts(pageable);
        return ResponseEntity.ok(posts);
    }

    // 카테고리별 게시글 조회
    @GetMapping("/category/{category}")
    public ResponseEntity<Page<PostResponseDto>> getPostsByCategory(
            @PathVariable PostCategory category,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<PostResponseDto> posts = postService.getPostsByCategory(category, pageable);
        return ResponseEntity.ok(posts);
    }

    // 게시글 검색
    @GetMapping("/search")
    public ResponseEntity<Page<PostResponseDto>> searchPosts(
            @RequestParam String keyword,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<PostResponseDto> posts = postService.searchPosts(keyword, pageable);
        return ResponseEntity.ok(posts);
    }

    // 카테고리별 + 검색
    @GetMapping("/category/{category}/search")
    public ResponseEntity<Page<PostResponseDto>> searchPostsByCategory(
            @PathVariable PostCategory category,
            @RequestParam String keyword,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<PostResponseDto> posts = postService.searchPostsByCategory(category, keyword, pageable);
        return ResponseEntity.ok(posts);
    }

    // 게시글 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDto> getPost(@PathVariable Long id) {
        PostResponseDto post = postService.getPost(id);
        return ResponseEntity.ok(post);
    }

    // 게시글 수정
    @PutMapping("/{id}")
    public ResponseEntity<Void> updatePost(
            @PathVariable Long id,
            @Valid @RequestBody PostRequestDto request,
            Authentication authentication) {
        Long userId = (Long) authentication.getDetails();
        postService.updatePost(id, request, userId);
        return ResponseEntity.noContent().build();
    }

    // 게시글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(
            @PathVariable Long id,
            Authentication authentication) {
        Long userId = (Long) authentication.getDetails();
        postService.deletePost(id, userId);
        return ResponseEntity.noContent().build();
    }

    // 좋아요 증가
    @PostMapping("/{id}/like")
    public ResponseEntity<Void> likePost(@PathVariable Long id) {
        postService.likePost(id);
        return ResponseEntity.ok().build();
    }

    // 좋아요 감소
    @DeleteMapping("/{id}/like")
    public ResponseEntity<Void> unlikePost(@PathVariable Long id) {
        postService.unlikePost(id);
        return ResponseEntity.ok().build();
    }
}
