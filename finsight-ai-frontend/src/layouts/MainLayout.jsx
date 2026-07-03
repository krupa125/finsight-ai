import Sidebar from "../components/Sidebar";
import DashboardCard from "../components/DashboardCard";
import AllInsightsCard from "../components/AllInsightsCard";
import CategoryBreakdown from "../components/CategoryBreakdown";

export default function MainLayout({
  dashboardData,
  categoryData,
}) {
  return (
    <div className="flex min-h-screen bg-slate-100">
      <Sidebar />

      <main className="flex-1 p-8">
        <h1 className="text-5xl font-bold mb-8">
          FinSight AI Dashboard
        </h1>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
          <DashboardCard
  title="Total Income"
  amount={dashboardData?.income || 0}
  change="Live Data"
  gradient="bg-gradient-to-r from-green-500 to-emerald-600"
/>

<DashboardCard
  title="Total Expenses"
  amount={dashboardData?.expenses || 0}
  change="Live Data"
  gradient="bg-gradient-to-r from-red-500 to-rose-600"
/>

<DashboardCard
  title="Savings"
  amount={dashboardData?.savings || 0}
  change="Live Data"
  gradient="bg-gradient-to-r from-blue-500 to-indigo-600"
/>
        </div>

        <div className="mt-8">
          <AllInsightsCard />
        </div>
        <div className="mt-8">
  <CategoryBreakdown data={categoryData} />
</div>
      </main>
    </div>
  );
}