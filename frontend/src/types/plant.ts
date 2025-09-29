// 백엔드 PlantResponseDto와 일치하는 타입
export interface Plant {
  id: number;
  name: string;
  lastWateringDate?: string; // ISO 날짜 문자열
  commonInterval?: number;
  summerInterval?: number;
  winterInterval?: number;
  notes?: string;
}

// 백엔드 PlantSaveRequestDto와 일치하는 타입
export interface PlantSaveRequest {
  name: string;
  commonInterval: number;
  summerInterval?: number;
  winterInterval?: number;
  lastWateringDate?: string;
  season: Season; // 등록 시점의 계절
  user: any; // 임시로 any 사용 (나중에 User 타입 정의)
  notes?: string;
}

// 백엔드 PlantUpdateRequestDto와 일치하는 타입
export interface PlantUpdateRequest {
  name?: string;
  commonInterval?: number;
  summerInterval?: number;
  winterInterval?: number;
  notes?: string;
}

// 계절 타입 (백엔드 Season enum과 일치)
export type Season = 'COMMON' | 'SUMMER' | 'WINTER';

// 페이지네이션 응답 타입
export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
  first: boolean;
  last: boolean;
}

// API 응답 타입
export interface ApiResponse<T> {
  data: T;
  message?: string;
  status: number;
}
