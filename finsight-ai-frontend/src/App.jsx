import { BrowserRouter, Routes, Route } from "react-router-dom";

import Dashboard from "./pages/Dashboard";
import Transactions from "./pages/Transactions";
import Analytics from "./pages/Analytics";
import AIInsights from "./pages/AIInsights";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Dashboard />} />
        <Route
          path="/transactions"
          element={<Transactions />}
        />
        <Route
          path="/analytics"
          element={<Analytics />}
        />
        <Route
          path="/ai-insights"
          element={<AIInsights />}
        />
      </Routes>
    </BrowserRouter>
  );
}

export default App;