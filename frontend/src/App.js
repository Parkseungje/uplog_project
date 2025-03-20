import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import MainPage from "./main/mainPage";
import SignupPage from "./join/signUp";

export default function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<MainPage />} />
        <Route path="/signup" element={<SignupPage />} />
      </Routes>
    </Router>
  );
}
