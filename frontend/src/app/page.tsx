'use client';

import { usePlants, useWaterPlant } from '@/hooks/usePlants';
import { Season } from '@/types/plant';

export default function HomePage() {
  // 물줘야 하는 식물 목록 가져오기
  const { data: wateringRequired, isLoading, error } = usePlants('wateringRequired');
  
  // 물주기 함수
  const waterPlantMutation = useWaterPlant();

  // 로딩 중일 때
  if (isLoading) return <div>로딩 중...</div>;
  
  // 에러가 있을 때
  if (error) return <div>에러가 발생했습니다: {error.message}</div>;

  // 물주기 버튼 클릭
  const handleWaterPlant = (plantId: number) => {
    waterPlantMutation.mutate({ id: plantId, season: 'COMMON' });
  };

  return (
    <div className="p-4">
      <h1 className="text-2xl font-bold mb-4">물줘야 하는 식물들</h1>
      
      {wateringRequired?.content.map((plant) => (
        <div key={plant.id} className="border p-4 mb-2 rounded">
          <h3 className="font-semibold">{plant.name}</h3>
          <p>다음 물주기: {plant.nextWateringDate}</p>
          
          <button
            onClick={() => handleWaterPlant(plant.id)}
            className="bg-blue-500 text-white px-4 py-2 rounded mt-2"
            disabled={waterPlantMutation.isPending}
          >
            {waterPlantMutation.isPending ? '물주는 중...' : '물주기'}
          </button>
        </div>
      ))}
    </div>
  );
}