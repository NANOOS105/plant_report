'use client';

import { usePlants, useWaterPlant, useCancelWaterPlant } from '@/hooks/usePlants';
import { useSeason } from '@/contexts/SeasonContext';

export default function HomePage() {
  // 물줘야 하는 식물 목록 가져오기
  const { data: wateringRequired, isLoading, error } = usePlants('wateringRequired');
  
  // 물주기 함수
  const waterPlantMutation = useWaterPlant();
  
  // 물주기 취소 함수
  const cancelWaterPlantMutation = useCancelWaterPlant();
  
  // 현재 계절
  const { currentSeason } = useSeason();

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
      <h1 className="text-2xl font-bold mb-4 text-gray-900">PLANT REPORT HOME</h1>
      
      {wateringRequired?.content.map((plant) => (
        <div key={plant.id} className="border p-4 mb-2 rounded">
          <h3 className="font-semibold text-gray-900">{plant.name}</h3>
          <p className="text-gray-900">다음 물주기: {plant.nextWateringDate}</p>
          
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
    </div>
  );
}