package project.plant_report.domain.community;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    
    // 특정 게시글의 댓글 조회 (최신순)
    Page<Comment> findByPostIdOrderByCreatedAtAsc(Long postId, Pageable pageable);
    
    // 특정 게시글의 댓글 개수 조회
    long countByPostId(Long postId);
    
    // 특정 작성자의 댓글 조회 (페이징)
    Page<Comment> findByAuthorId(Long authorId, Pageable pageable);
    
    // 댓글 내용으로 검색
    @Query("SELECT c FROM Comment c WHERE c.content LIKE %:keyword%")
    Page<Comment> findByContentContaining(@Param("keyword") String keyword, Pageable pageable);
    
    // 특정 게시글의 댓글 내용으로 검색
    @Query("SELECT c FROM Comment c WHERE c.post.id = :postId AND c.content LIKE %:keyword%")
    Page<Comment> findByPostIdAndContentContaining(
        @Param("postId") Long postId, 
        @Param("keyword") String keyword, 
        Pageable pageable
    );
}
