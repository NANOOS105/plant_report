package project.plant_report.domain.community;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {
    
    // 카테고리별 게시글 조회 (페이징)
    Page<Post> findByCategory(PostCategory category, Pageable pageable);
    
    // 작성자별 게시글 조회 (페이징)
    Page<Post> findByAuthorId(Long authorId, Pageable pageable);
    
    // 제목 또는 내용으로 검색 (페이징)
    @Query("SELECT p FROM Post p WHERE p.title LIKE %:keyword% OR p.content LIKE %:keyword%")
    Page<Post> findByTitleOrContentContaining(@Param("keyword") String keyword, Pageable pageable);
    
    // 카테고리별 + 검색어로 조회 (페이징)
    @Query("SELECT p FROM Post p WHERE p.category = :category AND (p.title LIKE %:keyword% OR p.content LIKE %:keyword%)")
    Page<Post> findByCategoryAndTitleOrContentContaining(
        @Param("category") PostCategory category, 
        @Param("keyword") String keyword, 
        Pageable pageable
    );
    
    // 조회수 기준 인기 게시글 조회 (페이징)
    Page<Post> findByOrderByViewCountDesc(Pageable pageable);
    
    // 최신 게시글 조회 (기본 정렬)
    Page<Post> findByOrderByCreatedAtDesc(Pageable pageable);
}
