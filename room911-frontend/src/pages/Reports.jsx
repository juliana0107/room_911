import { downloadReport } from "../services/accessService.js";

export default function Reports() {
    const handleDownload = async () => {
        const res = await downloadReport();
        const url = window.URL.createObjectURL(new Blob([res.data]));

        const link = document.createElement("a");
        link.href = url;
        link.setAttribute("download", "reporte.pdf");
        document.body.appendChild(link);
        link.click();
    };

    return (
        <div>
            <h1>Reportes</h1>
            <button onClick={handleDownload}>
                Descargar PDF
            </button>
        </div>
    );
}