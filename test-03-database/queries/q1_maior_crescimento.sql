WITH despesas_ordenadas AS (
    SELECT
        cnpj,
        razao_social,
        ano,
        trimestre,
        valor_despesas,
        ROW_NUMBER() OVER (
            PARTITION BY cnpj
            ORDER BY ano, trimestre
        ) AS rn_inicio,
        ROW_NUMBER() OVER (
            PARTITION BY cnpj
            ORDER BY ano DESC, trimestre DESC
        ) AS rn_fim
    FROM despesas_consolidadas
),
inicio_fim AS (
    SELECT
        cnpj,
        razao_social,
        MAX(CASE WHEN rn_inicio = 1 THEN valor_despesas END) AS valor_inicio,
        MAX(CASE WHEN rn_fim = 1 THEN valor_despesas END) AS valor_fim
    FROM despesas_ordenadas
    GROUP BY cnpj, razao_social
)
SELECT
    razao_social,
    ((valor_fim - valor_inicio) / valor_inicio) * 100 AS crescimento_percentual
FROM inicio_fim
WHERE valor_inicio > 0
ORDER BY crescimento_percentual DESC
LIMIT 5;
