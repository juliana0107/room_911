import { useEffect, useState } from "react";
import {
    getEmployees,
    deactivateEmployee,
    changeAccess
} from "../services/employeeService";
import { useNavigate } from "react-router-dom";

export default function Employees() {

    const [employees, setEmployees] = useState([]);
    const navigate = useNavigate();

    const loadEmployees = () => {
        getEmployees().then(res => setEmployees(res.data));
    };

    useEffect(() => {
        loadEmployees();
    }, []);

    return (
        <div>
            <h1>Empleados</h1>

            <button onClick={() => navigate("/employees/new")}>
                + Nuevo Empleado
            </button>

            <table border="1" cellPadding="10">
                <thead>
                <tr>
                    <th>Código</th>
                    <th>Nombre</th>
                    <th>Estado</th>
                    <th>Acceso</th>
                    <th>Acciones</th>
                </tr>
                </thead>

                <tbody>
                {employees.map(emp => (
                    <tr key={emp.id}>
                        <td>{emp.internalId}</td>
                        <td>{emp.firstName} {emp.lastName}</td>

                        <td>
                            {emp.active ? "Activo" : "Inactivo"}
                        </td>

                        <td>
                            {emp.accessEnabled ? "Permitido" : "Denegado"}
                        </td>

                        <td>
                            <button onClick={() => {
                                deactivateEmployee(emp.id)
                                    .then(loadEmployees);
                            }}>
                                Inactivar
                            </button>

                            <button onClick={() => {
                                changeAccess(emp.id, !emp.accessEnabled)
                                    .then(loadEmployees);
                            }}>
                                Toggle Acceso
                            </button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
}