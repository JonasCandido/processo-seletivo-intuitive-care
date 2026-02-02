# Teste 04 – API e Interface Web

Este projeto implementa o backend , consistindo em uma API REST desenvolvida em Python com FastAPI, responsável por expor dados de operadoras de saúde e suas despesas, bem como estatísticas agregadas, consumidas por uma interface web (Vue.js).

## Como rodar o projeto ##

Usei docker/docker compose para facilitar para os avaliadores executar o software.
Configurei o docker-compose.yml para rodar os testes(me refiro aos testes em Python feitos na pasta backend/tests).

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

## Iria aplicar com mais tempo: recursos de nuvem (GCP)

Os arquivos CSV gerados nas etapas anteriores seriam reutilizados como fonte de ingestão do banco de dados.

Em um cenário em nuvem, o banco de dados seria hospedado no Cloud SQL (PostgreSQL).  
A API (test-04) seria executada como um container independente no Cloud Run, conectando-se ao Cloud SQL.

Os mesmos scripts SQL utilizados localmente (schema.sql, load.sql, e fk_after.sql) poderiam ser executados em um ambiente de inicialização ou job específico.
