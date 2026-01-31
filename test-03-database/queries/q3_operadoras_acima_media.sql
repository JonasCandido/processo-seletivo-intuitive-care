WITH media_trimestre AS (
    SELECT
        ano,
        trimestre,
        AVG(valor_despesas) AS media_geral
    FROM despesas_consolidadas
    GROUP BY ano, trimestre
),
comparacao AS (
    SELECT
        d.cnpj,
        d.razao_social,
        d.ano,
        d.trimestre,
        CASE
            WHEN d.valor_despesas > m.media_geral THEN 1
            ELSE 0
        END AS acima_media
    FROM despesas_consolidadas d
    JOIN media_trimestre m
        ON d.ano = m.ano
       AND d.trimestre = m.trimestre
)
SELECT
    COUNT(DISTINCT cnpj) AS operadoras_acima_media
FROM (
    SELECT
        cnpj
    FROM comparacao
    GROUP BY cnpj
    HAVING SUM(acima_media) >= 2
) t;
