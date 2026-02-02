import { createRouter, createWebHistory } from "vue-router";

import OperadorasView from "../views/OperadorasView.vue";
import OperadoraDetalheView from "../views/OperadoraDetalheView.vue";

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
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
