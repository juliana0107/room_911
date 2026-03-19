import { BrowserRouter } from "react-router-dom";
import Sidebar from "./components/Sidebar.jsx";
import AppRouter from "./routes/AppRouter.jsx";

function App() {
    return (
            <div style={{ display: "flex" }}>
                <Sidebar />
                <div style={{ padding: "20px", width: "100%" }}>
                    <AppRouter />
                </div>
            </div>
    );
}

export default App;