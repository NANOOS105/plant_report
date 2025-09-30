import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import {
  getPosts,
  getPostsByCategory,
  searchPosts,
  getPost,
  createPost,
  updatePost,
  deletePost,
  likePost,
  unlikePost,
  getComments,
  createComment,
  updateComment,
  deleteComment,
} from '@/lib/api';
import { PostRequest, CommentRequest } from '@/types/community';

// ==================== 게시글 Hooks ====================

// 게시글 목록 조회
export const usePosts = (page: number = 0, size: number = 20) => {
  return useQuery({
    queryKey: ['posts', page, size],
    queryFn: () => getPosts(page, size),
  });
};

// 카테고리별 게시글 조회
export const usePostsByCategory = (category: string, page: number = 0, size: number = 20) => {
  return useQuery({
    queryKey: ['posts', 'category', category, page, size],
    queryFn: () => getPostsByCategory(category, page, size),
  });
};

// 게시글 검색
export const useSearchPosts = (keyword: string, page: number = 0, size: number = 20) => {
  return useQuery({
    queryKey: ['posts', 'search', keyword, page, size],
    queryFn: () => searchPosts(keyword, page, size),
    enabled: keyword.length > 0, // 검색어가 있을 때만 실행
  });
};

// 게시글 상세 조회
export const usePost = (id: number) => {
  return useQuery({
    queryKey: ['post', id],
    queryFn: () => getPost(id),
  });
};

// 게시글 작성
export const useCreatePost = () => {
  const queryClient = useQueryClient();
  
  return useMutation({
    mutationFn: (data: PostRequest) => createPost(data),
    onSuccess: () => {
      // 게시글 목록 새로고침
      queryClient.invalidateQueries({ queryKey: ['posts'] });
    },
  });
};

// 게시글 수정
export const useUpdatePost = () => {
  const queryClient = useQueryClient();
  
  return useMutation({
    mutationFn: ({ id, data }: { id: number; data: PostRequest }) => updatePost(id, data),
    onSuccess: (_, variables) => {
      // 해당 게시글 + 목록 새로고침
      queryClient.invalidateQueries({ queryKey: ['post', variables.id] });
      queryClient.invalidateQueries({ queryKey: ['posts'] });
    },
  });
};

// 게시글 삭제
export const useDeletePost = () => {
  const queryClient = useQueryClient();
  
  return useMutation({
    mutationFn: (id: number) => deletePost(id),
    onSuccess: () => {
      // 게시글 목록 새로고침
      queryClient.invalidateQueries({ queryKey: ['posts'] });
    },
  });
};

// 좋아요
export const useLikePost = () => {
  const queryClient = useQueryClient();
  
  return useMutation({
    mutationFn: (id: number) => likePost(id),
    onSuccess: (_, id) => {
      // 해당 게시글 새로고침
      queryClient.invalidateQueries({ queryKey: ['post', id] });
      queryClient.invalidateQueries({ queryKey: ['posts'] });
    },
  });
};

// 좋아요 취소
export const useUnlikePost = () => {
  const queryClient = useQueryClient();
  
  return useMutation({
    mutationFn: (id: number) => unlikePost(id),
    onSuccess: (_, id) => {
      // 해당 게시글 새로고침
      queryClient.invalidateQueries({ queryKey: ['post', id] });
      queryClient.invalidateQueries({ queryKey: ['posts'] });
    },
  });
};

// ==================== 댓글 Hooks ====================

// 댓글 목록 조회
export const useComments = (postId: number, page: number = 0, size: number = 50) => {
  return useQuery({
    queryKey: ['comments', postId, page, size],
    queryFn: () => getComments(postId, page, size),
  });
};

// 댓글 작성
export const useCreateComment = () => {
  const queryClient = useQueryClient();
  
  return useMutation({
    mutationFn: ({ postId, data }: { postId: number; data: CommentRequest }) => 
      createComment(postId, data),
    onSuccess: (_, variables) => {
      // 댓글 목록 + 게시글 새로고침 (댓글 개수 업데이트)
      queryClient.invalidateQueries({ queryKey: ['comments', variables.postId] });
      queryClient.invalidateQueries({ queryKey: ['post', variables.postId] });
      queryClient.invalidateQueries({ queryKey: ['posts'] });
    },
  });
};

// 댓글 수정
export const useUpdateComment = () => {
  const queryClient = useQueryClient();
  
  return useMutation({
    mutationFn: ({ postId, commentId, data }: { postId: number; commentId: number; data: CommentRequest }) => 
      updateComment(postId, commentId, data),
    onSuccess: (_, variables) => {
      // 댓글 목록 새로고침
      queryClient.invalidateQueries({ queryKey: ['comments', variables.postId] });
    },
  });
};

// 댓글 삭제
export const useDeleteComment = () => {
  const queryClient = useQueryClient();
  
  return useMutation({
    mutationFn: ({ postId, commentId }: { postId: number; commentId: number }) => 
      deleteComment(postId, commentId),
    onSuccess: (_, variables) => {
      // 댓글 목록 + 게시글 새로고침 (댓글 개수 업데이트)
      queryClient.invalidateQueries({ queryKey: ['comments', variables.postId] });
      queryClient.invalidateQueries({ queryKey: ['post', variables.postId] });
      queryClient.invalidateQueries({ queryKey: ['posts'] });
    },
  });
};
