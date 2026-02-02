import api from "./client";

export async function listarOperadoras({ page = 1, limit = 10, search = "" } = {}) {
  const params = { page, limit };

  if (search) {
    params.search = search;
  }

  const response = await api.get("/api/operadoras", { params });
  return response.data;
}
