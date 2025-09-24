import type { Metadata } from "next";
import { Noto_Sans_KR } from "next/font/google";
import "./globals.css";
import Providers from "@/providers/query-client";
import { SeasonProvider } from "@/contexts/SeasonContext";
import SeasonSelector from "@/components/SeasonSelector";
import Link from "next/link";

const notoSans = Noto_Sans_KR({
  variable: "--font-sans",
  subsets: ["latin"], // 한글은 자동 서브셋. 필요시 weight 지정 가능
  display: "swap", //임시 폰트 먼저 보여주고 폰트 로드
});

export const metadata: Metadata = {
  title: "Plant Report | 식물기록",
  description: "물주기 일정/기록을 한 눈에",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="ko">
              <body className={`${notoSans.variable} antialiased bg-gray-50 text-gray-900`}>
                <Providers>
                  <SeasonProvider>
                    <div className="flex min-h-screen">
            {/* 왼쪽 네비게이션 */}
            <nav className="w-64 bg-white shadow-md p-4">
              <h1 className="text-xl font-bold mb-6 text-green-600">🌱 PLANT REPORT</h1>
              
              {/* 계절 선택기 */}
              <div className="mb-6">
                <SeasonSelector />
              </div>
              
              <ul className="space-y-2">
                <li>
                  <Link href="/" className="block p-2 rounded hover:bg-gray-100 text-gray-900">
                    🏠 HOME
                  </Link>
                </li>
                <li>
                  <Link href="/register" className="block p-2 rounded hover:bg-gray-100 text-gray-900">
                    ➕ 식물 등록
                  </Link>
                </li>
                <li>
                  <Link href="/plantList" className="block p-2 rounded hover:bg-gray-100 text-gray-900">
                    📋 식물 목록
                  </Link>
                </li>
                <li>
                  <Link href="/settings" className="block p-2 rounded hover:bg-gray-100 text-gray-900">
                    ⚙️ 설정
                  </Link>
                </li>
              </ul>
            </nav>
            
            {/* 메인 내용 */}
            <main className="flex-1 p-6 bg-white">
              {children}
                    </main>
                    </div>
                  </SeasonProvider>
                </Providers>
              </body>
    </html>
  );
}
