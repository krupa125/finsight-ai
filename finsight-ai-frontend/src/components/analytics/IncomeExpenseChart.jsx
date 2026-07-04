import {
  ResponsiveContainer,
  AreaChart,
  Area,
  CartesianGrid,
  XAxis,
  YAxis,
  Tooltip,
} from "recharts";

export default function IncomeExpenseChart({ monthly }) {
  const data = [
    {
      name: "Income",
      value: monthly?.income || 0,
    },
    {
      name: "Expenses",
      value: monthly?.expenses || 0,
    },
    {
      name: "Savings",
      value: monthly?.savings || 0,
    },
  ];

  return (
    <div className="bg-white/80 backdrop-blur-xl rounded-3xl shadow-xl border border-white p-8">

      <h2 className="text-2xl font-bold text-slate-800 mb-6">
        Income vs Expenses
      </h2>

      <div className="h-72">

        <ResponsiveContainer width="100%" height="100%">

          <AreaChart data={data}>

            <CartesianGrid strokeDasharray="3 3" />

            <XAxis dataKey="name" />

            <YAxis />

            <Tooltip />

            <Area
              type="monotone"
              dataKey="value"
              stroke="#2563eb"
              fill="#93c5fd"
            />

          </AreaChart>

        </ResponsiveContainer>

      </div>

    </div>
  );
}