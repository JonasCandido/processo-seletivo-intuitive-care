CREATE TABLE operadoras (
    registro_operadora INT UNIQUE NOT NULL,
    cnpj CHAR(14) PRIMARY KEY,
    razao_social VARCHAR(255) NOT NULL,
    nome_fantasia VARCHAR(255),
    modalidade VARCHAR(100),
    logradouro VARCHAR(255),
    numero VARCHAR(20),
    complemento VARCHAR(100),
    bairro VARCHAR(100),
    cidade VARCHAR(100),
    uf CHAR(2),
    cep CHAR(8),
    ddd CHAR(2),
    telefone VARCHAR(20),
    fax VARCHAR(20),
    endereco_eletronico VARCHAR(255),
    representante VARCHAR(255),
    cargo_representante VARCHAR(100),
    regiao_de_comercializacao VARCHAR(50),
    data_registro_ans DATE
);

-- Índices úteis
CREATE INDEX idx_operadoras_uf ON operadoras(uf);
CREATE INDEX idx_operadoras_modalidade ON operadoras(modalidade);

\copy operadoras(
    registro_operadora, cnpj, razao_social, nome_fantasia, modalidade, logradouro, numero, complemento, 
    bairro, cidade, uf, cep, ddd, telefone, fax, endereco_eletronico, 
    representante, cargo_representante, regiao_de_comercializacao, data_registro_ans
)
FROM 'data/operadoras_ativas.csv'
DELIMITER ';'
CSV HEADER
ENCODING 'UTF8';

CREATE TABLE despesas_consolidadas (
    id SERIAL PRIMARY KEY,
    cnpj CHAR(14) REFERENCES operadoras(cnpj),
    razao_social VARCHAR(255),
    trimestre SMALLINT,
    ano INT,
    valor_despesas DECIMAL(20,2)
);

-- Índices úteis
CREATE INDEX idx_despesas_cnpj ON despesas_consolidadas(cnpj);
CREATE INDEX idx_despesas_trimestre ON despesas_consolidadas(trimestre);
CREATE INDEX idx_despesas_ano ON despesas_consolidadas(ano);

\copy despesas_consolidadas(cnpj, razao_social, trimestre, ano, valor_despesas)
FROM 'data/consolidado_despesas.csv'
DELIMITER ';'
CSV HEADER
ENCODING 'UTF8';

CREATE TABLE despesas_agregadas (
    id SERIAL PRIMARY KEY,
    cnpj CHAR(14) REFERENCES operadoras(cnpj),
    razao_social VARCHAR(255),
    uf CHAR(2),
    total_despesas DECIMAL(20,2),
    media_trimestral DECIMAL(20,2),
    desvio_padrao DECIMAL(20,2)
);

-- Índices úteis
CREATE INDEX idx_agg_razao_uf ON despesas_agregadas(razao_social, uf);
CREATE INDEX idx_agg_total ON despesas_agregadas(total_despesas DESC);

\copy despesas_agregadas(cnpj, razao_social, uf, total_despesas, media_trimestral, desvio_padrao)
FROM 'data/despesas_agregadas.csv'
DELIMITER ';'
CSV HEADER
ENCODING 'UTF8';
