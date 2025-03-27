import { useEffect } from "react";
import { useSearchParams, useNavigate } from "react-router-dom";

export default function GoogleCallback() {
  const [params] = useSearchParams();
  const navigate = useNavigate();

  useEffect(() => {
    const code = params.get("code");

    if (code) {
      //console.log("✅ 받은 code:", code);

      // 백엔드로 code 전달
      fetch("/api/user/oauth/google/callback", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ code }),
      })
        .then(res => res.json())
        .then(redata => {
          // JWT 저장 & 로그인 처리
          localStorage.setItem("accessToken", redata.data.token);
          localStorage.setItem("userNickname", redata.data.userNickname);

          // ✅ 너무 일찍 렌더링하면 userNickname 이 undefined로 나옴
            setTimeout(() => {
              navigate("/"); //홈으로 이동
            }, 600); // 10~50ms 정도면 충분
        })
        .catch(err => {
          console.error("OAuth 로그인 실패:", err);
        });
    }
  }, [params, navigate]);

  return <p></p>;
}
