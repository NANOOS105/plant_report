package project.plant_report.dto.community.response;

import lombok.Getter;
import project.plant_report.domain.community.PostImage;

@Getter
public class PostImageResponseDto {
    
    private Long id;
    private String originalFileName;
    private String storedFileName;
    private String filePath;
    private Long fileSize;

    // 기본 생성자
    public PostImageResponseDto() {}

    // PostImage 엔티티로부터 생성
    public PostImageResponseDto(PostImage postImage) {
        this.id = postImage.getId();
        this.originalFileName = postImage.getOriginalFileName();
        this.storedFileName = postImage.getStoredFileName();
        this.filePath = postImage.getFilePath();
        this.fileSize = postImage.getFileSize();
    }
}
