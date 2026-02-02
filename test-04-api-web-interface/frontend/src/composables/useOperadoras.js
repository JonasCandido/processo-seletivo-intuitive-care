import { ref, onMounted } from "vue";
import { listarOperadoras } from "../api/operadoras";

export function useOperadoras() {
  const operadoras = ref([]);
  const page = ref(1);
  const limit = ref(10);
  const total = ref(0);
  const loading = ref(false);
  const error = ref(null);

  async function fetchOperadoras() {
    loading.value = true;
    error.value = null;

    try {
      const response = await listarOperadoras({
        page: page.value,
        limit: limit.value,
      });

      operadoras.value = response.data;
      total.value = response.total;
    } catch (err) {
      error.value = "Erro ao carregar operadoras";
    } finally {
      loading.value = false;
    }
  }

  onMounted(fetchOperadoras);

  return {
    operadoras,
    page,
    limit,
    total,
    loading,
    error,
    fetchOperadoras,
  };
}