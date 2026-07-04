import { useEffect, useState } from "react";
import { motion } from "framer-motion";

import PremiumCard from "../components/dashboard/PremiumCard";
import ActivityCard from "../components/dashboard/ActivityCard";

import SpendingPieChart from "../components/SpendingPieChart";
import MonthlyBarChart from "../components/MonthlyBarChart";
import TransactionTable from "../components/TransactionTable";
import PageHeader from "../components/PageHeader";

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
    <div className="min-h-screen bg-slate-100 p-8">

      <PageHeader
        title="Dashboard"
        subtitle="Welcome back! Here's your financial overview."
      />

      {/* Premium Cards */}

      <motion.div
        initial={{ opacity: 0, y: 25 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.6 }}
        className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-4 gap-6 mt-6"
      >
        <PremiumCard
          title="Income"
          value={dashboard?.income || 0}
          icon={<FaArrowDown size={26} />}
          color="bg-green-500"
          change="+12%"
        />

        <PremiumCard
          title="Expenses"
          value={dashboard?.expenses || 0}
          icon={<FaArrowUp size={26} />}
          color="bg-red-500"
          change="-5%"
        />

        <PremiumCard
          title="Savings"
          value={dashboard?.savings || 0}
          icon={<FaPiggyBank size={26} />}
          color="bg-blue-500"
          change="+18%"
        />

        <PremiumCard
          title="Balance"
          value={(dashboard?.income || 0) - (dashboard?.expenses || 0)}
          icon={<FaWallet size={26} />}
          color="bg-purple-500"
          change="+8%"
        />
      </motion.div>

      {/* Activity */}

      <motion.div
        initial={{ opacity: 0 }}
        animate={{ opacity: 1 }}
        transition={{ delay: 0.2 }}
        className="mt-8"
      >
        <ActivityCard
          income={dashboard?.income || 0}
          expenses={dashboard?.expenses || 0}
          savings={dashboard?.savings || 0}
        />
      </motion.div>

      {/* Charts */}

      <motion.div
        initial={{ opacity: 0 }}
        animate={{ opacity: 1 }}
        transition={{ delay: 0.4 }}
        className="grid grid-cols-1 xl:grid-cols-2 gap-8 mt-8"
      >
        <div className="bg-white rounded-3xl shadow-lg p-6">

          <h2 className="text-xl font-bold mb-4">
            Spending Breakdown
          </h2>

          <SpendingPieChart categories={categories} />

        </div>

        <div className="bg-white rounded-3xl shadow-lg p-6">

          <h2 className="text-xl font-bold mb-4">
            Monthly Overview
          </h2>

          <MonthlyBarChart monthly={monthly} />

        </div>

      </motion.div>

      {/* Transactions */}

      <motion.div
        initial={{ opacity: 0 }}
        animate={{ opacity: 1 }}
        transition={{ delay: 0.6 }}
        className="bg-white rounded-3xl shadow-lg p-6 mt-8"
      >

        <h2 className="text-xl font-bold mb-4">
          Recent Transactions
        </h2>

        <TransactionTable transactions={transactions} />

      </motion.div>

    </div>
  );
}