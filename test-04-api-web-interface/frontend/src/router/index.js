import { createRouter, createWebHistory } from "vue-router";

import OperadorasView from "../views/OperadorasView.vue";
import OperadoraDetalheView from "../views/OperadoraDetalheView.vue";
import EstatisticasView from "../views/EstatisticasView.vue";

const routes = [
  {
    path: "/",
    redirect: "/operadoras",
  },
  {
    path: "/operadoras",
    name: "operadoras",
    component: OperadorasView,
  },
  {
    path: "/operadoras/:cnpj",
    name: "operadora-detalhe",
    component: OperadoraDetalheView,
    props: true,
  },
  { path: "/estatisticas", component: EstatisticasView },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
