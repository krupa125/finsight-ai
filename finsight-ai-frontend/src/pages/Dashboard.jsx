import { useEffect, useState } from "react";
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
    <div style={{ padding: "20px" }}>
      <h1>FinSight AI Dashboard</h1>

      <h2>Dashboard Summary</h2>
      <pre>{JSON.stringify(dashboard, null, 2)}</pre>

      <h2>Category Breakdown</h2>
      <pre>{JSON.stringify(categories, null, 2)}</pre>

      <h2>Monthly Insights</h2>
      <pre>{JSON.stringify(monthly, null, 2)}</pre>

      <h2>Transaction History</h2>
      <pre>{JSON.stringify(transactions, null, 2)}</pre>
    </div>
  );
}