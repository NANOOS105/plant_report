'use client';

import { usePlants, useWaterPlant, useCancelWaterPlant } from '@/hooks/usePlants';
import { useSeason } from '@/contexts/SeasonContext';
import { calculateNextWateringDate, isWateringRequired, getWateringDelayDays, getIntervalForSeason } from '@/utils/plantUtils';
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
           waterPlantMutation.mutate({ id: plantId, season: currentSeason }, {
             onSuccess: () => {
               // 물주기 성공 시 식물 목록 새로고침
               window.location.reload();
             }
           });
         };

  // 물주기 취소 버튼 클릭
  const handleCancelWaterPlant = (plantId: number) => {
    cancelWaterPlantMutation.mutate({ id: plantId, season: currentSeason });
  };

  return (
    <div className="p-4">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-xl font-bold text-gray-900">현재 물을 주어야 하는 식물 목록</h1>
      </div>
      
             <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
               {wateringRequiredPlants.map((plant) => {
                 const delayDays = getWateringDelayDays(plant, currentSeason);
                 return (
                   <div key={plant.id} className="border p-3 rounded-lg bg-white shadow-sm">
                     <div className="flex items-start justify-between">
                       <div className="flex-1">
                         <div className="flex items-center justify-between mb-2">
                           <h3 className="text-base font-semibold text-gray-900">{plant.name}</h3>
                           <span className="bg-red-100 text-red-800 px-2 py-1 rounded text-xs font-medium h-8 flex items-center">
                             {delayDays > 0 ? `${delayDays}일 지연` : '물주기 필요'}
                           </span>
                         </div>
                         
                         <div className="space-y-1 mb-3">
                           <p className="text-xs text-gray-600">
                             마지막: {plant.lastWateringDate || '없음'}
                           </p>
                           <p className="text-xs text-gray-600">
                             다음: {calculateNextWateringDate(plant, currentSeason) || '미정'}
                           </p>
                           <p className="text-xs text-gray-500">
                             간격: {getIntervalForSeason(plant, currentSeason)}일
                           </p>
                         </div>
                         
                         {plant.notes && (
                           <div className="mb-2">
                             <p className="text-xs text-gray-600 bg-gray-50 p-2 rounded">
                               <span className="font-medium">메모:</span> {plant.notes}
                             </p>
                           </div>
                         )}
                       </div>
                       
                       {/* 물주기 버튼 */}
                       <div className="ml-3">
                         <button
                           onClick={() => handleWaterPlant(plant.id)}
                           className="bg-blue-500 text-white p-2 rounded hover:bg-blue-600 disabled:bg-gray-400 text-sm h-8 w-8 flex items-center justify-center"
                           disabled={waterPlantMutation.isPending}
                           title={waterPlantMutation.isPending ? '물주는 중...' : '물주기'}
                         >
                           💧
                         </button>
                       </div>
                     </div>
                   </div>
                 );
               })}
             </div>

      {/* 물주기가 필요한 식물이 없을 때 */}
      {wateringRequiredPlants.length === 0 && (
        <div className="text-center py-8 text-gray-500">
          물주기가 필요한 식물이 없습니다! 🎉
        </div>
      )}
    </div>
  );
}