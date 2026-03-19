import { useState } from "react";
import { createEmployee } from "../services/employeeService";
import { useNavigate } from "react-router-dom";

export default function CreateEmployee() {

    const navigate = useNavigate();

    const [form, setForm] = useState({
        firstName: "",
        lastName: "",
        internalId: ""
    });

    const handleChange = (e) => {
        setForm({
            ...form,
            [e.target.name]: e.target.value
        });
    };

    const handleSubmit = () => {
        if (!form.firstName || !form.lastName || !form.internalId) {
            alert("Todos los campos son obligatorios");
            return;
        }

        createEmployee(form)
            .then(() => {
                alert("Empleado creado");
                navigate("/employees");
            })
            .catch(() => alert("Error al crear"));
    };

    return (
        <div>
            <h1>Crear Empleado</h1>

            <input
                name="firstName"
                placeholder="Nombre"
                value={form.firstName}
                onChange={handleChange}
            />

            <br /><br />

            <input
                name="lastName"
                placeholder="Apellido"
                value={form.lastName}
                onChange={handleChange}
            />

            <br /><br />

            <input
                name="internalId"
                placeholder="Código"
                value={form.internalId}
                onChange={handleChange}
            />

            <br /><br />

            <button onClick={handleSubmit}>
                Guardar
            </button>

            <button onClick={() => navigate("/employees")}>
                Cancelar
            </button>
        </div>
    );
}