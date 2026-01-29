# Teste 01 - Integração com API da ANS

Aplicação batch em Java (Spring Boot) para download, processamento e consolidação
de despesas de operadoras de saúde a partir da API de Dados Abertos da ANS.

Usei Spring Boot para acelerar a configuração, melhorar organização do código e facilitar testes e execução, sem abrir mão de simplicidade. A aplicação é um job batch, não um servidor web, e o uso do Spring se limita a injeção de dependências, configuração e cliente HTTP.

## Observações

1. TESTE DE INTEGRAÇÃO COM API PÚBLICA<br>
1.1 - A identificação de trimestre e ano é realizada de forma flexível, suportando diferentes padrões históricos de nomenclatura. Arquivos com informações incompletas são considerados apenas quando o período pode ser inferido de forma segura a partir do caminho do arquivo; caso contrário, são ignorados para evitar inconsistências temporais.

1.2 - Optei por processar cada arquivo de cada ZIP individualmente, lendo linha a linha. Esse processamento incremental garante baixo uso de memória, maior resiliência a erros em arquivos individuais, e permite filtrar e normalizar os dados conforme aparecem, sem necessidade de carregar todos os arquivos na memória de uma só vez. Essa abordagem é escalável para arquivos grandes ou múltiplos ZIPs, que poderiam causar problemas de memória se processados de uma vez.
