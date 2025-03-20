import React from "react";
import { useState } from "react";
import { motion } from "framer-motion";
import GithubIcon from "../assets/icons/github-icon.svg";
import GoogleIcon from "../assets/icons/google-icon.svg";
import { useNavigate } from "react-router-dom";
import { Button } from "../components/ui/Button";
import { Input } from "../components/ui/Input";

export default function MainPage() {
  const [isLoginOpen, setIsLoginOpen] = useState<boolean>(false);
  const navigate = useNavigate();

  const GithubButton = () => (
    <button className="p-3 bg-white text-white rounded-full hover:bg-gray-700 transition">
      <img src={GithubIcon} alt="GitHub 로그인" className="w-6 h-6" />
    </button>
  );

  const GoogleButton = () => (
    <button className="p-3 bg-white text-white rounded-full hover:bg-gray-700 transition">
      <img src={GoogleIcon} alt="Google 로그인" className="w-6 h-6" />
    </button>
  );

  return (
    <div className="min-h-screen flex flex-col items-center bg-gradient-to-br from-blue-400 to-purple-500 text-white">
      {/* 헤더 */}
      <header className="w-full flex justify-between items-center p-5 bg-white/10 backdrop-blur-md shadow-md">
        <button onClick={() => window.location.reload()} className="text-2xl font-extrabold tracking-wide">
          🌟 Uplog
        </button>
        <Button onClick={() => setIsLoginOpen(true)} className="px-5 py-2 rounded-full bg-white/20 hover:bg-white/30">
          로그인
        </Button>
      </header>

      {/* 메인 컨텐츠 */}
      <main className="flex-1 flex flex-col items-center justify-center text-center">
        <h1 className="text-6xl font-extrabold bg-clip-text text-transparent bg-gradient-to-r from-yellow-300 via-pink-400 to-red-500">
          Welcome to Uplog 🚀
        </h1>
        <p className="text-lg text-white/80 mt-4 max-w-lg">
          Discover and share amazing content with the world.
        </p>
      </main>

      {/* 로그인 모달 */}
      {isLoginOpen && (
        <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50 backdrop-blur-md">
          <motion.div
            initial={{ opacity: 0, scale: 0.9 }}
            animate={{ opacity: 1, scale: 1 }}
            exit={{ opacity: 0, scale: 0.9 }}
            className="bg-white/20 p-6 rounded-xl shadow-lg w-96 backdrop-blur-md border border-white/30"
          >
            <h2 className="text-2xl font-bold text-center mb-4 text-white">로그인</h2>
            <Input type="email" placeholder="이메일" className="mb-3 bg-white/30 text-white placeholder-white/60" />
            <Input type="password" placeholder="비밀번호" className="mb-3 bg-white/30 text-white placeholder-white/60" />
            <Button className="w-full mb-3 bg-blue-500 hover:bg-blue-600 text-white">로그인</Button>

            <div className="flex justify-center gap-4 my-3">
              <GithubButton />
              <GoogleButton />
            </div>

            <button
              className="text-yellow-300 w-full text-center mt-3 hover:text-yellow-400 transition"
              onClick={() => navigate("/signup")}
            >
              회원가입
            </button>
            <Button className="w-full mt-3 border-white/50 text-white" onClick={() => setIsLoginOpen(false)}>
              닫기
            </Button>
          </motion.div>
        </div>
      )}
    </div>
  );
}
