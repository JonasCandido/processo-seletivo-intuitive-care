# Teste 02 – Transformação e Validação de Dados

Este projeto realiza validação, enriquecimento e agregação de dados a partir do CSV consolidado gerado no Teste 01.

Utilizei python para acelerar o desenvolvimento dessa etapa para o processo seletivo.

## Observação
Este projeto consome como entrada o arquivo consolidado_despesas.csv gerado no Teste 01, mova-o ele para a pasta data de test-02-data-transform.

2 – TESTE DE TRANSFORMAÇÃO E VALIDAÇÃO DE DADOS

2.1 - Validação de Dados com Estratégias Diferentes

2.1.1 - Para CNPJs inválidos, optei por descartar os registros. Essa abordagem garante integridade referencial para as etapas posteriores de enriquecimento e agregação, evitando joins incorretos ou associações erradas com dados cadastrais.

Prós:
- Garante consistência dos dados
- Evita resultados incorretos em joins e agregações
- Simplifica análises posteriores

Contras:
- Pode descartar registros potencialmente válidos com erro de origem
- Perda de volume de dados em cenários com baixa qualidade de entrada

Essa abordagem prioriza qualidade e consistência dos dados finais, alinhada ao objetivo analítico do projeto.

2.1.2 - Para Validação de Valores Numéricos Positivos:

- Os valores de despesas são convertidos para formato numérico considerando o padrão brasileiro (separadores de milhar e decimal).
- Registros com valores zero ou negativos são descartados e contabilizados em log.

Prós
- Evita distorções em somatórios, médias e desvios padrão
- Garante coerência semântica do dado de despesa

Contras
- Pode descartar registros que representem ajustes ou estornos legítimos
- Assume que o dataset final deve conter apenas despesas efetivas

2.1.3 - Validação de Razão Social Não Vazia

Registros com Razão Social nula, vazia ou contendo apenas espaços são descartados e registrados em log.

Justificativa: a Razão Social é um identificador essencial para análises, agrupamentos e interpretação dos resultados. Manter registros sem esse campo comprometeria a rastreabilidade e a legibilidade dos dados.

Prós
- Garante dados semanticamente completos
- Evita agrupamentos inválidos ou linhas sem identificação

Contras
- Pode eliminar registros onde a informação poderia ser recuperada externamente
- Assume que a ausência da Razão Social é um erro irrecuperável no pipeline atual

