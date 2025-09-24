import axios from 'axios';
import { Plant, PlantSaveRequest, PlantUpdateRequest, Season, PageResponse } from '@/types/plant';

// 1. axios 인스턴스 생성
const api = axios.create({
  baseURL: process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080/api',
  headers: {
    'Content-Type': 'application/json',
  },
});

// 2. 식물 목록 조회 함수
export const getPlants = async (status?: string, page: number = 0, size: number = 20) => {
  const params = new URLSearchParams();
  if (status) params.append('status', status);
  params.append('page', page.toString());
  params.append('size', size.toString());
  params.append('sort', 'nextWateringDate,asc');
  
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