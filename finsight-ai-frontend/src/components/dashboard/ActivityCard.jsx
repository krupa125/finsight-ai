import { motion } from "framer-motion";
import {
  FaArrowTrendUp,
  FaArrowTrendDown,
  FaPiggyBank,
  FaChartLine,
} from "react-icons/fa6";

export default function ActivityCard({
  income = 0,
  expenses = 0,
  savings = 0,
}) {
  const balance = income - expenses;

  const health =
    income === 0
      ? 0
      : Math.min(100, Math.max(0, Math.round((savings / income) * 100)));

  const getHealthColor = () => {
    if (health >= 80) return "bg-green-500";
    if (health >= 60) return "bg-blue-500";
    if (health >= 40) return "bg-yellow-500";
    return "bg-red-500";
  };

  const getHealthText = () => {
    if (health >= 80) return "Excellent";
    if (health >= 60) return "Good";
    if (health >= 40) return "Average";
    return "Needs Improvement";
  };

  return (
    <motion.div
      initial={{ opacity: 0, y: 25 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ duration: 0.6 }}
      className="bg-white rounded-3xl shadow-lg p-8"
    >
      <div className="flex items-center gap-3 mb-6">
        <FaChartLine className="text-blue-600 text-2xl" />
        <h2 className="text-2xl font-bold text-slate-800">
          Financial Health
        </h2>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">

        <div className="bg-green-50 rounded-2xl p-5">
          <div className="flex items-center gap-3 mb-2">
            <FaArrowTrendUp className="text-green-600 text-xl" />
            <span className="font-semibold text-slate-700">
              Income
            </span>
          </div>

          <p className="text-3xl font-bold text-green-600">
            ₹{income.toLocaleString()}
          </p>
        </div>

        <div className="bg-red-50 rounded-2xl p-5">
          <div className="flex items-center gap-3 mb-2">
            <FaArrowTrendDown className="text-red-600 text-xl" />
            <span className="font-semibold text-slate-700">
              Expenses
            </span>
          </div>

          <p className="text-3xl font-bold text-red-600">
            ₹{expenses.toLocaleString()}
          </p>
        </div>

        <div className="bg-blue-50 rounded-2xl p-5">
          <div className="flex items-center gap-3 mb-2">
            <FaPiggyBank className="text-blue-600 text-xl" />
            <span className="font-semibold text-slate-700">
              Balance
            </span>
          </div>

          <p className="text-3xl font-bold text-blue-600">
            ₹{balance.toLocaleString()}
          </p>
        </div>

      </div>

      <div className="mb-3 flex justify-between">
        <span className="font-semibold text-slate-700">
          Financial Health Score
        </span>

        <span className="font-bold text-slate-900">
          {health}%
        </span>
      </div>

      <div className="w-full h-4 bg-slate-200 rounded-full overflow-hidden">
        <motion.div
          initial={{ width: 0 }}
          animate={{ width: `${health}%` }}
          transition={{ duration: 1.2 }}
          className={`h-full rounded-full ${getHealthColor()}`}
        />
      </div>

      <div className="mt-4 flex justify-between items-center">
        <span className="text-slate-500">
          Status
        </span>

        <span
          className={`px-4 py-2 rounded-full text-white font-semibold ${getHealthColor()}`}
        >
          {getHealthText()}
        </span>
      </div>
    </motion.div>
  );
}