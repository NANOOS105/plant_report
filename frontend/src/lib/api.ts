import axios from 'axios';
import { Plant, PlantSaveRequest, PlantUpdateRequest, Season, PageResponse } from '@/types/plant';

// 1. axios 인스턴스 생성
const api = axios.create({
  baseURL: process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080/api',
  headers: {
    'Content-Type': 'application/json',
  },
  withCredentials: true, // CORS 쿠키 포함
});

// 2. 요청 인터셉터: 토큰 자동 추가
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// 2. 식물 목록 조회 함수
export const getPlants = async (status?: string, page: number = 0, size: number = 20) => {
  const params = new URLSearchParams();
  if (status) params.append('status', status);
  params.append('page', page.toString());
  params.append('size', size.toString());
  params.append('sort', 'lastWateringDate,asc');
  
  const response = await api.get<PageResponse<Plant>>(`/plant?${params}`);
  return response.data;
};

// 3. 식물 등록 함수
export const savePlant = async (data: PlantSaveRequest) => {
  const response = await api.post('/plant', data);
  return response.data;
};

// 4. 물주기 함수
export const waterPlant = async (id: number, season: Season) => {
  const response = await api.post(`/plant/${id}/water?season=${season}`);
  return response.data;
};

// 5. 식물 수정 함수
export const updatePlant = async (id: number, data: PlantUpdateRequest) => {
  const response = await api.put(`/plant/${id}`, data);
  return response.data;
};

// 6. 식물 삭제 함수
export const deletePlant = async (id: number) => {
  const response = await api.delete(`/plant/${id}`);
  return response.data;
};

// 7. 물주기 취소 함수
export const cancelWaterPlant = async (id: number, season: Season) => {
  const response = await api.put(`/plant/${id}/cancelWater?season=${season}`);
  return response.data;
};

// ==================== 커뮤니티 API ====================

import { Post, PostRequest, Comment, CommentRequest, PageResponse as CommunityPageResponse } from '@/types/community';

// 게시글 목록 조회
export const getPosts = async (page: number = 0, size: number = 20) => {
  const response = await api.get<CommunityPageResponse<Post>>(`/community/posts?page=${page}&size=${size}`);
  return response.data;
};

// 카테고리별 게시글 조회
export const getPostsByCategory = async (category: string, page: number = 0, size: number = 20) => {
  const response = await api.get<CommunityPageResponse<Post>>(`/community/posts/category/${category}?page=${page}&size=${size}`);
  return response.data;
};

// 게시글 검색
export const searchPosts = async (keyword: string, page: number = 0, size: number = 20) => {
  const response = await api.get<CommunityPageResponse<Post>>(`/community/posts/search?keyword=${keyword}&page=${page}&size=${size}`);
  return response.data;
};

// 게시글 상세 조회
export const getPost = async (id: number) => {
  const response = await api.get<Post>(`/community/posts/${id}`);
  return response.data;
};

// 게시글 작성
export const createPost = async (data: PostRequest) => {
  const response = await api.post<number>(`/community/posts`, data);
  return response.data;
};

// 게시글 수정
export const updatePost = async (id: number, data: PostRequest) => {
  const response = await api.put(`/community/posts/${id}`, data);
  return response.data;
};

// 게시글 삭제
export const deletePost = async (id: number) => {
  const response = await api.delete(`/community/posts/${id}`);
  return response.data;
};

// 좋아요
export const likePost = async (id: number) => {
  const response = await api.post(`/community/posts/${id}/like`);
  return response.data;
};

// 좋아요 취소
export const unlikePost = async (id: number) => {
  const response = await api.delete(`/community/posts/${id}/like`);
  return response.data;
};

// 댓글 목록 조회
export const getComments = async (postId: number, page: number = 0, size: number = 50) => {
  const response = await api.get<CommunityPageResponse<Comment>>(`/community/posts/${postId}/comments?page=${page}&size=${size}`);
  return response.data;
};

// 댓글 작성
export const createComment = async (postId: number, data: CommentRequest) => {
  const response = await api.post<number>(`/community/posts/${postId}/comments`, data);
  return response.data;
};

// 댓글 수정
export const updateComment = async (postId: number, commentId: number, data: CommentRequest) => {
  const response = await api.put(`/community/posts/${postId}/comments/${commentId}`, data);
  return response.data;
};

// 댓글 삭제
export const deleteComment = async (postId: number, commentId: number) => {
  const response = await api.delete(`/community/posts/${postId}/comments/${commentId}`);
  return response.data;
};