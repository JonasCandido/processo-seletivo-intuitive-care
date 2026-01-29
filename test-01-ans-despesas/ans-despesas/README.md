# Teste 01 - Integração com API da ANS

Aplicação batch em Java (Spring Boot) para download, processamento e consolidação
de despesas de operadoras de saúde a partir da API de Dados Abertos da ANS.

Usei Spring Boot para acelerar a configuração, melhorar organização do código e facilitar testes e execução, sem abrir mão de simplicidade. A aplicação é um job batch, não um servidor web, e o uso do Spring se limita a injeção de dependências, configuração e cliente HTTP.