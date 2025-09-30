'use client';

import { useState } from 'react';
import { useRouter } from 'next/navigation';
import { useCreatePost } from '@/hooks/useCommunity';
import { POST_CATEGORIES, PostCategory } from '@/types/community';
import ProtectedRoute from '@/components/ProtectedRoute';
import Link from 'next/link';

export default function CommunityWritePage() {
  return (
    <ProtectedRoute>
      <CommunityWriteContent />
    </ProtectedRoute>
  );
}

function CommunityWriteContent() {
  // 1. 폼 데이터 상태
  const [formData, setFormData] = useState({
    title: '',
    content: '',
    category: 'PLANT_STORY' as PostCategory,
  });
  const [error, setError] = useState('');
  
  // 2. Router와 Mutation
  const router = useRouter();
  const createPostMutation = useCreatePost();

  // 3. 폼 제출 핸들러
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');

    // 유효성 검사
    if (!formData.title.trim()) {
      setError('제목을 입력해주세요.');
      return;
    }
    if (!formData.content.trim()) {
      setError('내용을 입력해주세요.');
      return;
    }

    try {
      // 게시글 작성 API 호출
      await createPostMutation.mutateAsync(formData);
      alert('게시글이 작성되었습니다!');
      router.push('/community');
    } catch (error) {
      setError('게시글 작성에 실패했습니다.');
      console.error('게시글 작성 오류:', error);
    }
  };

  return (
    <div className="max-w-4xl mx-auto p-6">
      <div className="bg-white rounded-lg shadow-md p-8">
        {/* 헤더 */}
        <div className="mb-6">
          <h1 className="text-2xl font-bold text-gray-900 mb-2">
            게시글 작성
          </h1>
          <p className="text-sm text-gray-600">
            식물 관련 이야기나 질문을 자유롭게 작성해보세요
          </p>
        </div>

        <form onSubmit={handleSubmit} className="space-y-6">
          {/* 카테고리 선택 */}
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              카테고리 *
            </label>
            <div className="flex gap-3">
              <button
                type="button"
                onClick={() => setFormData({...formData, category: 'PLANT_STORY'})}
                className={`flex-1 py-3 px-4 rounded-lg font-medium transition-colors ${
                  formData.category === 'PLANT_STORY'
                    ? 'bg-green-600 text-white'
                    : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
                }`}
              >
                {POST_CATEGORIES.PLANT_STORY}
              </button>
              <button
                type="button"
                onClick={() => setFormData({...formData, category: 'QNA'})}
                className={`flex-1 py-3 px-4 rounded-lg font-medium transition-colors ${
                  formData.category === 'QNA'
                    ? 'bg-blue-600 text-white'
                    : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
                }`}
              >
                {POST_CATEGORIES.QNA}
              </button>
            </div>
          </div>

          {/* 제목 */}
          <div>
            <label htmlFor="title" className="block text-sm font-medium text-gray-700 mb-2">
              제목 *
            </label>
            <input
              id="title"
              type="text"
              required
              value={formData.title}
              onChange={(e) => setFormData({...formData, title: e.target.value})}
              className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-green-500 focus:border-green-500 text-gray-900 placeholder-gray-400"
              placeholder="제목을 입력하세요 (최대 200자)"
              maxLength={200}
            />
          </div>

          {/* 내용 */}
          <div>
            <label htmlFor="content" className="block text-sm font-medium text-gray-700 mb-2">
              내용 *
            </label>
            <textarea
              id="content"
              required
              rows={15}
              value={formData.content}
              onChange={(e) => setFormData({...formData, content: e.target.value})}
              className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-green-500 focus:border-green-500 text-gray-900 placeholder-gray-400"
              placeholder="내용을 입력하세요 (최대 5000자)"
              maxLength={5000}
            />
            <div className="text-right text-sm text-gray-500 mt-1">
              {formData.content.length} / 5000
            </div>
          </div>

          {/* 에러 메시지 */}
          {error && (
            <div className="bg-red-50 border border-red-200 rounded-lg p-3 text-red-800 text-sm">
              {error}
            </div>
          )}

          {/* 버튼 */}
          <div className="flex gap-3">
            <button
              type="submit"
              disabled={createPostMutation.isPending}
              className="flex-1 bg-green-600 text-white py-3 px-4 rounded-lg hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-green-500 disabled:bg-gray-400 disabled:cursor-not-allowed font-medium"
            >
              {createPostMutation.isPending ? '작성 중...' : '작성 완료'}
            </button>
            
            <Link
              href="/community"
              className="flex-1 bg-gray-200 text-gray-800 py-3 px-4 rounded-lg hover:bg-gray-300 focus:outline-none focus:ring-2 focus:ring-gray-500 text-center font-medium"
            >
              취소
            </Link>
          </div>
        </form>
      </div>
    </div>
  );
}
