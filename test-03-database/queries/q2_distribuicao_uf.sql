SELECT
    uf,
    SUM(total_despesas) AS total_despesas_uf,
    AVG(total_despesas) AS media_por_operadora
FROM despesas_agregadas
GROUP BY uf
ORDER BY total_despesas_uf DESC
LIMIT 5;
