import { useEffect, useState } from "react";
import { motion } from "framer-motion";

import AnalyticsCard from "../components/analytics/AnalyticsCard";
import IncomeExpenseChart from "../components/analytics/IncomeExpenseChart";
import CategoryDonutChart from "../components/analytics/CategoryDonutChart";
import MonthlyTrendChart from "../components/analytics/MonthlyTrendChart";
import AIInsightCard from "../components/analytics/AIInsightCard";

import {
  getDashboardData,
  getCategoryBreakdown,
  getMonthlyInsights,
} from "../services/api";

const USER_ID = 19;

export default function Analytics() {
  const [dashboard, setDashboard] = useState(null);
  const [monthly, setMonthly] = useState(null);
  const [categories, setCategories] = useState({});

  useEffect(() => {
    const loadAnalytics = async () => {
      try {
        const [
          dashboardData,
          monthlyData,
          categoryData,
        ] = await Promise.all([
          getDashboardData(USER_ID),
          getMonthlyInsights(USER_ID),
          getCategoryBreakdown(USER_ID),
        ]);

        setDashboard(dashboardData);
        setMonthly(monthlyData);
        setCategories(categoryData);
      } catch (error) {
        console.error(error);
      }
    };

    loadAnalytics();
  }, []);

  if (!dashboard) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-slate-100 via-slate-50 to-blue-100">
        <div className="text-center">
          <div className="w-16 h-16 border-4 border-blue-500 border-t-transparent rounded-full animate-spin mx-auto"></div>

          <p className="mt-5 text-slate-600 text-lg font-medium">
            Loading Analytics...
          </p>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-slate-100 via-slate-50 to-blue-100">
      <div className="max-w-7xl mx-auto p-8">

        {/* Header */}

        <motion.div
          initial={{ opacity: 0, y: -20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.5 }}
          className="mb-10"
        >
          <p className="uppercase tracking-[0.2em] text-blue-600 font-semibold">
            FinSight AI
          </p>

          <h1 className="text-5xl font-extrabold text-slate-800 mt-2">
            Analytics 📊
          </h1>

          <p className="text-slate-500 mt-3 text-lg">
            Track your financial performance and spending trends.
          </p>
        </motion.div>

        {/* KPI Cards */}

        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">

          <AnalyticsCard
            title="Total Income"
            value={`₹${dashboard.income.toLocaleString()}`}
            color="text-green-600"
            subtitle="Current Month"
          />

          <AnalyticsCard
            title="Total Expenses"
            value={`₹${dashboard.expenses.toLocaleString()}`}
            color="text-red-600"
            subtitle="Current Month"
          />

          <AnalyticsCard
            title="Savings Rate"
            value={`${
              dashboard.income
                ? Math.round(
                    (dashboard.savings / dashboard.income) * 100
                  )
                : 0
            }%`}
            color="text-blue-600"
            subtitle="Financial Health"
          />

        </div>

        {/* Charts */}

        <div className="grid grid-cols-1 xl:grid-cols-2 gap-8 mt-8">

          <IncomeExpenseChart
            monthly={monthly}
          />

          <CategoryDonutChart
            categories={categories}
          />

        </div>

        {/* Monthly Trend */}

        <div className="mt-8">

          <MonthlyTrendChart
            monthly={monthly}
          />

        </div>

        {/* AI Insights */}

        <div className="mt-8">

          <AIInsightCard
            dashboard={dashboard}
            categories={categories}
          />

        </div>

      </div>
    </div>
  );
}