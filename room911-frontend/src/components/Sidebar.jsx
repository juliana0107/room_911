import { Link, useLocation } from "react-router-dom";

export default function Sidebar() {

    const location = useLocation();

    const menu = [
        { name: "Dashboard", path: "/" },
        { name: "Control de Acceso", path: "/access" },
        { name: "Empleados", path: "/employees" },
        { name: "Departamentos", path: "/departments" },
        { name: "Reportes", path: "/reports" },
        { name: "Perfil Empresa", path: "/company" }
    ];

    return (
        <div style={{
            width: "250px",
            height: "100vh",
            background: "#111",
            color: "#fff",
            padding: "20px"
        }}>
            <h2 style={{ marginBottom: "30px" }}>ROOM_911</h2>

            {menu.map((item) => (
                <Link
                    key={item.path}
                    to={item.path}
                    style={{
                        display: "block",
                        padding: "12px",
                        marginBottom: "10px",
                        borderRadius: "8px",
                        textDecoration: "none",
                        color: "#fff",
                        background:
                            location.pathname === item.path
                                ? "#2563eb"
                                : "transparent"
                    }}
                >
                    {item.name}
                </Link>
            ))}
        </div>
    );
}