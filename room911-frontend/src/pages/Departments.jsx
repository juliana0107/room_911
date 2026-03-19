
import { useEffect, useState } from "react";
import {
    getDepartments,
    createDepartment,
} from "../services/departmentService.js";

export default function Departments() {
    const [deps, setDeps] = useState([]);
    const [name, setName] = useState("");

    const load = async () => {
        const res = await getDepartments();
        setDeps(res.data);
    };

    useEffect(() => {
        load();
    }, []);

    const handleCreate = async () => {
        await createDepartment({ name });
        load();
    };

    return (
        <div>
            <h1>Departamentos</h1>

            <input onChange={(e) => setName(e.target.value)} />
            <button onClick={handleCreate}>Crear</button>

            <ul>
                {deps.map((d) => (
                    <li key={d.id}>{d.name}</li>
                ))}
            </ul>
        </div>
    );
}