package project.plant_report.domain.community;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {
    
    // 특정 게시글의 모든 이미지 조회
    List<PostImage> findByPostId(Long postId);
    
    // 특정 게시글의 이미지 삭제
    void deleteByPostId(Long postId);
}
