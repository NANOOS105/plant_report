package project.plant_report.domain.community;

import jakarta.persistence.*;
import lombok.Getter;
import project.plant_report.domain.common.DateEntity;

@Entity
@Table(name = "post_images")
@Getter
public class PostImage extends DateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String originalFileName; // 원본 파일명

    @Column(nullable = false)
    private String storedFileName; // 저장된 파일명

    @Column(nullable = false)
    private String filePath; // 파일 경로

    @Column(nullable = false)
    private Long fileSize; // 파일 크기

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post; // 게시글

    //== 생성자 ==
    protected PostImage() {}

    public PostImage(String originalFileName, String storedFileName, String filePath, Long fileSize, Post post) {
        this.originalFileName = originalFileName;
        this.storedFileName = storedFileName;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.post = post;
    }
}
