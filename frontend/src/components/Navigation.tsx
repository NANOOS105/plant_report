'use client';

import Link from 'next/link';
import { useRouter } from 'next/navigation';
import { useAuth } from '@/contexts/AuthContext';
import { useQueryClient } from '@tanstack/react-query';
import SeasonSelector from '@/components/SeasonSelector';

// 오늘 날짜를 포맷팅하는 함수
const getTodayString = () => {
  const today = new Date();
  const year = today.getFullYear();
  const month = String(today.getMonth() + 1).padStart(2, '0');
  const day = String(today.getDate()).padStart(2, '0');
  const weekdays = ['일', '월', '화', '수', '목', '금', '토'];
  const weekday = weekdays[today.getDay()];
  
  return `${year}.${month}.${day} (${weekday})`;
};

export default function Navigation() {
  const { user, logout } = useAuth();
  const router = useRouter();
  const queryClient = useQueryClient();

  const handleLogout = () => {
    logout();
    // React Query 캐시 초기화
    queryClient.clear();
    router.push('/');
  };

  return (
    <nav className="bg-white shadow-lg">
      <div className="max-w-7xl mx-auto px-4">
        <div className="flex justify-between h-16">
          {/* 로고/홈 링크 */}
          <div className="flex items-center">
            <Link href="/" className="flex items-center space-x-2">
              <span className="text-2xl">🌱</span>
              <span className="text-xl font-bold text-gray-800">Plant Report</span>
            </Link>

            {/* 오늘 날짜 */}
            <div className="text-gray-700 text-sm font-medium px-3 py-2 ml-5">
              {getTodayString()}
            </div>

          </div>
          

          {/* 메뉴 아이템들 */}
          <div className="flex items-center space-x-4">
            {/* 홈 링크 */}
            <Link 
              href="/" 
              className="text-gray-600 hover:text-gray-900 px-3 py-2 rounded-md text-sm font-medium"
            >
              홈
            </Link>

            {/* 계절 선택기 */}
            <SeasonSelector />

            {/* 식물 목록 링크 */}
            <Link 
              href="/plantList" 
              className="text-gray-600 hover:text-gray-900 px-3 py-2 rounded-md text-sm font-medium"
            >
              식물 목록
            </Link>

            {/* 식물 등록 링크 */}
            <Link 
              href="/plantRegister" 
              className="text-gray-600 hover:text-gray-900 px-3 py-2 rounded-md text-sm font-medium"
            >
              식물 등록
            </Link>

            {/* 커뮤니티 링크 */}
            <Link 
              href="/community" 
              className="text-gray-600 hover:text-gray-900 px-3 py-2 rounded-md text-sm font-medium"
            >
              커뮤니티
            </Link>

            {/* 로그인 상태에 따른 버튼 */}
            {user ? (
              <div className="flex items-center space-x-4">
                <span className="text-gray-600 text-sm">
                  안녕하세요, {user.name}님!
                </span>
                <button
                  onClick={handleLogout}
                  className="bg-red-500 hover:bg-red-600 text-white px-4 py-2 rounded-md text-sm font-medium"
                >
                  로그아웃
                </button>
              </div>
            ) : (
              <div className="flex items-center space-x-2">
                <Link 
                  href="/login"
                  className="bg-blue-500 hover:bg-blue-600 text-white px-4 py-2 rounded-md text-sm font-medium"
                >
                  로그인
                </Link>
                <Link 
                  href="/register"
                  className="bg-green-500 hover:bg-green-600 text-white px-4 py-2 rounded-md text-sm font-medium"
                >
                  회원가입
                </Link>
              </div>
            )}
          </div>
        </div>
      </div>
    </nav>
  );
}
