import {
  PieChart,
  Pie,
  Cell,
  Tooltip,
  Legend,
  ResponsiveContainer,
} from "recharts";

const data = [
  { name: "Food", value: 5000 },
  { name: "Shopping", value: 3000 },
  { name: "Transport", value: 2000 },
  { name: "Bills", value: 4000 },
];

const COLORS = [
  "#10b981",
  "#3b82f6",
  "#f59e0b",
  "#ef4444",
];

export default function SpendingPieChart() {
  return (
    <div className="bg-white rounded-3xl shadow-lg p-6">
      <h2 className="text-2xl font-bold mb-4">
        Spending Breakdown
      </h2>

      <div className="h-80">
        <ResponsiveContainer width="100%" height="100%">
          <PieChart>
            <Pie
              data={data}
              dataKey="value"
              outerRadius={100}
              label
            >
              {data.map((entry, index) => (
                <Cell
                  key={index}
                  fill={COLORS[index % COLORS.length]}
                />
              ))}
            </Pie>

            <Tooltip />
            <Legend />
          </PieChart>
        </ResponsiveContainer>
      </div>
    </div>
  );
}