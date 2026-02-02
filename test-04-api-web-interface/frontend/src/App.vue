<template>
  <div class="app">
    <header class="header">
      <div class="header-content">
        <h1>Operadoras de Saúde</h1>

        <nav>
          <router-link to="/operadoras">Operadoras</router-link>
        </nav>
      </div>
    </header>

    <main class="container">
      <router-view />
    </main>
  </div>
</template>

<script setup>
</script>

<style scoped>
/* ===== Reset básico ===== */
* {
  box-sizing: border-box;
}

body {
  margin: 0;
}

/* ===== Layout geral ===== */
.app {
  min-height: 100vh;
  background: #f8fafc;
  color: #0f172a;
  font-family: Inter, system-ui, Avenir, Helvetica, Arial, sans-serif;
}

.header {
  background: linear-gradient(90deg, #1e293b, #0f172a);
  color: white;
  padding: 16px 0;
}

.header-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 16px;

  display: flex;
  align-items: center;
  justify-content: space-between;
}

.header h1 {
  margin: 0;
  font-size: 1.4rem;
}

nav a {
  color: #e5e7eb;
  text-decoration: none;
  margin-left: 16px;
  font-weight: 500;
}

nav a:hover {
  color: #93c5fd;
}

nav a.router-link-active {
  color: #60a5fa;
  border-bottom: 2px solid #60a5fa;
  padding-bottom: 2px;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 24px 16px;
}

@media (max-width: 768px) {
  .header-content {
    flex-direction: column;
    align-items: flex-start;
    gap: 8px;
  }

  nav a {
    margin-left: 0;
    margin-right: 12px;
  }
}
</style>
