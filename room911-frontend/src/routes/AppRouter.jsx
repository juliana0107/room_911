import { Routes, Route } from "react-router-dom";
import Dashboard from "../pages/Dashboard";
import Employees from "../pages/Employees";
import Departments from "../pages/Departments";
import AccessControl from "../pages/AccessControl";
import Reports from "../pages/Reports";
import CreateEmployee from "../pages/CreateEmployee";

export default function AppRouter() {
    return (
            <Routes>
                <Route path="/" element={<Dashboard />} />
                <Route path="/access" element={<AccessControl />} />
                <Route path="/employees" element={<Employees />} />                <Route path="/departments" element={<h1>Departamentos</h1>} />
                <Route path="/employees/new" element={<CreateEmployee />} />
                <Route path="/reports" element={<h1>Reportes</h1>} />
                <Route path="/company" element={<h1>Perfil Empresa</h1>} />
                <Route path="/reports" element={<Reports />} />
            </Routes>
    );
}

