# Teste 03 – Banco de Dados e Análise

Para esta etapa, utiliza-se os CSVs gerados anteriormente: consolidado_despesas.csv, operadoras_ativas.csv e despesas_agregadas.csv.
Sendo que essa pasta atualmente possui os arquivos para executar os comandos SQL.

## Tabelas
Foram criadas três tabelas normalizadas no PostgreSQL:
 - operadoras: dados cadastrais das operadoras (CNPJ, razão social, modalidade, UF, etc.).
 - despesas_consolidadas: despesas detalhadas por operadora, trimestre e ano.
 - despesas_agregadas: métricas agregadas por operadora/UF (total, média trimestral e desvio padrão).

## Chaves e índices:
 - PK em operadoras(cnpj) e id nas tabelas de despesas.
 - FKs das tabelas de despesas referenciam operadoras(cnpj).
 - Índices adicionais em razao_social, uf, ano, trimestre e total_despesas para otimizar consultas analíticas.

## Trade-offs técnicos:
 - Normalização: escolhida para evitar duplicação de dados, reduzir inconsistências e facilitar atualizações. JOINs simples não impactam performance dado o volume esperado (~2–3 mil operadoras).

## Tipos de dados:
 - Valores monetários: DECIMAL(20,2) para preservar precisão.
 - Datas: DATE para operações nativas de data.
 - CNPJ: CHAR(14) para tamanho fixo e indexação eficiente.

Essa abordagem mantém os dados consistentes, evita redundância e garante consultas rápidas e confiáveis.