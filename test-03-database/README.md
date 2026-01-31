# Teste 03 – Banco de Dados e Análise

Para esta etapa, utiliza-se os CSVs gerados anteriormente: consolidado_despesas.csv, operadoras_ativas.csv e despesas_agregadas.csv(Traga-os do projeto 2 para a pasta data aqui).
Sendo que essa pasta atualmente possui os arquivos para executar os comandos SQL.

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

