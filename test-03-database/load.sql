COPY operadoras(
    registro_operadora, cnpj, razao_social, nome_fantasia, modalidade,
    logradouro, numero, complemento, bairro, cidade, uf, cep, ddd,
    telefone, fax, endereco_eletronico, representante,
    cargo_representante, regiao_de_comercializacao, data_registro_ans
)
FROM '/data/operadoras_ativas.csv'
DELIMITER ';'
CSV HEADER
ENCODING 'UTF8';


COPY despesas_consolidadas(
    cnpj, razao_social, trimestre, ano, valor_despesas
)
FROM '/data/consolidado_validado.csv'
DELIMITER ';'
CSV HEADER
ENCODING 'UTF8';


COPY despesas_agregadas(
    razao_social, uf, total_despesas, media_trimestral, desvio_padrao
)
FROM '/data/despesas_agregadas.csv'
DELIMITER ';'
CSV HEADER
ENCODING 'UTF8';
