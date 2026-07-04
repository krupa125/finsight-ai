import { FaBell, FaSearch, FaUserCircle } from "react-icons/fa";

export default function Navbar() {
  return (
    <header className="bg-white shadow-md rounded-xl px-6 py-4 flex items-center justify-between mb-8">
      <h2 className="text-2xl font-bold text-slate-800">
        FinSight AI
      </h2>

      <div className="flex items-center gap-6">
        <div className="flex items-center bg-slate-100 px-4 py-2 rounded-lg">
          <FaSearch className="text-slate-500 mr-2" />
          <input
            type="text"
            placeholder="Search..."
            className="bg-transparent outline-none"
          />
        </div>

        <FaBell className="text-2xl text-slate-600 cursor-pointer hover:text-cyan-600" />

        <FaUserCircle className="text-4xl text-cyan-600 cursor-pointer" />
      </div>
    </header>
  );
}