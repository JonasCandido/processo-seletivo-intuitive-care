<script setup>
import { ref, onMounted } from "vue";
import api from "../api/client";

import {
  Chart,
  BarController,
  BarElement,
  CategoryScale,
  LinearScale,
  Tooltip,
  Legend,
} from "chart.js";

Chart.register(BarController, BarElement, CategoryScale, LinearScale, Tooltip, Legend);

const loading = ref(false);
const error = ref(null);
const estatisticas = ref(null);
const canvasRef = ref(null);
let chartInstance = null;

async function carregarEstatisticas() {
  loading.value = true;
  error.value = null;

  try {
    const res = await api.get("/api/estatisticas");
    estatisticas.value = res.data;
    montarGrafico(estatisticas.value.top_5_operadoras);
  } catch (err) {
    console.error(err);
    error.value = "Erro ao carregar estatísticas.";
  } finally {
    loading.value = false;
  }
}

function montarGrafico(dados) {
  if (!canvasRef.value) {
    console.log("Canvas ainda não está disponível!");
    return;
  }

  if (chartInstance) chartInstance.destroy();

  const labels = dados.map(d => d.uf || "N/A");
  const valores = dados.map(d => Number(d.total_despesas));

  chartInstance = new Chart(canvasRef.value, {
    type: "bar",
    data: {
      labels,
      datasets: [{
        label: "Total de Despesas por UF (Top 5)",
        data: valores,
        backgroundColor: ["#3b82f6","#10b981","#f59e0b","#ef4444","#8b5cf6"],
        borderRadius: 6,
      }]
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      scales: {
        y: {
          beginAtZero: true,
          ticks: {
            callback: val => "R$ " + Number(val).toLocaleString("pt-BR", { minimumFractionDigits: 0 })
          }
        }
      },
      plugins: {
        tooltip: {
          callbacks: {
            label: ctx => `R$ ${ctx.raw.toLocaleString("pt-BR", { minimumFractionDigits: 2 })}`
          }
        },
        legend: { display: false }
      }
    }
  });
}

onMounted(() => {
  carregarEstatisticas();
});
</script>

<template>
  <div class="grafico-container">
    <h1>Distribuição de Despesas por UF</h1>

    <p v-if="loading" class="loading">Carregando gráfico...</p>
    <p v-if="error" class="error">{{ error }}</p>

    <div class="chart-wrapper">
      <!-- canvas SEM v-if, SEM v-show depende de data, sempre existe -->
      <canvas ref="canvasRef"></canvas>
    </div>
  </div>
</template>

<style scoped>
.grafico-container {
  max-width: 900px;
  margin: 0 auto;
  padding: 24px;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.05);
}

h1 { text-align:center; margin-bottom:16px; }
.loading { text-align:center; color:#555; }
.error { text-align:center; color:#dc2626; font-weight:600; }

/* tamanho definido */
.chart-wrapper {
  width: 100%;
  height: 450px;
  position: relative;
}

canvas {
  width: 100% !important;
  height: 100% !important;
}
</style>
