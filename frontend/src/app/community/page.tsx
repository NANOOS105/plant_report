'use client';

import { useState } from 'react';
import { usePosts, usePostsByCategory } from '@/hooks/useCommunity';
import { useAuth } from '@/contexts/AuthContext';
import { POST_CATEGORIES, PostCategory } from '@/types/community';
import Link from 'next/link';

export default function CommunityPage() {
  // 1. 상태 관리
  const [selectedCategory, setSelectedCategory] = useState<PostCategory | 'ALL'>('ALL');
  const [currentPage, setCurrentPage] = useState(0);
  
  // 2. 인증 상태 확인
  const { user } = useAuth();
  
  // 3. 데이터 가져오기
  // selectedCategory가 'ALL'이면 전체 조회, 아니면 카테고리별 조회
  const { data: allPosts, isLoading: isLoadingAll, error: errorAll } = usePosts(currentPage, 20);
  const { data: categoryPosts, isLoading: isLoadingCategory, error: errorCategory } = usePostsByCategory(
    selectedCategory as string,
    currentPage,
    20
  );
  
  // 4. 어떤 데이터를 보여줄지 결정
  const posts = selectedCategory === 'ALL' ? allPosts : categoryPosts;
  const isLoading = selectedCategory === 'ALL' ? isLoadingAll : isLoadingCategory;
  const error = selectedCategory === 'ALL' ? errorAll : errorCategory;
  
  // 5. 디버깅 로그
  console.log('선택된 카테고리:', selectedCategory);
  console.log('게시글 데이터:', posts);
  console.log('로딩 상태:', isLoading);
  console.log('에러:', error);

  return (
    <div className="max-w-7xl mx-auto p-6">
      {/* 헤더 */}
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-3xl font-bold text-gray-900">커뮤니티</h1>
        
        {/* 로그인한 사용자만 글쓰기 버튼 보임 */}
        {user && (
          <Link
            href="/community/write"
            className="bg-green-600 text-white px-6 py-2 rounded-lg hover:bg-green-700 font-medium"
          >
            글쓰기
          </Link>
        )}
      </div>

      {/* 카테고리 탭 */}
      <div className="flex gap-2 mb-6">
        <button
          onClick={() => setSelectedCategory('ALL')}
          className={`px-4 py-2 rounded-lg font-medium ${
            selectedCategory === 'ALL'
              ? 'bg-green-600 text-white'
              : 'bg-gray-200 text-gray-700 hover:bg-gray-300'
          }`}
        >
          전체
        </button>
        <button
          onClick={() => setSelectedCategory('PLANT_STORY')}
          className={`px-4 py-2 rounded-lg font-medium ${
            selectedCategory === 'PLANT_STORY'
              ? 'bg-green-600 text-white'
              : 'bg-gray-200 text-gray-700 hover:bg-gray-300'
          }`}
        >
          {POST_CATEGORIES.PLANT_STORY}
        </button>
        <button
          onClick={() => setSelectedCategory('QNA')}
          className={`px-4 py-2 rounded-lg font-medium ${
            selectedCategory === 'QNA'
              ? 'bg-green-600 text-white'
              : 'bg-gray-200 text-gray-700 hover:bg-gray-300'
          }`}
        >
          {POST_CATEGORIES.QNA}
        </button>
      </div>

      {/* 에러 */}
      {error && (
        <div className="bg-red-50 border border-red-200 rounded-lg p-4 mb-6">
          <p className="text-red-800">에러가 발생했습니다: {error.message}</p>
          <p className="text-sm text-red-600 mt-2">콘솔을 확인해주세요.</p>
        </div>
      )}

      {/* 로딩 중 */}
      {isLoading && !error && (
        <div className="text-center py-12">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-green-600 mx-auto"></div>
          <p className="mt-4 text-gray-600">게시글을 불러오는 중...</p>
        </div>
      )}

      {/* 게시글 목록 */}
      {!isLoading && posts && (
        <div className="bg-white rounded-lg shadow">
          {/* 테이블 헤더 */}
          <div className="grid grid-cols-12 gap-4 p-4 border-b font-semibold text-gray-700">
            <div className="col-span-1 text-center">번호</div>
            <div className="col-span-2 text-center">카테고리</div>
            <div className="col-span-5">제목</div>
            <div className="col-span-2 text-center">작성자</div>
            <div className="col-span-1 text-center">조회</div>
            <div className="col-span-1 text-center">좋아요</div>
          </div>

          {/* 게시글 리스트 */}
          {posts.content.map((post, index) => (
            <Link
              key={post.id}
              href={`/community/${post.id}`}
              className="grid grid-cols-12 gap-4 p-4 border-b hover:bg-gray-50 transition-colors"
            >
              <div className="col-span-1 text-center text-gray-600">
                {posts.totalElements - (currentPage * 20) - index}
              </div>
              <div className="col-span-2 text-center">
                <span className={`px-3 py-1 rounded-full text-sm font-medium ${
                  post.category === 'PLANT_STORY'
                    ? 'bg-green-100 text-green-800'
                    : 'bg-blue-100 text-blue-800'
                }`}>
                  {post.categoryDisplayName}
                </span>
              </div>
              <div className="col-span-5">
                <span className="font-medium text-gray-900">{post.title}</span>
                {post.commentCount > 0 && (
                  <span className="ml-2 text-green-600 text-sm">
                    [{post.commentCount}]
                  </span>
                )}
              </div>
              <div className="col-span-2 text-center text-gray-600">
                {post.authorName}
              </div>
              <div className="col-span-1 text-center text-gray-600">
                {post.viewCount}
              </div>
              <div className="col-span-1 text-center text-gray-600">
                {post.likeCount}
              </div>
            </Link>
          ))}

          {/* 빈 목록 */}
          {posts.content.length === 0 && (
            <div className="text-center py-12">
              <div className="text-gray-400 text-5xl mb-4">📝</div>
              <p className="text-gray-600 font-medium mb-2">게시글이 없습니다</p>
              <p className="text-sm text-gray-500">
                첫 번째 게시글을 작성해보세요!
              </p>
              {user && (
                <Link
                  href="/community/write"
                  className="inline-block mt-4 bg-green-600 text-white px-6 py-2 rounded-lg hover:bg-green-700 font-medium"
                >
                  글쓰기
                </Link>
              )}
            </div>
          )}
        </div>
      )}

      {/* 페이징 */}
      {!isLoading && posts && posts.totalPages > 1 && (
        <div className="flex justify-center gap-2 mt-6">
          {/* 이전 페이지 */}
          <button
            onClick={() => setCurrentPage(currentPage - 1)}
            disabled={currentPage === 0}
            className="px-4 py-2 rounded bg-gray-200 hover:bg-gray-300 disabled:opacity-50"
          >
            이전
          </button>
          
          {/* 페이지 번호들 */}
          {Array.from({ length: Math.min(5, posts.totalPages) }, (_, i) => {
            const pageNum = currentPage < 3 ? i : currentPage - 2 + i;
            if (pageNum >= posts.totalPages) return null;
            
            return (
              <button
                key={pageNum}
                onClick={() => setCurrentPage(pageNum)}
                className={`px-4 py-2 rounded ${
                  currentPage === pageNum
                    ? 'bg-green-600 text-white'
                    : 'bg-gray-200 hover:bg-gray-300'
                }`}
              >
                {pageNum + 1}
              </button>
            );
          })}
          
          {/* 다음 페이지 */}
          <button
            onClick={() => setCurrentPage(currentPage + 1)}
            disabled={currentPage >= posts.totalPages - 1}
            className="px-4 py-2 rounded bg-gray-200 hover:bg-gray-300 disabled:opacity-50"
          >
            다음
          </button>
        </div>
      )}
    </div>
  );
}
