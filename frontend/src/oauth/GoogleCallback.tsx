import { useEffect } from "react";
import { useSearchParams, useNavigate } from "react-router-dom";
import { jwtDecode } from "jwt-decode";

export default function GoogleCallback() {
  const [params] = useSearchParams();
  const navigate = useNavigate();

  useEffect(() => {
    const code = params.get("code");

    if (code) {
      //console.log("✅ 받은 code:", code);

      // 백엔드로 code 전달
      fetch("/api/oauth2/google/callback", {
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

          // ✅ 너무 일찍 렌더링하면 userNickname 이 undefined로 나옴
          try {
            const decoded: any = jwtDecode(redata.data.token);
            if (decoded.name) {
              localStorage.setItem("userNickname", decoded.name);
            }
          } catch (err) {
            console.error("JWT 디코딩 실패:", err);
          }
          
          setTimeout(() => {
            if (redata.data.exist == "N") {
              navigate("/signup");
            } else {
              navigate("/");
            }
          }, 600);
        })
        .catch(err => {
          console.error("OAuth 로그인 실패:", err);
        });
    }
  }, [params, navigate]);

  return <p></p>;
}
