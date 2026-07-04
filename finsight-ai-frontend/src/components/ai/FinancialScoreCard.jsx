import { motion } from "framer-motion";

export default function FinancialScoreCard({ dashboard }) {
  const income = dashboard?.income || 0;
  const savings = dashboard?.savings || 0;

  const score =
    income === 0
      ? 0
      : Math.min(
          100,
          Math.round((savings / income) * 100)
        );

  const color =
    score >= 80
      ? "text-green-600"
      : score >= 60
      ? "text-blue-600"
      : score >= 40
      ? "text-yellow-500"
      : "text-red-500";

  return (
    <motion.div
      whileHover={{ scale: 1.02 }}
      className="bg-white/80 backdrop-blur-xl rounded-3xl shadow-xl border border-white p-8"
    >
      <p className="text-slate-500 uppercase tracking-wider">
        Financial Score
      </p>

      <h1 className={`text-6xl font-bold mt-5 ${color}`}>
        {score}
      </h1>

      <p className="text-slate-500 mt-3">
        out of 100
      </p>
    </motion.div>
  );
}