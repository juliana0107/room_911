import api from "./api.js";

export const validateAccess = (internalId) => {
    return api.get(`/employees/validate?internalId=${internalId}`);
};

export const getStats = () => api.get("/access/stats");

export const getAttempts = (start, end) =>
    api.get(`/access/attempts?start=${start}&end=${end}`);

export const downloadReport = () =>
    api.get("/reports/basic", { responseType: "blob" });