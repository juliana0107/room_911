import { Link } from "react-router-dom";

function Navbar() {
    return (
        <nav style={{ padding: 10, background: "#eee" }}>
            <Link to="/">Dashboard</Link> |{" "}
            <Link to="/employees">Empleados</Link> |{" "}
            <Link to="/departments">Departamentos</Link> |{" "}
            <Link to="/reports">Reportes</Link>
        </nav>
    );
}

export default Navbar;