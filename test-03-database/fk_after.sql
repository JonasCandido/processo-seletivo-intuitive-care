-- remove despesas sem operadora
DELETE FROM despesas_consolidadas d
WHERE NOT EXISTS (
  SELECT 1
  FROM operadoras o
  WHERE TRIM(o.cnpj) = TRIM(d.cnpj)
);

-- adiciona a FK
ALTER TABLE despesas_consolidadas
ADD CONSTRAINT fk_despesas_operadoras
FOREIGN KEY (cnpj) REFERENCES operadoras(cnpj);
