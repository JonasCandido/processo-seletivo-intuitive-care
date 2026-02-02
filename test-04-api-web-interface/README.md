# Teste 04 – API e Interface Web

Este projeto implementa o backend , consistindo em uma API REST desenvolvida em Python com FastAPI, responsável por expor dados de operadoras de saúde e suas despesas, bem como estatísticas agregadas, consumidas por uma interface web (Vue.js).

## Como rodar o projeto ##

Usei docker/docker compose para facilitar para os avaliadores executar o software.
Configurei o docker-compose.yml para rodar os testes(me refiro aos testes em Python feitos na pasta backend/tests e os testes do frontend com Vitest em frontend/tests).

Em um terminal(usei Linux para o exemplo) na pasta raiz de test-04-api-web-interface, execute:
```
docker compose up --build
```

API: http://localhost:8000<br>
Documentação: http://localhost:8000/docs<br>
Frontend: http://localhost:5173/

## Observações do desenvolvimento e trade-offs

4 – API e Interface Web

4.2

4.2.1 - Escolha: FastAPI

## Justificativas:
 - Alto desempenho
 - Tipagem forte com Pydantic
 - Geração automática de OpenAPI
 - Excelente suporte a testes e manutenção

Para um projeto orientado a API, com contratos bem definidos e consumo por frontend moderno, o FastAPI se mostrou a opção mais adequada.

4.2.2 - Escolha: Offset-based pagination

## Justificativas:
 - Volume de dados moderado
 - Dados com baixa taxa de atualização
 - Implementação simples e clara
 - Fácil consumo no frontend (Vue.js)
 - Estratégias como cursor ou keyset seriam mais adequadas para streams ou grandes volumes altamente mutáveis, o que não é o caso deste domínio.

4.2.3 - Escolha: Pré-calcular e armazenar em tabela (despesas_agregadas)

## Justificativas:
 - Estatísticas custosas para calcular em tempo real
 - Dados atualizados em batch (Teste 01 / Teste 02)
 - Consistência previsível
 - Resposta rápida da API

Essa abordagem reduz carga no banco e simplifica a lógica da rota.

4.2.4 - Escolha: Dados + metadados

{
  "data": [...],
  "total": 100,
  "page": 1,
  "limit": 10
}

## Justificativas:
 - Facilita paginação no frontend
 - Evita cálculos adicionais no cliente
 - Contrato mais explícito e autoexplicativo

4.3 – Interface Vue.js

4.3.1 – Escolha: Busca no servidor

## Justificativa: 
 - Evita sobrecarregar o cliente com todos os dados; UX mais rápida mesmo com muitas operadoras.


4.3.2 - Escolha: Composables (Vue 3)
## Justificativa: 
 - Permite isolar e reutilizar lógica, adequada para o tamanho da aplicação.

4.3.3 - Escolha: Paginação implementada via backend, carregando apenas a página solicitada

## Justificativa: 
 - renderização leve e responsiva mesmo para centenas ou milhares de operadoras; evita travar o DOM.

4.3.4 - Escolhas:
 - Loader visível durante carregamento
 - Mensagens de erro específicas
 - Mensagens claras quando não há dados

## Justificativa: 
 - melhora UX e facilita debug


4.4 – Documentação
A documentação da API está disponível no backend: http://localhost:8000/docs
Inclui todos os endpoints com exemplos de requisição e resposta, por isso não achei necessário inserir Postman.

## Iria aplicar com mais tempo: recursos de nuvem (GCP) e testes para o EstatisticasView.vue(estava dando erros devido a forma que a view estava renderizando o Chart e não tive tempo para consertá-lo)

Os arquivos CSV gerados nas etapas anteriores seriam reutilizados como fonte de ingestão do banco de dados.

Em um cenário em nuvem, o banco de dados seria hospedado no Cloud SQL (PostgreSQL).  
A API e Frontend (test-04) seria executada como um container independente no Cloud Run, conectando-se ao Cloud SQL.

Os mesmos scripts SQL utilizados localmente (schema.sql, load.sql, e fk_after.sql) poderiam ser executados em um ambiente de inicialização ou job específico.
