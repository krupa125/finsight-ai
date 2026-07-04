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
      whileHover={{
        y: -8,
        scale: 1.03,
      }}
      transition={{
        duration: 0.25,
      }}
      className="relative overflow-hidden rounded-3xl bg-white/80 backdrop-blur-xl border border-white shadow-xl p-6"
    >
      <div
        className={`absolute top-0 left-0 w-full h-1 ${color}`}
      />

      <div className="flex justify-between items-center">

        <div>

          <p className="text-slate-500 text-sm font-medium">
            {title}
          </p>

          <h2 className="text-4xl font-extrabold mt-3 text-slate-800">
            ₹ {Number(value).toLocaleString()}
          </h2>

          <p className="mt-3 text-green-600 font-semibold">
            {change} this month
          </p>

        </div>

        <div
          className={`${color} w-16 h-16 rounded-2xl flex items-center justify-center text-white text-2xl shadow-lg`}
        >
          {icon}
        </div>

      </div>

    </motion.div>
  );
}