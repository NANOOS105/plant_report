package project.plant_report.domain.community;

import jakarta.persistence.*;
import lombok.Getter;
import project.plant_report.domain.common.DateEntity;
import project.plant_report.domain.user.User;

@Entity
@Table(name = "comments")
@Getter
public class Comment extends DateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content; // 댓글 내용

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post; // 게시글

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User author; // 작성자

    //== 생성자 ==
    protected Comment() {}

    public Comment(String content, Post post, User author) {
        this.content = content;
        this.post = post;
        this.author = author;
    }

    //== 비즈니스 로직 ==

    // 댓글 수정
    public void updateComment(String content) {
        this.content = content;
    }
}
