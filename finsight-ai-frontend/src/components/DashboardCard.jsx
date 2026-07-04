export default function DashboardCard({
  title,
  value,
  icon,
  color,
}) {
  return (
    <div
      className="rounded-3xl shadow-xl text-white p-6 transition-all duration-300 hover:-translate-y-2 hover:shadow-2xl"
      style={{
        background: color,
      }}
    >
      <div className="flex justify-between items-start">

        <div>

          <p className="text-white/80 text-sm uppercase tracking-wider">
            {title}
          </p>

          <h2 className="text-4xl font-bold mt-4">
            ₹ {Number(value || 0).toLocaleString("en-IN")}
          </h2>

        </div>

        <div className="w-16 h-16 rounded-full bg-white/20 flex items-center justify-center text-3xl">
          {icon}
        </div>

      </div>

      <div className="mt-8 flex justify-between items-center">

        <span className="text-white/80 text-sm">
          Live Financial Data
        </span>

        <span className="font-semibold text-green-200">
          ● Live
        </span>

      </div>

    </div>
  );
}