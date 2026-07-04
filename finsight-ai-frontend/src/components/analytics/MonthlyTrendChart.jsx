import {
  ResponsiveContainer,
  LineChart,
  Line,
  XAxis,
  YAxis,
  Tooltip,
  CartesianGrid,
} from "recharts";

export default function MonthlyTrendChart({ monthly }) {
  const data = [
    {
      month: "Current",
      Income: monthly?.income || 0,
      Expenses: monthly?.expenses || 0,
      Savings: monthly?.savings || 0,
    },
  ];

  return (
    <div className="bg-white/80 backdrop-blur-xl rounded-3xl shadow-xl border border-white p-8">

      <h2 className="text-2xl font-bold text-slate-800 mb-6">
        Monthly Trends
      </h2>

      <div className="h-80">

        <ResponsiveContainer width="100%" height="100%">

          <LineChart data={data}>

            <CartesianGrid strokeDasharray="3 3" />

            <XAxis dataKey="month" />

            <YAxis />

            <Tooltip />

            <Line
              type="monotone"
              dataKey="Income"
              stroke="#10b981"
              strokeWidth={3}
            />

            <Line
              type="monotone"
              dataKey="Expenses"
              stroke="#ef4444"
              strokeWidth={3}
            />

            <Line
              type="monotone"
              dataKey="Savings"
              stroke="#3b82f6"
              strokeWidth={3}
            />

          </LineChart>

        </ResponsiveContainer>

      </div>

    </div>
  );
}