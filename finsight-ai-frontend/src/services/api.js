import axios from "axios";

const api = axios.create({
  baseURL: "http://localhost:8081",
  headers: {
    "Content-Type": "application/json",
  },
});

export default api;

// Dashboard Summary
export const getDashboardData = async (userId) => {
  const response = await api.get(`/insights/dashboard/${userId}`);
  return response.data;
};

// Monthly Insights
export const getMonthlyInsights = async (userId) => {
  const response = await api.get(`/insights/monthly/${userId}`);
  return response.data;
};

// Category Breakdown
export const getCategoryBreakdown = async (userId) => {
  const response = await api.get(`/insights/categories/${userId}`);
  return response.data;
};

// Transaction History
export const getTransactionHistory = async (userId) => {
  const response = await api.get(`/users/history/${userId}`);
  return response.data;
};