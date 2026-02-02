<script setup>
import { ref, onMounted, watch } from "vue";
import { listarOperadoras } from "../api/operadoras";

const operadoras = ref([]);
const page = ref(1);
const limit = ref(10);
const total = ref(0);
const search = ref("");
const loading = ref(false);

async function carregar() {
  loading.value = true;

  const res = await listarOperadoras({
    page: page.value,
    limit: limit.value,
    search: search.value,
  });

  operadoras.value = res.data;
  total.value = res.total;

  loading.value = false;
}

watch([page, search], carregar);
onMounted(carregar);
</script>

<template>
  <div class="container">
    <h1>Operadoras</h1>

    <input
      v-model="search"
      class="search"
      placeholder="Buscar por CNPJ ou Razão Social"
    />

    <p v-if="loading" class="loading">Carregando...</p>

    <div v-else class="table-wrapper">
      <table class="table">
        <thead>
          <tr>
            <th>CNPJ</th>
            <th>Razão Social</th>
            <th>Nome Fantasia</th>
            <th>Modalidade</th>
            <th>UF</th>
            <th>Registro ANS</th>
          </tr>
        </thead>

        <tbody>
          <tr
            v-for="op in operadoras"
            :key="op.cnpj"
            @click="$router.push(`/operadoras/${op.cnpj}`)"
            class="clickable"
          >
            <td>{{ op.cnpj }}</td>
            <td class="bold">{{ op.razao_social }}</td>
            <td>{{ op.nome_fantasia || "-" }}</td>
            <td>{{ op.modalidade || "-" }}</td>
            <td>{{ op.uf || "-" }}</td>
            <td>
              {{
                op.data_registro_ans
                  ? new Date(op.data_registro_ans).toLocaleDateString()
                  : "-"
              }}
            </td>
          </tr>
        </tbody>
      </table>

      <p v-if="operadoras.length === 0" class="empty">
        Nenhuma operadora encontrada
      </p>
    </div>

    <div class="pagination">
      <button :disabled="page === 1" @click="page--">Anterior</button>
      <span>Página {{ page }}</span>
      <button :disabled="page * limit >= total" @click="page++">
        Próxima
      </button>
    </div>
  </div>
</template>

<style scoped>
.container {
  max-width: 1400px;
  margin: 0 auto;
  padding: 24px;
  font-family: system-ui, -apple-system, BlinkMacSystemFont, sans-serif;
}

h1 {
  margin-bottom: 16px;
}

.search {
  width: 100%;
  max-width: 420px;
  padding: 10px 14px;
  margin-bottom: 16px;
  border-radius: 8px;
  border: 1px solid #cbd5e1;
  font-size: 0.95rem;
}

.search:focus {
  outline: none;
  border-color: #2563eb;
}

.loading {
  padding: 16px;
}

.table-wrapper {
  overflow-x: auto;
  background: #ffffff;
  border-radius: 12px;
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.06);
}

.table {
  width: 100%;
  border-collapse: collapse;
  min-width: 1100px;
}

.table thead th {
  background: #f1f5f9;
  text-align: left;
  padding: 14px;
  font-size: 0.85rem;
  font-weight: 600;
  color: #334155;
  border-bottom: 2px solid #e2e8f0;
}
.table tbody td {
  padding: 14px;
  font-size: 0.9rem;
  border-bottom: 1px solid #e5e7eb;
  white-space: nowrap;
}

.clickable {
  cursor: pointer;
}

.table tbody tr:nth-child(even) {
  background: #f8fafc;
}

.table tbody tr:hover {
  background: #e0f2fe;
}

.bold {
  font-weight: 600;
}

.empty {
  padding: 16px;
  color: #64748b;
}

.pagination {
  margin-top: 16px;
  display: flex;
  gap: 12px;
  align-items: center;
}

.pagination button {
  padding: 8px 14px;
  border-radius: 8px;
  border: 1px solid #cbd5e1;
  background: #ffffff;
  cursor: pointer;
}

.pagination button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

@media (max-width: 768px) {
  .container {
    padding: 16px;
  }
}
</style>
