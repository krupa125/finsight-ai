import { NavLink } from "react-router-dom";

export default function Sidebar() {
  return (
    <div className="w-64 bg-slate-900 text-white p-6">
      <h2 className="text-2xl font-bold mb-10">
        FinSight AI
      </h2>

      <ul className="space-y-4">

        <li>
          <NavLink
            to="/"
            className={({ isActive }) =>
              `block p-3 rounded-lg ${
                isActive
                  ? "bg-blue-600"
                  : "hover:bg-slate-700"
              }`
            }
          >
            Dashboard
          </NavLink>
        </li>

        <li>
          <NavLink
            to="/transactions"
            className={({ isActive }) =>
              `block p-3 rounded-lg ${
                isActive
                  ? "bg-blue-600"
                  : "hover:bg-slate-700"
              }`
            }
          >
            Transactions
          </NavLink>
        </li>

        <li>
          <NavLink
            to="/analytics"
            className={({ isActive }) =>
              `block p-3 rounded-lg ${
                isActive
                  ? "bg-blue-600"
                  : "hover:bg-slate-700"
              }`
            }
          >
            Analytics
          </NavLink>
        </li>

        <li>
          <NavLink
            to="/ai-insights"
            className={({ isActive }) =>
              `block p-3 rounded-lg ${
                isActive
                  ? "bg-blue-600"
                  : "hover:bg-slate-700"
              }`
            }
          >
            AI Insights
          </NavLink>
        </li>

      </ul>
    </div>
  );
}