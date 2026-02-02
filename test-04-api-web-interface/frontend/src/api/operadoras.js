import api from "./client";

export async function listarOperadoras({ page = 1, limit = 10, search = "" }) {
  const params = { page, limit };

  if (search) params.search = search;

  const { data } = await api.get("/api/operadoras", { params });
  return data;
}

export async function obterOperadora(cnpj) {
  const response = await api.get(`/api/operadoras/${cnpj}`);
  return response.data;
}

export async function listarDespesasOperadora(cnpj) {
  const response = await api.get(`/api/operadoras/${cnpj}/despesas`);
  return response.data;
}
