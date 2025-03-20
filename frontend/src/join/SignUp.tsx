import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { Button } from "../components/ui/Button";
import { Input } from "../components/ui/Input";

export default function SignupPage() {
  const [userNickname, setUserNickName] = useState<string>("");
  const [userEmail, setUserEmail] = useState<string>("");
  const [userPw, setUserPw] = useState<string>("");
  const [userPwCheck, setUserPwCheck] = useState<string>("");
  const [userIntroduce, setUserIntroduce] = useState<string>("");
  const [isChecked, setIsChecked] = useState<boolean>(false);
  const [error, setError] = useState<string>("");

  const navigate = useNavigate();

  // 비밀번호 일치 여부 확인
  const isPasswordMatch = userPw === userPwCheck;

  // 회원가입 요청 함수
  const handleSignup = async () => {
    if (!isChecked) {
      setError("이용약관과 개인정보 취급방침에 동의해야 합니다.");
      return;
    }

    if (!isPasswordMatch) {
      setError("비밀번호가 일치하지 않습니다.");
      return;
    }

    console.log("📝 전송할 데이터:", {
      userNickname,
      userEmail,
      userPw,
      userPwCheck,
      userIntroduce,
    });

    try {
      const response = await fetch("http://localhost:8080/api/signup", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          userNickname,
          userEmail,
          userPw,
          userPwCheck,
          userIntroduce,
        }),
      });

      const data = await response.json();
    //   console.log("🔵 백엔드 응답:", data);

      if (response.ok) {
        alert(data.message); // 회원가입 성공 메시지
        navigate("/");
      } else {
        setError(data.message); // 실패 메시지 설정
      }

    } catch (err) {
      if (err instanceof Error) {
        setError(err.message);
      }
    }
  };

  return (
    <div className="min-h-screen flex flex-col items-center justify-center bg-gray-100">
      <div className="bg-white p-6 rounded-lg shadow-lg w-96">
        <h2 className="text-2xl font-bold text-center mb-4">회원가입</h2>

        {error && <p className="text-red-500 text-sm text-center mb-3">{error}</p>}

        <label className="block text-gray-700 font-medium mb-1">프로필 이름</label>
        <Input type="text" placeholder="프로필 이름" className="mb-3" value={userNickname} onChange={(e) => setUserNickName(e.target.value)} />

        <label className="block text-gray-700 font-medium mb-1">이메일</label>
        <Input type="email" placeholder="이메일" className="mb-3" value={userEmail} onChange={(e) => setUserEmail(e.target.value)} />

        <label className="block text-gray-700 font-medium mb-1">비밀번호</label>
        <Input type="password" placeholder="비밀번호" className="mb-3" value={userPw} onChange={(e) => setUserPw(e.target.value)} />

        <label className="block text-gray-700 font-medium mb-1">비밀번호 확인</label>
        <Input type="password" placeholder="비밀번호 확인" className="mb-3" value={userPwCheck} onChange={(e) => setUserPwCheck(e.target.value)} />

        {!isPasswordMatch && userPwCheck.length > 0 && <p className="text-red-500 text-sm mb-3">비밀번호가 일치하지 않습니다.</p>}

        <label className="block text-gray-700 font-medium mb-1">한 줄 소개</label>
        <Input type="text" placeholder="한 줄 소개" className="mb-3" value={userIntroduce} onChange={(e) => setUserIntroduce(e.target.value)} />

        <div className="flex items-center gap-2 my-3">
          <input type="checkbox" checked={isChecked} onChange={() => setIsChecked(!isChecked)} className="w-4 h-4" />
          <span className="text-sm text-gray-700">이용약관과 개인정보 취급방침에 동의합니다.</span>
        </div>

        <div className="flex justify-between mt-4">
          <Button onClick={() => navigate("/")}>취소</Button>
          <Button onClick={handleSignup} disabled={!isChecked || !isPasswordMatch}>가입</Button>
        </div>
      </div>
    </div>
  );
}
