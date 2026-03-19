import { useEffect, useState } from "react";
import { getStats } from "../services/accessService.js";
import Card from "../components/Card";

export default function Dashboard() {
    const [stats, setStats] = useState(null);

    useEffect(() => {
        getStats()
            .then((res) => {
                console.log("DATA:", res.data); // 👈 IMPORTANTE
                setStats({
                    total: res.data.TOTAL,
                    authorized: res.data.AUTHORIZED,
                    denied: res.data.DENIED,
                    notRegistered: res.data.NOT_REGISTERED
                });
            })
            .catch((err) => {
                console.error("ERROR:", err);
            });
    }, []);

    if (!stats) return <p>Cargando...</p>;

    return (
        <div style={{ padding: "20px" }}>
            <h1>Dashboard</h1>

            <div style={{
                display: "grid",
                gridTemplateColumns: "repeat(2, 1fr)",
                gap: "20px",
                marginTop: "20px"
            }}>

                <Card title="TOTAL" value={stats.total} color="#333" />
                <Card title="AUTHORIZED" value={stats.authorized} color="green" />
                <Card title="DENIED" value={stats.denied} color="red" />
                <Card title="NOT REGISTERED" value={stats.notRegistered} color="gray" />

            </div>
        </div>
    );
}