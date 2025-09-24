'use client';

import { usePlants, useWaterPlant } from '@/hooks/usePlants';
import { Season } from '@/types/plant';

export default function PlantListPage() {
  // 모든 식물 목록 가져오기
  const { data: plants, isLoading, error } = usePlants();
  
  // 물주기 함수
  const waterPlantMutation = useWaterPlant();

  // 로딩 중일 때
  if (isLoading) return <div className="p-4">로딩 중...</div>;
  
  // 에러가 있을 때
  if (error) return <div className="p-4">에러가 발생했습니다: {error.message}</div>;

  // 물주기 버튼 클릭
  const handleWaterPlant = (plantId: number) => {
    waterPlantMutation.mutate({ id: plantId, season: 'COMMON' });
  };

  return (
    <div className="p-4">
      <h1 className="text-2xl font-bold mb-4 text-gray-900">식물 목록</h1>
      
      {/* 식물 목록 */}
      <div className="space-y-4">
        {plants?.content.map((plant) => {
          console.log('개별 식물 데이터:', plant);
          return (
            <div key={plant.id} className="border p-4 rounded-lg bg-white shadow-sm">
            <div className="flex justify-between items-start">
              <div>
                <h3 className="text-lg font-semibold text-gray-900">{plant.name}</h3>
                <p className="text-sm text-gray-600">
                  마지막 물주기: {plant.lastWateringDate || '없음'}
                </p>
                <p className="text-sm text-gray-600">
                  다음 물주기: {plant.nextWateringDate || '미정'}
                </p>
                <div className="mt-2">
                  <span className={`inline-block px-2 py-1 rounded text-xs ${
                    plant.isWateringRequired 
                      ? 'bg-red-100 text-red-800' 
                      : 'bg-green-100 text-green-800'
                  }`}>
                    {plant.isWateringRequired ? '물주기 필요' : '물주기 완료'}
                  </span>
                </div>
              </div>
              
              {/* 물주기 버튼 */}
              {plant.isWateringRequired && (
                <button
                  onClick={() => handleWaterPlant(plant.id)}
                  className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600 disabled:bg-gray-400"
                  disabled={waterPlantMutation.isPending}
                >
                  {waterPlantMutation.isPending ? '물주는 중...' : '물주기'}
                </button>
              )}
            </div>
          </div>
          );
        })}
      </div>

      {/* 빈 목록 메시지 */}
      {plants?.content.length === 0 && (
        <div className="text-center py-8 text-gray-500">
          등록된 식물이 없습니다.
        </div>
      )}
    </div>
  );
}
