import React, { useState, useEffect, useRef } from "react"; // ← useRef 추가
import { useNavigate } from "react-router-dom";
import { motion } from "framer-motion";
import GithubIcon from "../assets/icons/github-icon.svg";
import GoogleIcon from "../assets/icons/google-icon.svg";
import { Button } from "../components/ui/Button";
import { Input } from "../components/ui/Input";

export default function MainPage() {
  const [isLoginOpen, setIsLoginOpen] = useState<boolean>(false);
  const [userEmail, setEmail] = useState<string>("");
  const [userPw, setPassword] = useState<string>("");
  const [userNickname, setUserName] = useState<string | null>(() => {
    return localStorage.getItem("userNickname");
  });
  const [showLogout, setShowLogout] = useState<boolean>(false);

  const navigate = useNavigate();
  const dropdownRef = useRef<HTMLDivElement>(null);

  // 로그인 시도 함수
  const handleLogin = async () => {
    try {
      const response = await fetch("/api/user/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ userEmail, userPw }),
      });

      const redata = await response.json();

      if (response.ok) {
        localStorage.setItem("accessToken", redata.data.token); // JWT 저장
        setUserName(redata.data.userNickname); // 백엔드가 username도 같이 반환한다고 가정
        setIsLoginOpen(false); // 로그인 모달 닫기
      } else {
        alert("로그인 실패: " + redata.message);
      }
    } catch (error) {
      console.error("로그인 중 오류:", error);
      alert("서버 오류");
    }
  };

  // 로그아웃 함수
  const handleLogout = () => {
    localStorage.removeItem("accessToken");
    localStorage.removeItem("userNickname");
    setUserName(null);
    setShowLogout(false);
  };

  // 자동 로그인 유지용 (토큰 있을 때 사용자 정보 가져오기)
  useEffect(() => {
    const token = localStorage.getItem("accessToken");
    const nickname = localStorage.getItem("userNickname");
  
    // 👉 1. 로컬스토리지에 있는 값 먼저 반영
    if (nickname) {
      setUserName(nickname);
    }
  
    // 👉 2. 토큰이 있으면 백엔드에 실제 유저 정보 fetch (검증용)
    if (token) {
      fetch("/api/user/me", {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      })
        .then(res => res.json())
        .then(redata => {
          if (redata.data.userNickname) {
            setUserName(redata.data.userNickname);
          }
        })
        .catch(err => console.log("토큰으로 유저 조회 실패", err));
    }
  }, []);

  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (dropdownRef.current && !dropdownRef.current.contains(event.target as Node)) {
        setShowLogout(false);
      }
    };
  
    document.addEventListener("mousedown", handleClickOutside);
  
    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, [showLogout]);


  // 로그인/유저 이름 버튼
  // HeaderRight: 로그인 여부에 따라 UI 다르게 렌더링
  const HeaderRight = () =>
    userNickname ? (
      <div className="relative" ref={dropdownRef}>
        {/* 닉네임 버튼 */}
        <button
          onClick={(e) => {
            e.stopPropagation(); // 바깥 클릭 방지
            setShowLogout(!showLogout);
          }}
          className="px-4 py-2 bg-white/20 hover:bg-white/30 rounded-full text-white font-semibold transition duration-200"
        >
          {userNickname}님 ⌄
        </button>
  
        {/* 로그아웃 드롭다운 */}
        {showLogout && (
          <div className="absolute right-0 mt-2 w-40 bg-white text-gray-800 rounded-lg shadow-lg z-50">
            <button
              onClick={handleLogout}
              className="block w-full px-4 py-3 text-sm text-left rounded-lg hover:bg-gray-100 transition"
            >
              로그아웃
            </button>
          </div>
        )}
      </div>
    ) : (
      <Button
        onClick={() => setIsLoginOpen(true)}
        className="px-5 py-2 rounded-full bg-white/20 hover:bg-white/30"
      >
        로그인
      </Button>
    );
  

  return (
    <div className="min-h-screen flex flex-col items-center bg-gradient-to-br from-blue-400 to-purple-500 text-white">
      {/* 헤더 */}
      <header className="w-full flex justify-between items-center p-5 bg-white/10 backdrop-blur-md shadow-md">
        <button onClick={() => window.location.reload()} className="text-2xl font-extrabold tracking-wide">
          🌟 Uplog
        </button>
        <HeaderRight />
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
            <Input
              type="email"
              placeholder="이메일"
              value={userEmail}
              onChange={(e) => setEmail(e.target.value)}
              className="mb-3 bg-white/30 text-white placeholder-white/60"
            />
            <Input
              type="password"
              placeholder="비밀번호"
              value={userPw}
              onChange={(e) => setPassword(e.target.value)}
              className="mb-3 bg-white/30 text-white placeholder-white/60"
            />
            <Button onClick={handleLogin} className="w-full mb-3 bg-blue-500 hover:bg-blue-600 text-white">
              로그인
            </Button>

            <div className="flex justify-center gap-4 my-3">
              <button className="p-3 bg-white text-white rounded-full hover:bg-gray-700 transition">
                <img src={GithubIcon} alt="GitHub 로그인" className="w-6 h-6" />
              </button>
              <button
                className="p-3 bg-white text-white rounded-full hover:bg-gray-700 transition"
                onClick={() => {
                  window.location.href = `${process.env.REACT_APP_BACKEND}/api/user/oauth/google`;
                }}
              >
                <img src={GoogleIcon} alt="Google 로그인" className="w-6 h-6" />
              </button>
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
