// 게시글 카테고리
export type PostCategory = 'PLANT_STORY' | 'QNA';

// 게시글 응답 DTO
export interface Post {
  id: number;
  title: string;
  content: string;
  category: PostCategory;
  categoryDisplayName: string;
  authorId: number;
  authorName: string;
  viewCount: number;
  likeCount: number;
  commentCount: number;
  createdAt: string;
  updatedAt: string;
  images: PostImage[];
}

// 게시글 이미지
export interface PostImage {
  id: number;
  originalFileName: string;
  storedFileName: string;
  filePath: string;
  fileSize: number;
}

// 게시글 작성/수정 요청 DTO
export interface PostRequest {
  title: string;
  content: string;
  category: PostCategory;
}

// 댓글 응답 DTO
export interface Comment {
  id: number;
  content: string;
  postId: number;
  authorId: number;
  authorName: string;
  createdAt: string;
  updatedAt: string;
}

// 댓글 작성/수정 요청 DTO
export interface CommentRequest {
  content: string;
}

// 페이지 응답 (백엔드 Page 객체)
export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
  first: boolean;
  last: boolean;
}

// 카테고리 정보
export const POST_CATEGORIES = {
  PLANT_STORY: '식물 관련 이야기',
  QNA: 'Q&A',
} as const;
