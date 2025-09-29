'use client';

import { useState, useEffect } from 'react';
import { useRouter, useParams } from 'next/navigation';
import { usePlants, useUpdatePlant } from '@/hooks/usePlants';

export default function EditPlantPage() {
  const router = useRouter();
  const params = useParams();
  const plantId = parseInt(params.id as string);
  
  // 모든 식물 목록 가져오기
  const { data: plantsData, isLoading } = usePlants();
  
  // 수정 함수
  const updatePlantMutation = useUpdatePlant();
  
  // 폼 데이터 상태
  const [formData, setFormData] = useState({
    name: '',
    commonInterval: '',
    summerInterval: '',
    winterInterval: '',
    lastWateringDate: '',
    notes: '',
  });

  // 현재 식물 찾기
  const currentPlant = plantsData?.content.find(plant => plant.id === plantId);

  // 식물 데이터로 폼 초기화
  useEffect(() => {
    if (currentPlant) {
      setFormData({
        name: currentPlant.name,
        commonInterval: currentPlant.commonInterval?.toString() || '',
        summerInterval: currentPlant.summerInterval?.toString() || '',
        winterInterval: currentPlant.winterInterval?.toString() || '',
        lastWateringDate: currentPlant.lastWateringDate || '',
        notes: currentPlant.notes || '',
      });
    }
  }, [currentPlant]);

  // 폼 제출
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    
    try {
      const updateData = {
        name: formData.name,
        commonInterval: formData.commonInterval ? parseInt(formData.commonInterval) : undefined,
        summerInterval: formData.summerInterval ? parseInt(formData.summerInterval) : undefined,
        winterInterval: formData.winterInterval ? parseInt(formData.winterInterval) : undefined,
        lastWateringDate: formData.lastWateringDate || undefined,
        notes: formData.notes || undefined,
      };

      await updatePlantMutation.mutateAsync({ id: plantId, data: updateData });
      alert('식물 정보가 수정되었습니다.');
      router.push('/plantList');
    } catch (error) {
      console.error('식물 수정 실패:', error);
      alert('식물 수정에 실패했습니다. 다시 시도해주세요.');
    }
  };

  if (isLoading) return <div className="p-4">로딩 중...</div>;
  
  if (!currentPlant) {
    return (
      <div className="p-4">
        <div className="text-center py-8">
          <p className="text-gray-500">식물을 찾을 수 없습니다.</p>
          <button 
            onClick={() => router.push('/plantList')}
            className="mt-4 bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
          >
            목록으로 돌아가기
          </button>
        </div>
      </div>
    );
  }

  return (
    <div className="max-w-2xl mx-auto p-6 pb-32">
      <div className="bg-white rounded-lg shadow-md p-8 mb-8">
        <div className="mb-6">
          <h1 className="text-xl font-bold text-gray-900 mb-2">
            🌱 식물 정보 수정
          </h1>
        </div>
        
        <form onSubmit={handleSubmit} className="space-y-6">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            {/* 식물 이름 */}
            <div className="md:col-span-2">
              <label htmlFor="name" className="block text-sm font-medium text-gray-700 mb-2">
                식물 이름 *
              </label>
              <input
                id="name"
                name="name"
                type="text"
                required
                value={formData.name}
                onChange={(e) => setFormData({...formData, name: e.target.value})}
                className="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-green-500 focus:border-green-500 text-gray-900 placeholder-gray-400"
                placeholder="예: 몬스테라, 고무나무"
              />
            </div>

            {/* 공통 물주기 주기 */}
            <div>
              <label htmlFor="commonInterval" className="block text-sm font-medium text-gray-700 mb-2">
                공통 물주기 주기 (일) *
              </label>
              <input
                id="commonInterval"
                name="commonInterval"
                type="number"
                min="1"
                max="30"
                required
                value={formData.commonInterval}
                onChange={(e) => setFormData({...formData, commonInterval: e.target.value})}
                className="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-green-500 focus:border-green-500 text-gray-900 placeholder-gray-400"
                placeholder="예: 7"
              />
            </div>

            {/* 여름 물주기 주기 */}
            <div>
              <label htmlFor="summerInterval" className="block text-sm font-medium text-gray-700 mb-2">
                여름 물주기 주기 (일)
              </label>
              <input
                id="summerInterval"
                name="summerInterval"
                type="number"
                min="1"
                max="30"
                value={formData.summerInterval}
                onChange={(e) => setFormData({...formData, summerInterval: e.target.value})}
                className="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-green-500 focus:border-green-500 text-gray-900 placeholder-gray-400"
                placeholder="선택(비워두면 공통 주기 사용)"
              />
            </div>

            {/* 겨울 물주기 주기 */}
            <div>
              <label htmlFor="winterInterval" className="block text-sm font-medium text-gray-700 mb-2">
                겨울 물주기 주기 (일)
              </label>
              <input
                id="winterInterval"
                name="winterInterval"
                type="number"
                min="1"
                max="30"
                value={formData.winterInterval}
                onChange={(e) => setFormData({...formData, winterInterval: e.target.value})}
                className="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-green-500 focus:border-green-500 text-gray-900 placeholder-gray-400"
                placeholder="선택(비워두면 공통 주기 사용)"
              />
            </div>

            {/* 마지막 물주기 날짜 */}
            <div>
              <label htmlFor="lastWateringDate" className="block text-sm font-medium text-gray-700 mb-2">
                마지막 물주기 날짜
              </label>
              <input
                id="lastWateringDate"
                name="lastWateringDate"
                type="date"
                value={formData.lastWateringDate}
                onChange={(e) => setFormData({...formData, lastWateringDate: e.target.value})}
                className="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-green-500 focus:border-green-500 text-gray-900 placeholder-gray-400"
              />
            </div>
          </div>

          {/* 메모 */}
          <div>
            <label htmlFor="notes" className="block text-sm font-medium text-gray-700 mb-2">
              메모
            </label>
            <textarea
              id="notes"
              name="notes"
              rows={4}
              value={formData.notes}
              onChange={(e) => setFormData({...formData, notes: e.target.value})}
              className="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-green-500 focus:border-green-500 text-gray-900 placeholder-gray-400"
              placeholder="식물에 대한 특별한 관리법이나 주의사항을 적어주세요"
            />
          </div>

          <div className="flex space-x-4">
            <button
              type="submit"
              disabled={updatePlantMutation.isPending}
              className="flex-1 bg-green-600 text-white py-3 px-4 rounded-md hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-green-500 disabled:bg-gray-400 disabled:cursor-not-allowed font-medium"
            >
              {updatePlantMutation.isPending ? '수정 중...' : '🌱 수정하기'}
            </button>
            
            <button
              type="button"
              onClick={() => router.push('/plantList')}
              className="flex-1 bg-gray-200 text-gray-800 py-3 px-4 rounded-md hover:bg-gray-300 focus:outline-none focus:ring-2 focus:ring-gray-500 text-center font-medium"
            >
              취소
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
