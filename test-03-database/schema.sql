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

CREATE INDEX idx_operadoras_uf ON operadoras(uf);
CREATE INDEX idx_operadoras_modalidade ON operadoras(modalidade);


CREATE TABLE despesas_consolidadas (
    id SERIAL PRIMARY KEY,
    cnpj CHAR(14),
    razao_social VARCHAR(255),
    trimestre SMALLINT,
    ano INT,
    valor_despesas DECIMAL(20,2)
);

CREATE INDEX idx_despesas_cnpj ON despesas_consolidadas(cnpj);
CREATE INDEX idx_despesas_trimestre ON despesas_consolidadas(trimestre);
CREATE INDEX idx_despesas_ano ON despesas_consolidadas(ano);


CREATE TABLE despesas_agregadas (
    id SERIAL PRIMARY KEY,
    razao_social VARCHAR(255),
    uf CHAR(2),
    total_despesas DECIMAL(20,2),
    media_trimestral DECIMAL(20,2),
    desvio_padrao DECIMAL(20,2)
);

CREATE INDEX idx_agg_razao_uf ON despesas_agregadas(razao_social, uf);
CREATE INDEX idx_agg_total ON despesas_agregadas(total_despesas DESC);
