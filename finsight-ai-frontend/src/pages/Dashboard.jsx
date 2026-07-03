import { useEffect, useState } from "react";
import DashboardCard from "../components/DashboardCard";
import {
  FaArrowDown,
  FaArrowUp,
  FaPiggyBank,
  FaWallet,
} from "react-icons/fa";

import {
  getDashboardData,
  getCategoryBreakdown,
  getMonthlyInsights,
  getTransactionHistory,
} from "../services/api";

const USER_ID = 17; // Change if you're using a different user

export default function Dashboard() {
  const [dashboard, setDashboard] = useState(null);
  const [categories, setCategories] = useState({});
  const [monthly, setMonthly] = useState(null);
  const [transactions, setTransactions] = useState([]);

  useEffect(() => {
    const loadData = async () => {
      try {
        const dashboardData = await getDashboardData(USER_ID);
        const categoryData = await getCategoryBreakdown(USER_ID);
        const monthlyData = await getMonthlyInsights(USER_ID);
        const transactionData = await getTransactionHistory(USER_ID);

        setDashboard(dashboardData);
        setCategories(categoryData);
        setMonthly(monthlyData);
        setTransactions(transactionData);
      } catch (error) {
        console.error("Error loading dashboard:", error);
      }
    };

    loadData();
  }, []);

  return (
  <div className="p-8 bg-slate-100 min-h-screen">
    <h1 className="text-4xl font-bold mb-8">
      FinSight AI Dashboard
    </h1>

    <div className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-4 gap-6">

      <DashboardCard
        title="Income"
        value={dashboard?.income || 0}
        icon={<FaArrowDown />}
        color="linear-gradient(135deg,#10B981,#059669)"
      />

      <DashboardCard
        title="Expenses"
        value={dashboard?.expenses || 0}
        icon={<FaArrowUp />}
        color="linear-gradient(135deg,#EF4444,#DC2626)"
      />

      <DashboardCard
        title="Savings"
        value={dashboard?.savings || 0}
        icon={<FaPiggyBank />}
        color="linear-gradient(135deg,#3B82F6,#2563EB)"
      />

      <DashboardCard
        title="Balance"
        value={(dashboard?.income || 0) - (dashboard?.expenses || 0)}
        icon={<FaWallet />}
        color="linear-gradient(135deg,#8B5CF6,#6D28D9)"
      />

    </div>
  </div>
);
}