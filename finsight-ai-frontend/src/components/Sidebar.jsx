import { NavLink } from "react-router-dom";
import {
  FaChartPie,
  FaExchangeAlt,
  FaChartBar,
  FaRobot,
  FaUser,
  FaCog,
} from "react-icons/fa";

const menuItems = [
  {
    name: "Dashboard",
    path: "/",
    icon: <FaChartPie />,
  },
  {
    name: "Transactions",
    path: "/transactions",
    icon: <FaExchangeAlt />,
  },
  {
    name: "Analytics",
    path: "/analytics",
    icon: <FaChartBar />,
  },
  {
    name: "AI Insights",
    path: "/ai-insights",
    icon: <FaRobot />,
  },
  {
    name: "Profile",
    path: "/profile",
    icon: <FaUser />,
  },
  {
    name: "Settings",
    path: "/settings",
    icon: <FaCog />,
  },
];

export default function Sidebar() {
  return (
    <aside className="w-72 bg-slate-900 text-white min-h-screen shadow-2xl">
      <div className="text-center py-8 border-b border-slate-700">
        <h1 className="text-3xl font-bold text-cyan-400">
          FinSight AI
        </h1>

        <p className="text-slate-400 mt-2 text-sm">
          Personal Finance Intelligence
        </p>
      </div>

      <nav className="mt-8 px-4">
        {menuItems.map((item) => (
          <NavLink
            key={item.path}
            to={item.path}
            className={({ isActive }) =>
              `flex items-center gap-4 p-4 rounded-xl mb-3 transition-all duration-300 ${
                isActive
                  ? "bg-cyan-600 text-white shadow-lg"
                  : "hover:bg-slate-800 text-slate-300"
              }`
            }
          >
            <span className="text-xl">{item.icon}</span>

            <span className="font-medium">
              {item.name}
            </span>
          </NavLink>
        ))}
      </nav>
    </aside>
  );
}