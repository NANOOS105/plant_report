import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { getPlants, savePlant, waterPlant } from '@/lib/api';
import { Plant, PlantSaveRequest, Season } from '@/types/plant';

// 식물 목록 조회
export const usePlants = (status?: string, page: number = 0, size: number = 20) => {
  return useQuery({
    queryKey: ['plants', status, page, size],
    queryFn: () => getPlants(status, page, size),
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