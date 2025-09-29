'use client';

import { usePlants, useWaterPlant, useCancelWaterPlant } from '@/hooks/usePlants';
import { useSeason } from '@/contexts/SeasonContext';
import { calculateNextWateringDate, isWateringRequired } from '@/utils/plantUtils';
import Link from 'next/link';

export default function HomePage() {
  // 모든 식물 목록 가져오기
  const { data: allPlants, isLoading, error } = usePlants();
  
  // 물주기 함수
  const waterPlantMutation = useWaterPlant();
  
  // 물주기 취소 함수
  const cancelWaterPlantMutation = useCancelWaterPlant();
  
  // 현재 계절
  const { currentSeason } = useSeason();

  // 디버깅용 로그
  console.log('전체 식물 데이터:', allPlants);
  console.log('현재 계절:', currentSeason);
  
  // 물주기가 필요한 식물만 필터링
  const wateringRequiredPlants = allPlants?.content?.filter(plant => 
    isWateringRequired(plant, currentSeason)
  ) || [];
  
  console.log('물주기 필요한 식물:', wateringRequiredPlants);

  // 로딩 중일 때
  if (isLoading) return <div>로딩 중...</div>;
  
  // 에러가 있을 때
  if (error) return <div>에러가 발생했습니다: {error.message}</div>;

  // 물주기 버튼 클릭
  const handleWaterPlant = (plantId: number) => {
    waterPlantMutation.mutate({ id: plantId, season: currentSeason });
  };

  // 물주기 취소 버튼 클릭
  const handleCancelWaterPlant = (plantId: number) => {
    cancelWaterPlantMutation.mutate({ id: plantId, season: currentSeason });
  };

  return (
    <div className="p-4">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-xl font-bold text-gray-900">현재 물을 주어야 하는 식물 목록록</h1>
      </div>
      
      {wateringRequiredPlants.map((plant) => (
        <div key={plant.id} className="border p-4 mb-2 rounded">
          <h3 className="font-semibold text-gray-900">{plant.name}</h3>
          <p className="text-gray-900">마지막 물주기: {plant.lastWateringDate || '없음'}</p>
          <p className="text-gray-900">다음 물주기: {calculateNextWateringDate(plant, currentSeason) || '미정'}</p>
          <button
            onClick={() => handleWaterPlant(plant.id)}
            className="bg-blue-500 text-white p-2 rounded mt-2 text-lg"
            disabled={waterPlantMutation.isPending}
            title={waterPlantMutation.isPending ? '물주는 중...' : '물주기'}
          >
            💧
          </button>
        </div>
      ))}

      {/* 물주기가 필요한 식물이 없을 때 */}
      {wateringRequiredPlants.length === 0 && (
        <div className="text-center py-8 text-gray-500">
          물주기가 필요한 식물이 없습니다! 🎉
        </div>
      )}
    </div>
  );
}