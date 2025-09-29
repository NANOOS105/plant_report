package project.plant_report.service.community;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import project.plant_report.domain.community.Post;
import project.plant_report.domain.community.PostCategory;
import project.plant_report.domain.community.PostRepository;
import project.plant_report.domain.user.User;
import project.plant_report.domain.user.UserRepository;
import project.plant_report.dto.community.request.PostRequestDto;
import project.plant_report.dto.community.response.PostResponseDto;
import project.plant_report.exception.PostNotFoundException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PostService 테스트")
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PostService postService;

    private User testUser;
    private Post testPost;
    private PostRequestDto testRequest;

    @BeforeEach
    void setUp() {
        // 테스트용 사용자 생성
        testUser = new User("테스트사용자", "test@example.com", "password123");
        
        // 테스트용 게시글 생성
        testPost = new Post("테스트 제목", "테스트 내용", PostCategory.PLANT_STORY, testUser);
        
        // 테스트용 요청 DTO 생성
        testRequest = new PostRequestDto("테스트 제목", "테스트 내용", PostCategory.PLANT_STORY);
    }

    @Test
    @DisplayName("게시글 작성 성공")
    void createPost_Success() {
        // Given
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(testUser));
        when(postRepository.save(any(Post.class))).thenReturn(testPost);

        // When
        Long postId = postService.createPost(testRequest, 1L);

        // Then
        assertThat(postId).isNotNull();
        verify(userRepository).findById(1L);
        verify(postRepository).save(any(Post.class));
    }

    @Test
    @DisplayName("존재하지 않는 사용자로 게시글 작성 시 예외 발생")
    void createPost_UserNotFound_ThrowsException() {
        // Given
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> postService.createPost(testRequest, 999L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("사용자를 찾을 수 없습니다");
    }

    @Test
    @DisplayName("게시글 목록 조회 성공")
    void getAllPosts_Success() {
        // Given
        List<Post> posts = Arrays.asList(testPost);
        Page<Post> postPage = new PageImpl<>(posts);
        when(postRepository.findByOrderByCreatedAtDesc(any(Pageable.class))).thenReturn(postPage);

        // When
        Page<PostResponseDto> result = postService.getAllPosts(PageRequest.of(0, 10));

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getTitle()).isEqualTo("테스트 제목");
        verify(postRepository).findByOrderByCreatedAtDesc(any(Pageable.class));
    }

    @Test
    @DisplayName("카테고리별 게시글 조회 성공")
    void getPostsByCategory_Success() {
        // Given
        List<Post> posts = Arrays.asList(testPost);
        Page<Post> postPage = new PageImpl<>(posts);
        when(postRepository.findByCategory(PostCategory.PLANT_STORY, any(Pageable.class))).thenReturn(postPage);

        // When
        Page<PostResponseDto> result = postService.getPostsByCategory(PostCategory.PLANT_STORY, PageRequest.of(0, 10));

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        verify(postRepository).findByCategory(PostCategory.PLANT_STORY, any(Pageable.class));
    }

    @Test
    @DisplayName("게시글 상세 조회 성공")
    void getPost_Success() {
        // Given
        when(postRepository.findById(1L)).thenReturn(Optional.of(testPost));
        when(postRepository.save(any(Post.class))).thenReturn(testPost);

        // When
        PostResponseDto result = postService.getPost(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("테스트 제목");
        assertThat(result.getContent()).isEqualTo("테스트 내용");
        verify(postRepository).findById(1L);
        verify(postRepository).save(any(Post.class)); // 조회수 증가로 인한 저장
    }

    @Test
    @DisplayName("존재하지 않는 게시글 조회 시 예외 발생")
    void getPost_NotFound_ThrowsException() {
        // Given
        when(postRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> postService.getPost(999L))
                .isInstanceOf(PostNotFoundException.class);
    }

    @Test
    @DisplayName("게시글 수정 성공")
    void updatePost_Success() {
        // Given
        PostRequestDto updateRequest = new PostRequestDto("수정된 제목", "수정된 내용", PostCategory.QNA);
        when(postRepository.findById(1L)).thenReturn(Optional.of(testPost));

        // When
        postService.updatePost(1L, updateRequest, 1L);

        // Then
        verify(postRepository).findById(1L);
        // updatePost 메서드가 호출되었는지 확인 (실제로는 엔티티의 메서드가 호출됨)
    }

    @Test
    @DisplayName("권한 없는 사용자가 게시글 수정 시 예외 발생")
    void updatePost_Unauthorized_ThrowsException() {
        // Given
        PostRequestDto updateRequest = new PostRequestDto("수정된 제목", "수정된 내용", PostCategory.QNA);
        when(postRepository.findById(1L)).thenReturn(Optional.of(testPost));

        // When & Then
        assertThatThrownBy(() -> postService.updatePost(1L, updateRequest, 999L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("게시글을 수정할 권한이 없습니다");
    }

    @Test
    @DisplayName("게시글 삭제 성공")
    void deletePost_Success() {
        // Given
        when(postRepository.findById(1L)).thenReturn(Optional.of(testPost));

        // When
        postService.deletePost(1L, 1L);

        // Then
        verify(postRepository).findById(1L);
        verify(postRepository).delete(testPost);
    }

    @Test
    @DisplayName("권한 없는 사용자가 게시글 삭제 시 예외 발생")
    void deletePost_Unauthorized_ThrowsException() {
        // Given
        when(postRepository.findById(1L)).thenReturn(Optional.of(testPost));

        // When & Then
        assertThatThrownBy(() -> postService.deletePost(1L, 999L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("게시글을 삭제할 권한이 없습니다");
    }

    @Test
    @DisplayName("좋아요 증가 성공")
    void likePost_Success() {
        // Given
        when(postRepository.findById(1L)).thenReturn(Optional.of(testPost));
        when(postRepository.save(any(Post.class))).thenReturn(testPost);

        // When
        postService.likePost(1L);

        // Then
        verify(postRepository).findById(1L);
        verify(postRepository).save(any(Post.class));
    }

    @Test
    @DisplayName("좋아요 감소 성공")
    void unlikePost_Success() {
        // Given
        when(postRepository.findById(1L)).thenReturn(Optional.of(testPost));
        when(postRepository.save(any(Post.class))).thenReturn(testPost);

        // When
        postService.unlikePost(1L);

        // Then
        verify(postRepository).findById(1L);
        verify(postRepository).save(any(Post.class));
    }
}
