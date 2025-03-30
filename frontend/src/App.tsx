import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import MainPage from "./main/MainPage";
import SignupPage from "./join/SignUp";
import GoogleCallback from "./oauth/GoogleCallback"

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<MainPage />} />
        <Route path="/signup" element={<SignupPage />} />
        <Route path="/oauth2/google/callback" element={<GoogleCallback />} />
      </Routes>
    </Router>
  );
}

export default App;
