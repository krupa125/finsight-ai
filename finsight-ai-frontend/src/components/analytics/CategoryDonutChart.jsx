import {
  ResponsiveContainer,
  PieChart,
  Pie,
  Cell,
  Tooltip,
  Legend,
} from "recharts";

const COLORS = [
  "#10b981",
  "#3b82f6",
  "#f59e0b",
  "#ef4444",
  "#8b5cf6",
  "#06b6d4",
];

export default function CategoryDonutChart({ categories }) {
  const data = Object.entries(categories || {}).map(
    ([name, value]) => ({
      name,
      value,
    })
  );

  return (
    <div className="bg-white/80 backdrop-blur-xl rounded-3xl shadow-xl border border-white p-8">

      <h2 className="text-2xl font-bold text-slate-800 mb-6">
        Category Breakdown
      </h2>

      {data.length === 0 ? (
        <div className="flex items-center justify-center h-72 text-slate-500">
          No category data available
        </div>
      ) : (
        <div className="h-72">

          <ResponsiveContainer width="100%" height="100%">

            <PieChart>

              <Pie
                data={data}
                dataKey="value"
                nameKey="name"
                innerRadius={65}
                outerRadius={100}
                paddingAngle={3}
                label
              >
                {data.map((entry, index) => (
                  <Cell
                    key={entry.name}
                    fill={COLORS[index % COLORS.length]}
                  />
                ))}
              </Pie>

              <Tooltip />

              <Legend />

            </PieChart>

          </ResponsiveContainer>

        </div>
      )}

    </div>
  );
}