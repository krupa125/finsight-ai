export default function TransactionTable({ transactions }) {
  return (
    <div className="bg-white rounded-3xl shadow-lg p-6 mt-8">
      <h2 className="text-2xl font-bold mb-5">
        Recent Transactions
      </h2>

      <div className="overflow-x-auto">
        <table className="w-full">

          <thead className="bg-slate-100">
            <tr>
              <th className="text-left p-3">Type</th>
              <th className="text-left p-3">Amount</th>
              <th className="text-left p-3">Category</th>
              <th className="text-left p-3">Description</th>
              <th className="text-left p-3">Status</th>
            </tr>
          </thead>

          <tbody>
            {transactions?.content?.map((tx) => (
              <tr
                key={tx.id}
                className="border-b hover:bg-slate-50"
              >
                <td className="p-3">
                  {tx.type}
                </td>

                <td className="p-3">
                  ₹{tx.amount}
                </td>

                <td className="p-3">
                  {tx.category}
                </td>

                <td className="p-3">
                  {tx.description}
                </td>

                <td className="p-3">
                  {tx.status}
                </td>
              </tr>
            ))}
          </tbody>

        </table>
      </div>
    </div>
  );
}