import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import MainPage from "./main/MainPage";
import SignupPage from "./join/SignUp";

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<MainPage />} />
        <Route path="/signup" element={<SignupPage />} />
      </Routes>
    </Router>
  );
}

export default App;
