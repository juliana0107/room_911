import { useState } from "react";
import { validateAccess } from "../services/accessService";

export default function AccessControl() {

    const [code, setCode] = useState("");
    const [result, setResult] = useState(null);

    const handleValidate = () => {
        if (!code) return;

        validateAccess(code)
            .then(res => setResult(res.data.result))
            .catch(() => setResult("ERROR"));
    };

    const getColor = () => {
        if (result === "AUTHORIZED") return "#16a34a"; // verde
        if (result === "DENIED") return "#dc2626"; // rojo
        if (result === "NOT_REGISTERED") return "#6b7280"; // gris
        return "#111";
    };

    return (
        <div style={{
            display: "flex",
            justifyContent: "center",
            alignItems: "center",
            height: "80vh",
            flexDirection: "column"
        }}>
            <h1 style={{ marginBottom: "30px" }}>Control de Acceso</h1>

            <input
                type="text"
                placeholder="Escanea o ingresa código"
                value={code}
                onChange={(e) => setCode(e.target.value)}
                onKeyDown={(e) => e.key === "Enter" && handleValidate()}
                style={{
                    padding: "20px",
                    fontSize: "20px",
                    width: "300px",
                    borderRadius: "10px",
                    border: "1px solid #ccc",
                    textAlign: "center"
                }}
            />

            <button
                onClick={handleValidate}
                style={{
                    marginTop: "20px",
                    padding: "15px 30px",
                    fontSize: "18px",
                    borderRadius: "10px",
                    background: "#111",
                    color: "#fff",
                    border: "none",
                    cursor: "pointer"
                }}
            >
                Validar
            </button>

            {result && (
                <div style={{
                    marginTop: "40px",
                    padding: "30px",
                    borderRadius: "15px",
                    background: "#f9f9f9",
                    width: "300px",
                    textAlign: "center",
                    boxShadow: "0 5px 20px rgba(0,0,0,0.1)"
                }}>
                    <h2 style={{
                        color: getColor(),
                        fontSize: "32px"
                    }}>
                        {result}
                    </h2>
                </div>
            )}
        </div>
    );
}