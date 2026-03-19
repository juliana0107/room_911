import api from "../api/axios";

export const getEmployees = () => api.get("/employees");

export const createEmployee = (data) => api.post("/employees", data);

export const updateEmployee = (id, data) =>
    api.put(`/employees/${id}`, data);

export const deactivateEmployee = (id) =>
    api.put(`/employees/${id}/deactivate`);

export const changeAccess = (id, enabled) =>
    api.put(`/employees/${id}/access?enabled=${enabled}`);

export const assignDepartment = (id, departmentId) =>
    api.put(`/employees/${id}/department/${departmentId}`);