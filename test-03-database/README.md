# Teste 03 – Banco de Dados e Análise

Para esta etapa, utiliza-se os CSVs gerados anteriormente: consolidado_despesas.csv, operadoras_ativas.csv e despesas_agregadas.csv(traga-os do projeto 2 para a pasta data aqui(crie uma)).
Sendo que esse projeto possui schema.sql e a pasta queries onde se encontram os comandos SQL.

## Tabelas
Foram criadas três tabelas normalizadas no PostgreSQL:
 - operadoras: dados cadastrais das operadoras (CNPJ, razão social, modalidade, UF, etc.).
 - despesas_consolidadas: despesas detalhadas por operadora, trimestre e ano.
 - despesas_agregadas: métricas agregadas por operadora/UF (total, média trimestral e desvio padrão).

 OBS: Mantive alguns campos repetidos entre as tabelas, por estar importando os dados dos csvs. Isso serviu apenas para acelerar o processo no processo seletivo(mas tenho o conhecimento de que isso estar fora do adequado).

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

## Tratamento de inconsistências:
Valores inválidos são tratados antes do COPY ou aceitos como NULL quando permitido

### Tabela operadoras:
 - CNPJs ausentes → serão rejeitados, pois a coluna é PK.

### Tabelas consolidado_despesas:
 - CNPJs ausentes ou inválidos → rejeitados, pois têm FK com operadoras.

### Tabelas despesas_agregadas:
 - UF ausente → pode ser NULL, ou rejeitado se considerar obrigatório.

## Justificativas:
 - Manter-se a integridade referencial e precisão nos dados.
 - Rejeitar linhas inválidas evita inconsistências futuras em análises agregadas.
 - Conversão prévia no CSV minimiza erros durante a importação via COPY.

## Queries

### Maior crescimento
#### Justificativa
 - Operadoras sem dados em múltiplos trimestres são excluídas, evitando distorções.

### Despesas acima da média geral 
#### Justificativa
 - A abordagem com CTEs prioriza legibilidade e manutenção. Alternativas com subqueries correlacionadas foram evitadas por reduzir clareza sem ganho relevante de performance no volume esperado. 

## Iria aplicar com mais tempo: recursos de nuvem (GCP)

Os arquivos CSV gerados na etapa anterior são reutilizados nesta fase como fonte de ingestão do banco de dados.
Um container executado em Cloud Run é responsável por baixar os arquivos do bucket e executar os scripts SQL de criação de tabelas, carga (\copy) e análises no Cloud SQL (PostgreSQL).
Essa abordagem manteria os mesmos scripts SQL usados localmente, garantindo reprodutibilidade entre ambientes.