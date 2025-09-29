'use client';

import { useState, useMemo } from 'react';
import { usePlants, useWaterPlant, useUpdatePlant, useDeletePlant, useCancelWaterPlant } from '@/hooks/usePlants';
import { useSeason } from '@/contexts/SeasonContext';
import { Plant } from '@/types/plant';
import { getIntervalForSeason, calculateNextWateringDate, isWateringRequired, getWateringDelayDays } from '@/utils/plantUtils';
import Link from 'next/link';

export default function PlantListPage() {
  // 모든 식물 목록 가져오기
  const { data: plants, isLoading, error } = usePlants();
  
  // 현재 계절
  const { currentSeason } = useSeason();
  
  // 검색 상태
  const [searchTerm, setSearchTerm] = useState('');
  
  // 공통 함수들은 utils/plantUtils.ts에서 import
  
  // 물주기 함수
  const waterPlantMutation = useWaterPlant();
  
  // 수정/삭제 함수
  const updatePlantMutation = useUpdatePlant();
  const deletePlantMutation = useDeletePlant();
  
  // 물주기 취소 함수
  const cancelWaterPlantMutation = useCancelWaterPlant();

  // 검색된 식물 목록 필터링
  const filteredPlants = useMemo(() => {
    if (!plants?.content) return [];
    
    if (!searchTerm.trim()) {
      return plants.content;
    }
    
    return plants.content.filter(plant => 
      plant.name.toLowerCase().includes(searchTerm.toLowerCase())
    );
  }, [plants?.content, searchTerm]);

  // 로딩 중일 때
  if (isLoading) return <div className="p-4">로딩 중...</div>;
  
  // 에러가 있을 때
  if (error) return <div className="p-4">에러가 발생했습니다: {error.message}</div>;

  // 물주기 버튼 클릭
  const handleWaterPlant = (plantId: number) => {
    waterPlantMutation.mutate({ id: plantId, season: currentSeason });
  };

  // 삭제 버튼 클릭
  const handleDeletePlant = async (plantId: number, plantName: string) => {
    if (confirm(`"${plantName}" 식물을 정말 삭제하시겠습니까?`)) {
      try {
        await deletePlantMutation.mutateAsync(plantId);
        alert('식물이 삭제되었습니다.');
      } catch (error) {
        alert('삭제에 실패했습니다.');
      }
    }
  };

  // 물주기 취소 버튼 클릭
  const handleCancelWaterPlant = (plantId: number) => {
    console.log('물주기 취소 버튼 클릭:', plantId);
    cancelWaterPlantMutation.mutate({ id: plantId, season: currentSeason });
  };

  return (
    <div className="p-4">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-2xl font-bold text-gray-900">식물 목록</h1>
        
        {/* 검색 입력창 */}
        <div className="flex items-center space-x-2">
          <div className="relative">
            <input
              type="text"
              placeholder="식물 이름으로 검색..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="w-64 px-4 py-2 pl-10 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-green-500 focus:border-green-500 text-gray-900 placeholder-gray-400"
            />
            <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
              <svg className="h-5 w-5 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
              </svg>
            </div>
          </div>
          {searchTerm && (
            <button
              onClick={() => setSearchTerm('')}
              className="text-gray-400 hover:text-gray-600"
              title="검색 초기화"
            >
              <svg className="h-5 w-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
              </svg>
            </button>
          )}
        </div>
      </div>
      
      {/* 검색 결과 개수 표시 */}
      {searchTerm && (
        <div className="mb-4 text-sm text-gray-600">
          "{searchTerm}" 검색 결과: {filteredPlants.length}개
        </div>
      )}
      
      {/* 식물 목록 */}
      <div className="space-y-1">
        {filteredPlants.map((plant) => {
          console.log('개별 식물 데이터:', plant);
          return (
            <div key={plant.id} className="border p-4 rounded-lg bg-white shadow-sm">
            <div className="flex items-start">
              <div className="flex-1">
                <h3 className="text-lg font-semibold text-gray-900 mb-2">{plant.name}</h3>
                
                {/* 수평 배치: 물주기 정보와 메모 */}
                <div className="flex gap-4 items-start">
                  <div className="flex-none h-16 flex flex-col justify-center">
                    <p className="text-sm text-gray-600">
                      마지막 물주기: {plant.lastWateringDate || '없음'}
                    </p>
                    <p className="text-sm text-gray-600">
                      다음 물주기: {calculateNextWateringDate(plant, currentSeason) || '미정'}
                    </p>
                    <p className="text-xs text-gray-500">
                      현재 계절 간격: {getIntervalForSeason(plant, currentSeason)}일
                    </p>
                  </div>
                  
                  {plant.notes && (
                    <div className="w-150 ml-4">
                      <div className="p-1 bg-gray-50 rounded text-sm text-gray-700 h-16">
                        <span className="font-medium">메모:</span> {plant.notes}
                      </div>
                    </div>
                  )}
                </div>
                
                <div className="mt-1">
                  <span className={`inline-block px-2 py-1 rounded text-xs ${
                    isWateringRequired(plant, currentSeason)
                      ? 'bg-red-100 text-red-800' 
                      : 'bg-green-100 text-green-800'
                  }`}>
                    {isWateringRequired(plant, currentSeason) 
                      ? `물주기 필요${getWateringDelayDays(plant, currentSeason) > 0 ? ` (${getWateringDelayDays(plant, currentSeason)}일 지연)` : ''}` 
                      : '물주기 완료'}
                  </span>
                </div>
              </div>
              
              {/* 버튼들 */}
              <div className="flex gap-2 ml-4">
                {/* 물주기/취소 토글 버튼 */}
                {isWateringRequired(plant, currentSeason) ? (
                  <button
                    onClick={() => handleWaterPlant(plant.id)}
                    className="bg-blue-200 text-white p-2 rounded hover:bg-blue-600 disabled:bg-gray-400 text-lg"
                    disabled={waterPlantMutation.isPending}
                    title={waterPlantMutation.isPending ? '물주는 중...' : '물주기'}
                  >
                    💧
                  </button>
                ) : (
                  <button
                    onClick={() => handleCancelWaterPlant(plant.id)}
                    className="bg-orange-200 text-white p-2 rounded hover:bg-orange-600 disabled:bg-gray-400 text-lg"
                    disabled={cancelWaterPlantMutation.isPending}
                    title={cancelWaterPlantMutation.isPending ? '취소 중...' : '물주기 취소'}
                  >
                    ↩️
                  </button>
                )}
                
                {/* 수정 버튼 */}
                <Link 
                  href={`/edit/${plant.id}`}
                  className="bg-gray-200 text-white p-2 rounded hover:bg-yellow-600 text-center text-lg"
                  title="수정"
                >
                  ⚙️
                </Link>
                
                {/* 삭제 버튼 */}
                <button
                  onClick={() => handleDeletePlant(plant.id, plant.name)}
                  className="bg-red-200 text-white p-2 rounded hover:bg-red-600 disabled:bg-gray-400 text-lg"
                  disabled={deletePlantMutation.isPending}
                  title={deletePlantMutation.isPending ? '삭제 중...' : '삭제'}
                >
                  🗑️
                </button>
              </div>
            </div>
          </div>
          );
        })}
      </div>

      {/* 빈 목록 메시지 */}
      {filteredPlants.length === 0 && (
        <div className="text-center py-8 text-gray-500">
          {searchTerm ? `"${searchTerm}"에 해당하는 식물이 없습니다.` : '등록된 식물이 없습니다.'}
        </div>
      )}
    </div>
  );
}
