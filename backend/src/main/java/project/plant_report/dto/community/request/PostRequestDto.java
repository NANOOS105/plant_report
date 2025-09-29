package project.plant_report.dto.community.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import project.plant_report.domain.community.PostCategory;

@Getter
public class PostRequestDto {

    @NotBlank(message = "제목은 필수입니다")
    @Size(max = 200, message = "제목은 200자를 초과할 수 없습니다")
    private String title; // 게시글 제목

    @NotBlank(message = "내용은 필수입니다")
    @Size(max = 5000, message = "내용은 5000자를 초과할 수 없습니다")
    private String content; // 게시글 내용

    @NotNull(message = "카테고리는 필수입니다")
    private PostCategory category; // 카테고리

    // 기본 생성자
    public PostRequestDto() {}

    // 생성자
    public PostRequestDto(String title, String content, PostCategory category) {
        this.title = title;
        this.content = content;
        this.category = category;
    }
}
