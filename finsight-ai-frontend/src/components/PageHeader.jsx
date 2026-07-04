import { motion } from "framer-motion";

export default function PageHeader({ title, subtitle }) {
  return (
    <motion.div
      initial={{ opacity: 0, y: -20 }}
      animate={{ opacity: 1, y: 0 }}
      className="mb-8"
    >
      <h1 className="text-4xl font-bold text-slate-800">
        {title}
      </h1>

      <p className="text-slate-500 mt-2">
        {subtitle}
      </p>
    </motion.div>
  );
}