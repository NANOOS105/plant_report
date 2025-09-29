package project.plant_report.service.community;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.plant_report.domain.community.Post;
import project.plant_report.domain.community.PostCategory;
import project.plant_report.domain.community.PostRepository;
import project.plant_report.domain.user.User;
import project.plant_report.domain.user.UserRepository;
import project.plant_report.dto.community.request.PostRequestDto;
import project.plant_report.dto.community.response.PostResponseDto;
import project.plant_report.exception.PostNotFoundException;

@Service
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    // 게시글 작성
    @Transactional
    public Long createPost(PostRequestDto request, Long userId) {
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + userId));

        Post post = new Post(request.getTitle(), request.getContent(), request.getCategory(), author);
        Post savedPost = postRepository.save(post);
        
        return savedPost.getId();
    }

    // 게시글 목록 조회 (전체)
    public Page<PostResponseDto> getAllPosts(Pageable pageable) {
        Page<Post> posts = postRepository.findByOrderByCreatedAtDesc(pageable);
        return posts.map(PostResponseDto::new);
    }

    // 카테고리별 게시글 조회
    public Page<PostResponseDto> getPostsByCategory(PostCategory category, Pageable pageable) {
        Page<Post> posts = postRepository.findByCategory(category, pageable);
        return posts.map(PostResponseDto::new);
    }

    // 게시글 검색
    public Page<PostResponseDto> searchPosts(String keyword, Pageable pageable) {
        Page<Post> posts = postRepository.findByTitleOrContentContaining(keyword, pageable);
        return posts.map(PostResponseDto::new);
    }

    // 카테고리별 + 검색어로 조회
    public Page<PostResponseDto> searchPostsByCategory(PostCategory category, String keyword, Pageable pageable) {
        Page<Post> posts = postRepository.findByCategoryAndTitleOrContentContaining(category, keyword, pageable);
        return posts.map(PostResponseDto::new);
    }

    // 게시글 상세 조회
    public PostResponseDto getPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));
        
        // 조회수 증가
        post.increaseViewCount();
        postRepository.save(post);
        
        return new PostResponseDto(post);
    }

    // 게시글 수정
    @Transactional
    public void updatePost(Long postId, PostRequestDto request, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        // 작성자 확인
        if (!post.getAuthor().getId().equals(userId)) {
            throw new IllegalArgumentException("게시글을 수정할 권한이 없습니다.");
        }

        post.updatePost(request.getTitle(), request.getContent(), request.getCategory());
    }

    // 게시글 삭제
    @Transactional
    public void deletePost(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        // 작성자 확인
        if (!post.getAuthor().getId().equals(userId)) {
            throw new IllegalArgumentException("게시글을 삭제할 권한이 없습니다.");
        }

        postRepository.delete(post);
    }

    // 좋아요 증가
    @Transactional
    public void likePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));
        
        post.increaseLikeCount();
        postRepository.save(post);
    }

    // 좋아요 감소
    @Transactional
    public void unlikePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));
        
        post.decreaseLikeCount();
        postRepository.save(post);
    }
}
