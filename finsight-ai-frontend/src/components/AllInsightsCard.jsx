export default function AllInsightsCard() {
  return (
    <div className="bg-white rounded-3xl shadow-lg p-6 h-full">
      <h2 className="text-2xl font-bold mb-4">
        AI Insights
      </h2>

      <div className="space-y-4">
        <div className="bg-slate-100 p-4 rounded-xl">
          Food spending is 20% higher this month.
        </div>

        <div className="bg-slate-100 p-4 rounded-xl">
          Shopping expenses increased by ₹1,500.
        </div>

        <div className="bg-slate-100 p-4 rounded-xl">
          Savings rate improved by 18%.
        </div>
      </div>
    </div>
  );
}