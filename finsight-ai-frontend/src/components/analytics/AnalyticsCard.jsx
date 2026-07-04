import { motion } from "framer-motion";

export default function AnalyticsCard({
  title,
  value,
  color,
  subtitle,
}) {
  return (
    <motion.div
      whileHover={{
        y: -6,
        scale: 1.02,
      }}
      transition={{
        duration: 0.25,
      }}
      className="bg-white/80 backdrop-blur-xl rounded-3xl shadow-xl border border-white p-8"
    >
      <p className="text-slate-500 text-sm font-medium">
        {title}
      </p>

      <h2 className={`text-4xl font-bold mt-3 ${color}`}>
        {value}
      </h2>

      <p className="text-slate-500 mt-3">
        {subtitle}
      </p>
    </motion.div>
  );
}