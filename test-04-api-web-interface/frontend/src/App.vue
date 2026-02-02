<script setup>
import { onMounted, ref } from "vue";
import { listarOperadoras } from "./api/operadoras";

const operadoras = ref([]);
const loading = ref(false);
const error = ref(null);

onMounted(async () => {
  loading.value = true;
  try {
    const data = await listarOperadoras({ page: 1, limit: 5 });
    operadoras.value = data.data ?? data;
  } catch (err) {
    error.value = err.message || "Erro ao carregar operadoras";
  } finally {
    loading.value = false;
  }
});
</script>

<template>
  <div style="padding: 20px">
    <h1>Operadoras</h1>

    <p v-if="loading">Carregando...</p>
    <p v-if="error" style="color: red">{{ error }}</p>

    <ul>
      <li v-for="op in operadoras" :key="op.cnpj">
        {{ op.razao_social }} — {{ op.cnpj }}
      </li>
    </ul>
  </div>
</template>
