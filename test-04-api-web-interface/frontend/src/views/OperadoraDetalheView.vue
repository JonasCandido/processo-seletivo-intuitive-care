<script setup>
import { ref, onMounted } from "vue";
import { useRoute } from "vue-router";
import api from "../api/client";

const route = useRoute();
const cnpj = route.params.cnpj;

const operadora = ref(null);
const despesas = ref([]);

const loadingOperadora = ref(true);
const loadingDespesas = ref(true);
const erro = ref(null);

async function carregarOperadora() {
  try {
    const res = await api.get(`/api/operadoras/${cnpj}`);
    operadora.value = res.data;
  } catch (e) {
    erro.value = "Operadora não encontrada.";
  } finally {
    loadingOperadora.value = false;
  }
}

async function carregarDespesas() {
  try {
    const res = await api.get(`/api/operadoras/${cnpj}/despesas`);
    despesas.value = res.data;
  } catch (e) {
    despesas.value = [];
  } finally {
    loadingDespesas.value = false;
  }
}

onMounted(() => {
  carregarOperadora();
  carregarDespesas();
});
</script>

<template>
  <div class="container">
    <p v-if="erro" class="error">{{ erro }}</p>
    <p v-else-if="loadingOperadora">Carregando operadora...</p>

    <!-- DADOS DA OPERADORA -->
    <div v-else class="card">
      <h1>{{ operadora.razao_social }}</h1>

      <div class="info-grid">
        <div><strong>CNPJ:</strong> {{ operadora.cnpj }}</div>
        <div><strong>UF:</strong> {{ operadora.uf || "-" }}</div>
        <div><strong>Modalidade:</strong> {{ operadora.modalidade || "-" }}</div>
        <div><strong>Nome Fantasia:</strong> {{ operadora.nome_fantasia || "-" }}</div>
        <div>
          <strong>Registro ANS:</strong>
          {{ operadora.data_registro_ans || "-" }}
        </div>
      </div>
    </div>

    <!-- DESPESAS -->
    <div class="card">
      <h2>Histórico de Despesas</h2>

      <p v-if="loadingDespesas">Carregando despesas...</p>

      <p v-else-if="despesas.length === 0" class="empty">
        Esta operadora não possui despesas registradas.
      </p>

      <table v-else>
        <thead>
          <tr>
            <th>Ano</th>
            <th>Trimestre</th>
            <th>Valor (R$)</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="d in despesas" :key="`${d.ano}-${d.trimestre}`">
            <td>{{ d.ano }}</td>
            <td>{{ d.trimestre }}</td>
            <td class="money">
              {{ Number(d.valor_despesas).toLocaleString("pt-BR", {
                style: "currency",
                currency: "BRL"
              }) }}
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<style scoped>
.container {
  max-width: 1100px;
  margin: 0 auto;
  padding: 24px;
}

.card {
  background: #ffffff;
  border-radius: 10px;
  padding: 20px;
  margin-bottom: 24px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

h1 {
  margin-bottom: 16px;
  color: #111827;
}

h2 {
  margin-bottom: 12px;
  color: #1f2937;
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
  gap: 12px;
  font-size: 14px;
  color: #374151;
}

table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 12px;
}

th,
td {
  padding: 12px 16px;
  border-bottom: 1px solid #e5e7eb;
  text-align: center;
}

th {
  background-color: #f3f4f6;
  font-weight: 600;
}

tbody tr:hover {
  background-color: #f9fafb;
}

.money {
  text-align: right;
  font-weight: 500;
}

.empty {
  color: #6b7280;
  font-style: italic;
}

.error {
  color: #dc2626;
  font-weight: 600;
}
</style>
