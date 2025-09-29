package project.plant_report.domain.community;

import jakarta.persistence.*;
import lombok.Getter;
import project.plant_report.domain.common.DateEntity;
import project.plant_report.domain.user.User;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "posts")
@Getter
public class Post extends DateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title; // 게시글 제목

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content; // 게시글 내용

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PostCategory category; // 카테고리

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User author; // 작성자

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>(); // 댓글 목록

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostImage> images = new ArrayList<>(); // 첨부 이미지

    @Column(nullable = false)
    private Integer viewCount = 0; // 조회수

    @Column(nullable = false)
    private Integer likeCount = 0; // 좋아요 수

    //== 생성자 ==
    protected Post() {}

    public Post(String title, String content, PostCategory category, User author) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.author = author;
    }

    //== 비즈니스 로직 ==

    // 게시글 수정
    public void updatePost(String title, String content, PostCategory category) {
        this.title = title;
        this.content = content;
        this.category = category;
    }

    // 조회수 증가
    public void increaseViewCount() {
        this.viewCount++;
    }

    // 좋아요 수 증가
    public void increaseLikeCount() {
        this.likeCount++;
    }

    // 좋아요 수 감소
    public void decreaseLikeCount() {
        if (this.likeCount > 0) {
            this.likeCount--;
        }
    }
}
