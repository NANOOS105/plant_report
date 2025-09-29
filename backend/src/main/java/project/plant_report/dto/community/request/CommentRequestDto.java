package project.plant_report.dto.community.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class CommentRequestDto {

    @NotBlank(message = "댓글 내용은 필수입니다")
    @Size(max = 1000, message = "댓글은 1000자를 초과할 수 없습니다")
    private String content; // 댓글 내용

    // 기본 생성자
    public CommentRequestDto() {}

    // 생성자
    public CommentRequestDto(String content) {
        this.content = content;
    }
}
