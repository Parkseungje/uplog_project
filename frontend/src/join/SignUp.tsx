import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { Button } from "../components/ui/Button";
import { Input } from "../components/ui/Input";
import { useEffect } from "react";
import { useSearchParams } from "react-router-dom";
import { jwtDecode } from "jwt-decode";

export default function SignupPage() {
  const [name, setName] = useState<string>("");
  const [email, setEmail] = useState<string>("");
  const [isEmailLocked, setIsEmailLocked] = useState<boolean>(false);
  const [domainName, setDomainName] = useState<string>("");
  const [introduce, setIntroduce] = useState<string>("");
  const [isChecked, setIsChecked] = useState<boolean>(false);
  const [error, setError] = useState<string>("");

  const navigate = useNavigate();

  const isValidUserId = /^[a-z0-9_-]{3,16}$/.test(domainName);
  const isFormValid =
    name.trim() !== "" &&
    email.trim() !== "" &&
    domainName.trim() !== "" &&
    isChecked;

  // URL 쿼리에서 code 추출
  const [searchParams] = useSearchParams();

  useEffect(() => {
    const token = localStorage.getItem("accessToken");
    if (token) {
      try {
        const decoded: any = jwtDecode(token);
        if (decoded.email) {
          setEmail(decoded.email);
          setIsEmailLocked(true);
        }
        if (decoded.name) {
          setName(decoded.name);
        }
      } catch (err) {
        console.error("❌ JWT 디코딩 실패:", err);
      }
    }
  }, []);

  useEffect(() => {
    const codeParam = searchParams.get("code");
    if (codeParam) {
      try {
        const decodedEmail = atob(codeParam); // base64 디코딩
        setEmail(decodedEmail);
        setIsEmailLocked(true); // 입력 비활성화
      } catch (err) {
        console.error("Base64 디코딩 실패:", err);
      }
    }
  }, [searchParams]);

  const handleSignup = async () => {
    if (!isChecked) {
      setError("이용약관과 개인정보 취급방침에 동의해야 합니다.");
      return;
    }

    if (!isValidUserId) {
      setError("사용자 ID는 3~16자의 알파벳 소문자,숫자,혹은 - _ 으로 이루어져야 합니다.");
      return;
    }

    try {
      const response = await fetch("/api/user/", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          name,
          email,
          domainName,
          introduce,
        }),
      });

      const data = await response.json();

      if (response.ok) {
        alert(data.message);
        navigate("/");
      } else {
        setError(data.message);
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
        <Input type="text" placeholder="프로필 이름" className="mb-3" value={name} onChange={(e) => setName(e.target.value)} />

        <label className="block text-gray-700 font-medium mb-1">이메일</label>
        <Input type="email" placeholder="이메일" className="mb-3" value={email} onChange={(e) => setEmail(e.target.value)} disabled={isEmailLocked} />

        <label className="block text-gray-700 font-medium mb-1">사용자 ID</label>
        <Input type="text" placeholder="사용자 ID" className="mb-3" value={domainName} onChange={(e) => setDomainName(e.target.value)} />

        <label className="block text-gray-700 font-medium mb-1">한 줄 소개</label>
        <Input type="text" placeholder="한 줄 소개" className="mb-3" value={introduce} onChange={(e) => setIntroduce(e.target.value)} />

        <div className="flex items-center gap-2 my-3">
          <input type="checkbox" checked={isChecked} onChange={() => setIsChecked(!isChecked)} className="w-4 h-4" />
          <span className="text-sm text-gray-700">이용약관과 개인정보 취급방침에 동의합니다.</span>
        </div>

        <div className="flex justify-between mt-4">
          <Button onClick={() => navigate("/")}>취소</Button>
          <Button onClick={handleSignup} disabled={!isFormValid}>가입</Button>
        </div>
      </div>
    </div>
  );
}
