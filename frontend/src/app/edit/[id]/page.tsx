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
    <div className="p-4 max-w-md mx-auto">
      <h1 className="text-2xl font-bold mb-4 text-gray-900">식물 정보 수정</h1>
      
      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <label className="block text-sm font-medium mb-1 text-gray-900">식물 이름</label>
          <input
            type="text"
            value={formData.name}
            onChange={(e) => setFormData({...formData, name: e.target.value})}
            className="w-full p-2 border rounded placeholder-gray-600 text-gray-900"
            placeholder="예: 몬스테라"
            required
          />
        </div>

        <div>
          <label className="block text-sm font-medium mb-1 text-gray-900">공통 물주기 간격 (일)</label>
          <input
            type="number"
            value={formData.commonInterval}
            onChange={(e) => setFormData({...formData, commonInterval: e.target.value})}
            className="w-full p-2 border rounded placeholder-gray-600 text-gray-900"
            placeholder="예: 7"
          />
        </div>

        <div>
          <label className="block text-sm font-medium mb-1 text-gray-900">여름 물주기 간격 (일)</label>
          <input
            type="number"
            value={formData.summerInterval}
            onChange={(e) => setFormData({...formData, summerInterval: e.target.value})}
            className="w-full p-2 border rounded placeholder-gray-600 text-gray-900"
            placeholder="예: 5 (선택사항)"
          />
        </div>

        <div>
          <label className="block text-sm font-medium mb-1 text-gray-900">겨울 물주기 간격 (일)</label>
          <input
            type="number"
            value={formData.winterInterval}
            onChange={(e) => setFormData({...formData, winterInterval: e.target.value})}
            className="w-full p-2 border rounded placeholder-gray-600 text-gray-900"
            placeholder="예: 10 (선택사항)"
          />
        </div>

        <div className="flex gap-2">
          <button
            type="submit"
            disabled={updatePlantMutation.isPending}
            className="flex-1 bg-green-500 text-white py-2 rounded hover:bg-green-600 disabled:bg-gray-400 disabled:cursor-not-allowed"
          >
            {updatePlantMutation.isPending ? '수정 중...' : '수정하기'}
          </button>
          
          <button
            type="button"
            onClick={() => router.push('/plantList')}
            className="flex-1 bg-gray-500 text-white py-2 rounded hover:bg-gray-600"
          >
            취소
          </button>
        </div>
      </form>
    </div>
  );
}
