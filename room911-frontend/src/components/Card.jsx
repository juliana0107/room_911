export default function Card({ title, value, color }) {
    return (
        <div style={{
            background: "#fff",
            borderRadius: "12px",
            padding: "20px",
            boxShadow: "0 4px 10px rgba(0,0,0,0.1)",
            textAlign: "center",
            borderTop: `5px solid ${color}`
        }}>
            <h3>{title}</h3>
            <h1 style={{ color }}>{value}</h1>
        </div>
    );
}