export default function DashboardCard({
  title,
  amount,
  change,
  gradient,
}) {
  return (
    <div
      className={`
        ${gradient}
        text-white
        rounded-3xl
        p-6
        shadow-lg
        transition-all
        duration-300
        hover:scale-105
        hover:shadow-2xl
      `}
    >
      <h3 className="text-lg font-semibold opacity-90">
        {title}
      </h3>

      <p className="text-4xl font-bold mt-4">
        ₹{amount}
      </p>

      <p className="mt-3 text-sm opacity-80">
        {change}
      </p>
    </div>
  );
}