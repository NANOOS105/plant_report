package project.plant_report.service.community;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.plant_report.domain.community.Comment;
import project.plant_report.domain.community.CommentRepository;
import project.plant_report.domain.community.Post;
import project.plant_report.domain.community.PostRepository;
import project.plant_report.domain.user.User;
import project.plant_report.domain.user.UserRepository;
import project.plant_report.dto.community.request.CommentRequestDto;
import project.plant_report.dto.community.response.CommentResponseDto;
import project.plant_report.exception.PostNotFoundException;

@Service
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public CommentService(CommentRepository commentRepository, PostRepository postRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    // 댓글 작성
    @Transactional
    public Long createComment(Long postId, CommentRequestDto request, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));
        
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + userId));

        Comment comment = new Comment(request.getContent(), post, author);
        Comment savedComment = commentRepository.save(comment);
        
        return savedComment.getId();
    }

    // 특정 게시글의 댓글 목록 조회
    public Page<CommentResponseDto> getCommentsByPostId(Long postId, Pageable pageable) {
        Page<Comment> comments = commentRepository.findByPostIdOrderByCreatedAtAsc(postId, pageable);
        return comments.map(CommentResponseDto::new);
    }

    // 댓글 수정
    @Transactional
    public void updateComment(Long commentId, CommentRequestDto request, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다: " + commentId));

        // 작성자 확인
        if (!comment.getAuthor().getId().equals(userId)) {
            throw new IllegalArgumentException("댓글을 수정할 권한이 없습니다.");
        }

        comment.updateComment(request.getContent());
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다: " + commentId));

        // 작성자 확인
        if (!comment.getAuthor().getId().equals(userId)) {
            throw new IllegalArgumentException("댓글을 삭제할 권한이 없습니다.");
        }

        commentRepository.delete(comment);
    }

    // 특정 게시글의 댓글 개수 조회
    public long getCommentCountByPostId(Long postId) {
        return commentRepository.countByPostId(postId);
    }
}
