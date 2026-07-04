import { useEffect, useState } from "react";
import { motion } from "framer-motion";

import PremiumCard from "../components/dashboard/PremiumCard";
import ActivityCard from "../components/dashboard/ActivityCard";
import SpendingPieChart from "../components/SpendingPieChart";
import MonthlyBarChart from "../components/MonthlyBarChart";
import TransactionTable from "../components/TransactionTable";

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

const USER_ID = 19;

export default function Dashboard() {
  const [dashboard, setDashboard] = useState(null);
  const [categories, setCategories] = useState({});
  const [monthly, setMonthly] = useState(null);
  const [transactions, setTransactions] = useState([]);

  useEffect(() => {
    const loadData = async () => {
      try {
        const [
          dashboardData,
          categoryData,
          monthlyData,
          transactionData,
        ] = await Promise.all([
          getDashboardData(USER_ID),
          getCategoryBreakdown(USER_ID),
          getMonthlyInsights(USER_ID),
          getTransactionHistory(USER_ID),
        ]);

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

  const hour = new Date().getHours();

  const greeting =
    hour < 12
      ? "Good Morning"
      : hour < 17
      ? "Good Afternoon"
      : "Good Evening";

  if (!dashboard) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-slate-100 via-slate-50 to-blue-100">
        <div className="text-center">
          <div className="w-16 h-16 border-4 border-blue-500 border-t-transparent rounded-full animate-spin mx-auto"></div>

          <p className="mt-5 text-slate-600 text-lg font-medium">
            Loading FinSight AI...
          </p>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-slate-100 via-slate-50 to-blue-100">
      <div className="max-w-7xl mx-auto p-8">

        {/* Hero */}

        <motion.div
          initial={{ opacity: 0, y: -15 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.6 }}
          className="mb-12"
        >
          <p className="text-blue-600 font-semibold uppercase tracking-[0.2em]">
            FinSight AI
          </p>

          <h1 className="text-5xl md:text-6xl font-extrabold text-slate-800 mt-2">
            {greeting} 👋
          </h1>

          <p className="text-slate-500 mt-3 text-lg">
            Here's your financial overview for today.
          </p>
        </motion.div>

        {/* KPI Cards */}

        <motion.div
          initial={{ opacity: 0, y: 25 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{
            duration: 0.6,
            ease: "easeOut",
          }}
          className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-4 gap-6"
        >
          <PremiumCard
            title="Income"
            value={dashboard.income}
            icon={<FaArrowDown size={26} />}
            color="bg-gradient-to-r from-green-500 to-emerald-600"
            change="+12%"
          />

          <PremiumCard
            title="Expenses"
            value={dashboard.expenses}
            icon={<FaArrowUp size={26} />}
            color="bg-gradient-to-r from-red-500 to-pink-600"
            change="-5%"
          />

          <PremiumCard
            title="Savings"
            value={dashboard.savings}
            icon={<FaPiggyBank size={26} />}
            color="bg-gradient-to-r from-blue-500 to-cyan-600"
            change="+18%"
          />

          <PremiumCard
            title="Balance"
            value={dashboard.income - dashboard.expenses}
            icon={<FaWallet size={26} />}
            color="bg-gradient-to-r from-purple-500 to-indigo-600"
            change="+8%"
          />
        </motion.div>

        {/* Financial Health */}

        <motion.div
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          transition={{ delay: 0.2 }}
          className="mt-8"
        >
          <ActivityCard
            income={dashboard.income}
            expenses={dashboard.expenses}
            savings={dashboard.savings}
          />
        </motion.div>

        {/* Charts */}

        <motion.div
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          transition={{ delay: 0.4 }}
          className="grid grid-cols-1 xl:grid-cols-2 gap-8 mt-8"
        >
          <SpendingPieChart categories={categories} />

          <MonthlyBarChart monthly={monthly} />
        </motion.div>

        {/* Transactions */}

        <motion.div
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          transition={{ delay: 0.6 }}
          className="mt-8"
        >
          <div className="bg-white/80 backdrop-blur-xl rounded-3xl shadow-xl border border-white p-6">
            <h2 className="text-2xl font-bold text-slate-800 mb-6">
              Recent Transactions
            </h2>

            <TransactionTable transactions={transactions} />
          </div>
        </motion.div>

      </div>
    </div>
  );
}