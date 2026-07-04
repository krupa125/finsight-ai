import { motion } from "framer-motion";
import {
  FaLightbulb,
  FaArrowTrendUp,
  FaPiggyBank,
} from "react-icons/fa6";

export default function AIInsightCard({
  dashboard,
  categories,
}) {
  const income = dashboard?.income || 0;
  const expenses = dashboard?.expenses || 0;
  const savings = dashboard?.savings || 0;

  const savingsRate =
    income === 0
      ? 0
      : Math.round((savings / income) * 100);

  const biggestCategory =
    Object.entries(categories || {}).sort(
      (a, b) => b[1] - a[1]
    )[0];

  return (
    <motion.div
      initial={{ opacity: 0, y: 25 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ duration: 0.5 }}
      className="bg-white/80 backdrop-blur-xl rounded-3xl shadow-xl border border-white p-8"
    >
      <div className="flex items-center gap-3 mb-6">

        <FaLightbulb className="text-yellow-500 text-2xl" />

        <h2 className="text-2xl font-bold text-slate-800">
          AI Insights
        </h2>

      </div>

      <div className="space-y-5">

        <div className="flex gap-3 items-start">

          <FaArrowTrendUp className="text-green-600 mt-1" />

          <p className="text-slate-700">
            Your savings rate is{" "}
            <span className="font-bold">
              {savingsRate}%
            </span>.
          </p>

        </div>

        <div className="flex gap-3 items-start">

          <FaPiggyBank className="text-blue-600 mt-1" />

          <p className="text-slate-700">
            Highest spending category:
            <span className="font-bold">
              {" "}
              {biggestCategory
                ? biggestCategory[0]
                : "N/A"}
            </span>
          </p>

        </div>

        <div className="bg-blue-50 rounded-2xl p-5">

          <p className="text-blue-800 font-medium">
            💡 Recommendation
          </p>

          <p className="mt-2 text-slate-700">
            Try keeping your savings above
            <span className="font-bold">
              {" "}30%
            </span>{" "}
            of your monthly income.
          </p>

        </div>

      </div>
    </motion.div>
  );
}