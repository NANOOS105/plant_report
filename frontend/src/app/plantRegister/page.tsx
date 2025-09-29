'use client';

import { useState } from 'react';
import { useRouter } from 'next/navigation';
import Link from 'next/link';
import { Season } from '@/types/plant';
import { useSavePlant } from '@/hooks/usePlants';

export default function PlantRegisterPage() {
  const [formData, setFormData] = useState({
    name: '',
    commonInterval: '',
    summerInterval: '',
    winterInterval: '',
    lastWateringDate: '',
    notes: '',
  });
  const [error, setError] = useState('');
  const router = useRouter();
  
  // 식물 등록 mutation
  const savePlantMutation = useSavePlant();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');

    try {
             const requestData = {
               ...formData,
               commonInterval: parseInt(formData.commonInterval),
               summerInterval: formData.summerInterval ? parseInt(formData.summerInterval) : undefined,
               winterInterval: formData.winterInterval ? parseInt(formData.winterInterval) : undefined,
               lastWateringDate: formData.lastWateringDate || undefined,
               season: 'COMMON' as Season,
             };
      
      console.log('식물 등록 시도:', requestData);
      
      await savePlantMutation.mutateAsync(requestData);
      alert('식물 등록 성공!');
      router.push('/plantList');
    } catch (error) {
      setError('식물 등록에 실패했습니다.');
      console.error('식물 등록 오류:', error);
    }
  };

  return (
    <div className="max-w-2xl mx-auto p-6 pb-32">
      <div className="bg-white rounded-lg shadow-md p-8 mb-8">
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

          {error && (
            <div className="text-red-600 text-sm text-center bg-red-50 p-3 rounded-md">
              {error}
            </div>
          )}

          <div className="flex space-x-4">
            <button
              type="submit"
              disabled={savePlantMutation.isPending}
              className="flex-1 bg-green-600 text-white py-3 px-4 rounded-md hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-green-500 disabled:bg-gray-400 disabled:cursor-not-allowed font-medium"
            >
              {savePlantMutation.isPending ? '등록 중...' : '🌱 식물 등록하기'}
            </button>
            
            <Link
              href="/"
              className="flex-1 bg-gray-200 text-gray-800 py-3 px-4 rounded-md hover:bg-gray-300 focus:outline-none focus:ring-2 focus:ring-gray-500 text-center font-medium"
            >
              취소
            </Link>
          </div>
        </form>
      </div>
    </div>
  );
}
