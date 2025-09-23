'use client';

import { useState } from 'react';

export default function RegisterPage() {
  const [formData, setFormData] = useState({
    name: '',
    commonInterval: '',
    summerInterval: '',
    winterInterval: '',
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
          <label className="block text-sm font-medium mb-1">공통 물주기 간격 (일)</label>
          <input
            type="number"
            value={formData.commonInterval}
            onChange={(e) => setFormData({...formData, commonInterval: e.target.value})}
            className="w-full p-2 border rounded"
            placeholder="예: 7"
          />
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