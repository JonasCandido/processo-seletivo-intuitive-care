import { createRouter, createWebHistory } from "vue-router"

const routes = [
  {
    path: "/",
    name: "operadoras",
    component: () => import("../views/OperadorasView.vue")
  },
  {
    path: "/operadoras/:cnpj",
    name: "operadora-detalhe",
    component: () => import("../views/OperadoraDetalheView.vue")
  }
]

export const router = createRouter({
  history: createWebHistory(),
  routes
})
