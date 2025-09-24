import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { getPlants, savePlant, waterPlant, updatePlant, deletePlant, cancelWaterPlant } from '@/lib/api';
import { Plant, PlantSaveRequest, PlantUpdateRequest, Season } from '@/types/plant';

// 식물 목록 조회
export const usePlants = (status?: string, page: number = 0, size: number = 20) => {
  return useQuery({
    queryKey: ['plants', status, page, size], //캐시 저장
    queryFn: () => getPlants(status, page, size), //실제 HTTP는 api 레이어 함수가 수행
  });
};

// 식물 등록
export const useSavePlant = () => {
  const queryClient = useQueryClient();
  
  return useMutation({
    mutationFn: (data: PlantSaveRequest) => savePlant(data),
    onSuccess: () => {
      // 식물 등록 성공 시 목록 새로고침
      queryClient.invalidateQueries({ queryKey: ['plants'] });
    },
  });
};

// 식물 물주기
export const useWaterPlant = () => {
  const queryClient = useQueryClient();
  
  return useMutation({
    mutationFn: ({ id, season }: { id: number; season: Season }) => waterPlant(id, season),
    onSuccess: () => {
      // 물주기 성공 시 목록 새로고침
      queryClient.invalidateQueries({ queryKey: ['plants'] });
    },
  });
};

// 식물 수정
export const useUpdatePlant = () => {
  const queryClient = useQueryClient();
  
  return useMutation({
    mutationFn: ({ id, data }: { id: number; data: PlantUpdateRequest }) => updatePlant(id, data),
    onSuccess: () => {
      // 수정 성공 시 목록 새로고침
      queryClient.invalidateQueries({ queryKey: ['plants'] });
    },
  });
};

// 식물 삭제
export const useDeletePlant = () => {
  const queryClient = useQueryClient();
  
  return useMutation({
    mutationFn: (id: number) => deletePlant(id),
    onSuccess: () => {
      // 삭제 성공 시 목록 새로고침
      queryClient.invalidateQueries({ queryKey: ['plants'] });
    },
  });
};

// 물주기 취소
export const useCancelWaterPlant = () => {
  const queryClient = useQueryClient();
  
  return useMutation({
    mutationFn: ({ id, season }: { id: number; season: Season }) => cancelWaterPlant(id, season),
    onSuccess: () => {
      // 취소 성공 시 목록 새로고침
      queryClient.invalidateQueries({ queryKey: ['plants'] });
    },
  });
};