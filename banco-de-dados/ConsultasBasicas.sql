-- 1. Todas as empresas cadastradas
SELECT CNPJ, NomeFantasia, email
FROM EmpresaAgricola
ORDER BY NomeFantasia;

-- 2. Alertas de gravidade Alta
SELECT ID, Nome, Tipo, Data_de_Emissao
FROM Alertas
WHERE Grau_Gravidade = 'Alta'
ORDER BY Data_de_Emissao DESC;

-- 3. Alertas emitidos em um período
SELECT Nome, Tipo, Grau_Gravidade, Data_de_Emissao
FROM Alertas
WHERE Data_de_Emissao BETWEEN TIMESTAMP '2026-05-01 00:00:00'
                          AND TIMESTAMP '2026-05-31 23:59:59'
ORDER BY Data_de_Emissao;

-- 4. Quantidade de alertas por tipo
SELECT Tipo, COUNT(*) AS total
FROM Alertas
GROUP BY Tipo
ORDER BY total DESC;

-- 5. Quantidade de alertas por grau de gravidade
SELECT Grau_Gravidade, COUNT(*) AS total
FROM Alertas
GROUP BY Grau_Gravidade
ORDER BY total DESC;

-- 6. Busca de empresa por e-mail (case-insensitive)
SELECT CNPJ, NomeFantasia, email
FROM EmpresaAgricola
WHERE LOWER(email) = LOWER('TI@terraverde.com.br');

-- 7. Endereços (fazendas) com o nome da empresa dona
SELECT e.ID, ea.NomeFantasia, e.ERD_PlusCode
FROM Enderecos e
JOIN EmpresaAgricola ea ON ea.CNPJ = e.CNPJ
ORDER BY ea.NomeFantasia;

-- 8. Número de fazendas por empresa
SELECT ea.NomeFantasia, COUNT(e.ID) AS qtd_fazendas
FROM EmpresaAgricola ea
LEFT JOIN Enderecos e ON e.CNPJ = ea.CNPJ
GROUP BY ea.CNPJ, ea.NomeFantasia
ORDER BY qtd_fazendas DESC;

-- 9. Regiões afetadas por cada alerta
SELECT a.Nome AS alerta, r.ERD_PlusCode
FROM RegioesAfetadas r
JOIN Alertas a ON a.ID = r.Alertas_ID
ORDER BY a.Nome;

-- 10. Quantidade de regiões afetadas por alerta
SELECT a.Nome AS alerta, COUNT(r.ID) AS qtd_regioes
FROM Alertas a
LEFT JOIN RegioesAfetadas r ON r.Alertas_ID = a.ID
GROUP BY a.ID, a.Nome
ORDER BY qtd_regioes DESC;

-- 11. Notificações: quais empresas receberam quais alertas
SELECT ea.NomeFantasia, a.Nome AS alerta, a.Grau_Gravidade, a.Data_de_Emissao
FROM NotificacoesRecebidas n
JOIN EmpresaAgricola ea ON ea.CNPJ = n.CNPJ
JOIN Alertas a ON a.ID = n.Alertas_ID
ORDER BY ea.NomeFantasia, a.Data_de_Emissao;

-- 12. Quantidade de notificações recebidas por empresa
SELECT ea.NomeFantasia, COUNT(n.Alertas_ID) AS qtd_notificacoes
FROM EmpresaAgricola ea
LEFT JOIN NotificacoesRecebidas n ON n.CNPJ = ea.CNPJ
GROUP BY ea.CNPJ, ea.NomeFantasia
ORDER BY qtd_notificacoes DESC;
