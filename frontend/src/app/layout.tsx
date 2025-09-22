import type { Metadata } from "next";
import { Noto_Sans_KR } from "next/font/google";
import "./globals.css";
import Providers from "@/providers/query-client";

const notoSans = Noto_Sans_KR({
  variable: "--font-sans",
  subsets: ["latin"], // 한글은 자동 서브셋. 필요시 weight 지정 가능
  display: "swap", //임시 폰트 먼저 보여주고 폰트 로드
});

console.log("API =", process.env.NEXT_PUBLIC_API_URL);

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
      <body
        className={`${notoSans.variable} antialiased bg-gray-50 text-gray-900`}
      >
        <Providers>{children}</Providers>
      </body>
    </html>
  );
}
