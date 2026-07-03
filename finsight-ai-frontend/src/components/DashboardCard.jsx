import { motion } from "framer-motion";

export default function DashboardCard({
  title,
  value,
  icon,
  color,
}) {
  return (
    <motion.div
      initial={{ opacity: 0, y: 25 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ duration: 0.5 }}
      whileHover={{
        scale: 1.03,
        transition: { duration: 0.2 },
      }}
      className="rounded-2xl p-6 shadow-lg text-white"
      style={{ background: color }}
    >
      <div className="flex justify-between items-center">
        <div>
          <p className="text-sm opacity-80">{title}</p>

          <h2 className="text-3xl font-bold mt-2">
            ₹{value?.toLocaleString()}
          </h2>
        </div>

        <div className="text-4xl">
          {icon}
        </div>
      </div>
    </motion.div>
  );
}