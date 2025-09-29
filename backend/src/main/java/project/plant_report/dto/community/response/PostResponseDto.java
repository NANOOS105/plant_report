package project.plant_report.dto.community.response;

import lombok.Getter;
import project.plant_report.domain.community.Post;
import project.plant_report.domain.community.PostCategory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PostResponseDto {
    
    private Long id;
    private String title;
    private String content;
    private PostCategory category;
    private String categoryDisplayName;
    private Long authorId;
    private String authorName;
    private Integer viewCount;
    private Integer likeCount;
    private Integer commentCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<PostImageResponseDto> images;

    // 기본 생성자
    public PostResponseDto() {}

    // Post 엔티티로부터 생성
    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.category = post.getCategory();
        this.categoryDisplayName = post.getCategory().getDisplayName();
        this.authorId = post.getAuthor().getId();
        this.authorName = post.getAuthor().getName();
        this.viewCount = post.getViewCount();
        this.likeCount = post.getLikeCount();
        this.commentCount = post.getComments().size(); // 댓글 개수
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
        
        // 이미지 목록 변환
        this.images = post.getImages().stream()
                .map(PostImageResponseDto::new)
                .collect(Collectors.toList());
    }
}
