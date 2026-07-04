import { motion } from "framer-motion";

export default function PremiumCard({
  title,
  value,
  icon,
  color,
  change,
}) {
  return (
    <motion.div
      whileHover={{ y: -6, scale: 1.02 }}
      transition={{ duration: 0.25 }}
      className="bg-white rounded-3xl p-6 shadow-lg border border-slate-200"
    >
      <div className="flex justify-between items-center">
        <div>
          <p className="text-slate-500 text-sm">{title}</p>

          <h2 className="text-3xl font-bold mt-2">
            ₹ {Number(value).toLocaleString()}
          </h2>

          <p className="text-green-600 mt-3 font-semibold text-sm">
            ▲ {change}
          </p>
        </div>

        <div
          className={`w-14 h-14 rounded-2xl flex items-center justify-center text-white ${color}`}
        >
          {icon}
        </div>
      </div>
    </motion.div>
  );
}