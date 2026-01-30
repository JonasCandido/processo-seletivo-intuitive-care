# Teste 01 - Integração com API da ANS

Aplicação batch em Java (Spring Boot) para download, processamento e consolidação
de despesas de operadoras de saúde a partir da API de Dados Abertos da ANS.

Usei Spring Boot para acelerar a configuração, melhorar organização do código e facilitar testes e execução, sem abrir mão de simplicidade. A aplicação é um job batch, não um servidor web, e o uso do Spring se limita a injeção de dependências, configuração e cliente HTTP.

## Como rodar o projeto ##

Usei docker compose para facilitar para os avaliadores executar o software.
Configurei o Dockerfile para rodar os testes e montar a pasta downloads(onde se encontra os arquivos zip baixados) e temp(onde se encontra o consolidado_csv) localmente fora do container.

Em um terminal(usei Linux para o exemplo) na pasta ans-despesas, execute:
```
docker build -t ans-despesas .
docker run --rm \
  -v $(pwd)/downloads:/app/downloads \
  -v $(pwd)/temp:/app/temp \
  ans-despesas
```



## Observações do desenvolvimento e trade-offs

1. TESTE DE INTEGRAÇÃO COM API PÚBLICA<br>
1.1 - A identificação de trimestre e ano é realizada de forma flexível, suportando diferentes padrões históricos de nomenclatura. Arquivos com informações incompletas são considerados apenas quando o período pode ser inferido de forma segura a partir do caminho do arquivo; caso contrário, são ignorados para evitar inconsistências temporais.

1.2 - Optei por processar cada arquivo de cada ZIP individualmente, lendo linha a linha. Esse processamento incremental garante baixo uso de memória, maior resiliência a erros em arquivos individuais, e permite filtrar e normalizar os dados conforme aparecem, sem necessidade de carregar todos os arquivos na memória de uma só vez. Essa abordagem é escalável para arquivos grandes ou múltiplos ZIPs, que poderiam causar problemas de memória se processados de uma vez.

1.3 -

CNPJs duplicados com razões diferentes: Registrados no log; apenas a primeira razão social é usada no CSV. Evita decisões arriscadas sobre qual razão é “correta”.

Valores zerados ou negativos: Mantidos no CSV, mas alertados no log. Garante transparência sem mascarar problemas nos dados.

Trimestres com datas inconsistentes: Linhas inválidas são ignoradas e registradas no log. Protege a integridade temporal do CSV.

Ordenação e consolidação: Dados agrupados e ordenados por ano → trimestre → CNPJ usando TreeMap. Facilita análise e auditoria.

Sobre performance em 1.3: usei cache de CNPJs/razões socias -> Consultas à API(da ANS que usei para buscar CNPJs/razões socias) são armazenadas localmente, evitando múltiplos acessos e melhorando a performance.
(Nos meus testes locais na primeira vez o software roda em 2 minutos e 6 segundos e na segunda vez cae para 35 segundos)

OBS: Por causa de tempo, faltou aplicar testes para o Consolidator.java (que possui muitas funcionalidades decidindo priorizar as outras etapas do projeto e voltar aqui se houvesse tempo)