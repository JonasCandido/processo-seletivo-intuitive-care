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

CREATE TABLE consolidado_despesas (
    id SERIAL PRIMARY KEY,
    cnpj CHAR(14) NOT NULL REFERENCES operadoras(cnpj),
    trimestre INT NOT NULL,
    ano INT NOT NULL,
    valor_despesas DECIMAL(20,2) NOT NULL
);

-- Índices para análises rápidas
CREATE INDEX idx_despesas_cnpj ON despesas(cnpj);
CREATE INDEX idx_despesas_trimestre ON despesas(trimestre);
CREATE INDEX idx_despesas_ano ON despesas(ano);

CREATE TABLE despesas_agregadas (
    id SERIAL PRIMARY KEY,
    cnpj CHAR(14) REFERENCES operadoras(cnpj),
    total_despesas DECIMAL(20,2) NOT NULL,
    media_trimestral DECIMAL(20,2),
    desvio_padrao DECIMAL(20,2)
);

-- Índices para consultas analíticas
CREATE INDEX idx_agg_razao_uf ON despesas_agregadas(razao_social, uf);
CREATE INDEX idx_agg_total ON despesas_agregadas(total_despesas DESC);
