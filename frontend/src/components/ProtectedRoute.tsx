'use client';

import { useEffect, useRef } from 'react';
import { useRouter } from 'next/navigation';
import { useAuth } from '@/contexts/AuthContext';

interface ProtectedRouteProps {
  children: React.ReactNode;
}

export default function ProtectedRoute({ children }: ProtectedRouteProps) {
  const { user, isLoading } = useAuth();
  const router = useRouter();
  const hasRedirected = useRef(false); // 리다이렉트 여부 추적

  useEffect(() => {
    // 로딩이 끝나고 사용자가 없으면 로그인 페이지로 리다이렉트
    if (!isLoading && !user && !hasRedirected.current) {
      hasRedirected.current = true; // 플래그 설정
      alert('로그인이 필요한 페이지입니다.');
      router.replace('/login');
    }
  }, [user, isLoading, router]);

  // 로딩 중일 때
  if (isLoading) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <div className="text-center">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-green-600 mx-auto mb-4"></div>
          <p className="text-gray-600">로딩 중...</p>
        </div>
      </div>
    );
  }

  // 인증되지 않은 경우 (리다이렉트 전까지 아무것도 보여주지 않음)
  if (!user) {
    return null;
  }

  // 인증된 경우 자식 컴포넌트 렌더링
  return <>{children}</>;
}
