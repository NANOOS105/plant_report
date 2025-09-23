'use client';

import { useState } from 'react';

export default function RegisterPage() {
  const [formData, setFormData] = useState({
    name: '',
    commonInterval: '',
    summerInterval: '',
    winterInterval: '',
    lastWateringDate: '',
  });

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    console.log('폼 데이터:', formData);
  };

  return (
    <div className="p-4 max-w-md mx-auto">
      <h1 className="text-2xl font-bold mb-4">새 식물 등록</h1>
      
      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <label className="block text-sm font-medium mb-1">식물 이름</label>
          <input
            type="text"
            value={formData.name}
            onChange={(e) => setFormData({...formData, name: e.target.value})}
            className="w-full p-2 border rounded"
            placeholder="예: 장미"
          />
        </div>

        <div>
          <label className="block text-sm font-medium mb-1">공통 물주기 간격 (일) *</label>
          <input
            type="number"
            value={formData.commonInterval}
            onChange={(e) => setFormData({...formData, commonInterval: e.target.value})}
            className="w-full p-2 border rounded"
            placeholder="예: 7"
            required
          />
        </div>

        <div>
          <label className="block text-sm font-medium mb-1">여름 물주기 간격 (일)</label>
          <input
            type="number"
            value={formData.summerInterval}
            onChange={(e) => setFormData({...formData, summerInterval: e.target.value})}
            className="w-full p-2 border rounded"
            placeholder="예: 5 (선택사항)"
          />
        </div>

        <div>
          <label className="block text-sm font-medium mb-1">겨울 물주기 간격 (일)</label>
          <input
            type="number"
            value={formData.winterInterval}
            onChange={(e) => setFormData({...formData, winterInterval: e.target.value})}
            className="w-full p-2 border rounded"
            placeholder="예: 10 (선택사항)"
          />
        </div>

        <div>
          <label className="block text-sm font-medium mb-1">마지막 물 준 날짜</label>
          <input
            type="date"
            value={formData.lastWateringDate}
            onChange={(e) => setFormData({...formData, lastWateringDate: e.target.value})}
            className="w-full p-2 border rounded"
          />
          <p className="text-sm text-gray-500 mt-1">선택사항 - 비워두면 오늘 날짜로 설정됩니다</p>
        </div>

        <button
          type="submit"
          className="w-full bg-green-500 text-white py-2 rounded hover:bg-green-600"
        >
          식물 등록
        </button>
      </form>
    </div>
  );
}