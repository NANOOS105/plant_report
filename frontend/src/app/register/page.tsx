'use client';

import { useState } from 'react';
import { useSavePlant } from '@/hooks/usePlants';
import { useRouter } from 'next/navigation';

export default function RegisterPage() {
  const [formData, setFormData] = useState({
    name: '',
    commonInterval: '',
    summerInterval: '',
    winterInterval: '',
    lastWateringDate: '',
  });

  const savePlantMutation = useSavePlant();
  const router = useRouter();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    
    try {
      // 데이터 변환
      const plantData = {
        name: formData.name,
        commonInterval: parseInt(formData.commonInterval),
        summerInterval: formData.summerInterval ? parseInt(formData.summerInterval) : undefined, // 빈 값은 undefined
        winterInterval: formData.winterInterval ? parseInt(formData.winterInterval) : undefined, // 빈 값은 undefined
        lastWateringDate: formData.lastWateringDate || undefined, // 빈 값은 undefined
        user: null, // 임시로 null (나중에 사용자 인증 추가)
      };

      // API 호출
      await savePlantMutation.mutateAsync(plantData);
      
      // 성공 시 홈페이지로 이동
      router.push('/');
    } catch (error) {
      console.error('식물 등록 실패:', error);
      alert('식물 등록에 실패했습니다. 다시 시도해주세요.');
    }
  };

  return (
    <div className="p-4 max-w-md mx-auto">
      <h1 className="text-2xl font-bold mb-4 text-gray-900">새 식물 등록</h1>
      
      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <label className="block text-sm font-medium mb-1 text-gray-900">식물 이름</label>
          <input
            type="text"
            value={formData.name}
            onChange={(e) => setFormData({...formData, name: e.target.value})}
            className="w-full p-2 border rounded placeholder-gray-600 text-gray-900"
            placeholder="예: 몬스테라"
          />
        </div>

        <div>
          <label className="block text-sm font-medium mb-1 text-gray-900">공통 물주기 간격 (일) *</label>
          <input
            type="number"
            value={formData.commonInterval}
            onChange={(e) => setFormData({...formData, commonInterval: e.target.value})}
            className="w-full p-2 border rounded placeholder-gray-600 text-gray-900"
            placeholder="예: 7"
            required
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

        <div>
          <label className="block text-sm font-medium mb-1 text-gray-900">마지막 물 준 날짜</label>
          <input
            type="date"
            value={formData.lastWateringDate}
            onChange={(e) => setFormData({...formData, lastWateringDate: e.target.value})}
            className="w-full p-2 border rounded text-gray-900"
          />
          <p className="text-sm text-gray-500 mt-1">선택사항 - 비워두면 오늘 날짜로 설정됩니다</p>
        </div>

        <button
          type="submit"
          disabled={savePlantMutation.isPending}
          className="w-full bg-green-500 text-white py-2 rounded hover:bg-green-600 disabled:bg-gray-400 disabled:cursor-not-allowed"
        >
          {savePlantMutation.isPending ? '등록 중...' : '식물 등록'}
        </button>
      </form>
    </div>
  );
}