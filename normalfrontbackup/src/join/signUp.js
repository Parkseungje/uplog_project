import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { Button } from "../components/ui/button";
import { Input } from "../components/ui/input";

export default function SignupPage() {
  const [userNickname, setUserNickName] = useState("");
  const [userEmail, setUserEmail] = useState("");
  const [userPw, setUserPw] = useState("");
  const [userPwCheck, setUserPwCheck] = useState("");
  const [userIntroduce, setUserIntroduce] = useState("");
  const [isChecked, setIsChecked] = useState(false);
  const [error, setError] = useState("");

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

      //console.log("📡 요청이 전송됨! 응답을 기다리는 중...");

      //const responseData = await response.json(); // 백엔드 응답 확인
      //console.log("🔵 백엔드 응답 데이터:", responseData); // 응답 확인

      if (!response.ok) {
        throw new Error("회원가입 실패! 다시 시도해주세요.");
      }

      alert("회원가입 성공! 로그인 페이지로 이동합니다.");
      navigate("/");
    } catch (err) {
      setError(err.message);
    }
  };

  return (
    <div className="min-h-screen flex flex-col items-center justify-center bg-gray-100">
      <div className="bg-white p-6 rounded-lg shadow-lg w-96">
        <h2 className="text-2xl font-bold text-center mb-4">회원가입</h2>

        {/* 오류 메시지 */}
        {error && <p className="text-red-500 text-sm text-center mb-3">{error}</p>}

        {/* 프로필 이름 */}
        <label className="block text-gray-700 font-medium mb-1">프로필 이름</label>
        <Input
          type="text"
          placeholder="프로필 이름"
          className="mb-3"
          value={userNickname}
          onChange={(e) => setUserNickName(e.target.value)}
        />

        {/* 이메일 */}
        <label className="block text-gray-700 font-medium mb-1">이메일</label>
        <Input
          type="email"
          placeholder="이메일"
          className="mb-3"
          value={userEmail}
          onChange={(e) => setUserEmail(e.target.value)}
        />

        {/* 패스워드 */}
        <label className="block text-gray-700 font-medium mb-1">비밀번호</label>
        <Input
          type="password"
          placeholder="비밀번호"
          className="mb-3"
          value={userPw}
          onChange={(e) => setUserPw(e.target.value)}
        />

        {/* 패스워드 확인 */}
        <label className="block text-gray-700 font-medium mb-1">비밀번호 확인</label>
        <Input
          type="password"
          placeholder="비밀번호 확인"
          className="mb-3"
          value={userPwCheck}
          onChange={(e) => setUserPwCheck(e.target.value)}
        />

        {/* 비밀번호 불일치 경고 메시지 */}
        {!isPasswordMatch && userPwCheck.length > 0 && (
          <p className="text-red-500 text-sm mb-3">비밀번호가 일치하지 않습니다.</p>
        )}

        {/* 한 줄 소개 */}
        <label className="block text-gray-700 font-medium mb-1">한 줄 소개</label>
        <Input
          type="text"
          placeholder="한 줄 소개"
          className="mb-3"
          value={userIntroduce}
          onChange={(e) => setUserIntroduce(e.target.value)}
        />

        {/* 이용약관 & 개인정보 동의 체크박스 */}
        <div className="flex items-center gap-2 my-3">
          <input
            type="checkbox"
            checked={isChecked}
            onChange={() => setIsChecked(!isChecked)}
            className="w-4 h-4"
          />
          <span className="text-sm text-gray-700">
            이용약관과 개인정보 취급방침에 동의합니다.
          </span>
        </div>

        {/* 버튼 영역 */}
        <div className="flex justify-between mt-4">
          <Button onClick={() => navigate("/")}>취소</Button>
          <Button onClick={handleSignup} disabled={!isChecked || !isPasswordMatch}>
            가입
          </Button>
        </div>
      </div>
    </div>
  );
}
