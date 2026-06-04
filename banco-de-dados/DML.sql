BEGIN;

INSERT INTO EmpresaAgricola (CNPJ, NomeFantasia, email, senha_hash) VALUES
    ('11222333000181', 'Fazenda São João',          'contato@fazendasaojoao.com.br',   '$2b$12$abc123hashficticio0000000000000000000000000001'),
    ('44555666000172', 'AgroVale Cooperativa',       'sac@agrovale.coop.br',            '$2b$12$abc123hashficticio0000000000000000000000000002'),
    ('77888999000163', 'Sítio Boa Esperança',        'admin@boaesperanca.agr.br',       '$2b$12$abc123hashficticio0000000000000000000000000003'),
    ('12345678000199', 'Terra Verde Agronegócios',   'ti@terraverde.com.br',            '$2b$12$abc123hashficticio0000000000000000000000000004'),
    ('98765432000110', 'Fazenda Santa Rita',         'gestao@santarita.agro.br',        '$2b$12$abc123hashficticio0000000000000000000000000005');

INSERT INTO Alertas (Nome, Tipo, Grau_Gravidade, Data_de_Emissao, Descricao) VALUES
    ('Tempestade Severa Litoral Sul', 'Problemas climáticos', 'Alta',
        TIMESTAMP '2026-05-10 06:30:00',
        'Tempestade com ventos acima de 90 km/h e risco de alagamentos. Recomenda-se recolher animais e equipamentos.'),
    ('Enchente Bacia do Rio Doce',    'Problemas climáticos', 'Alta',
        TIMESTAMP '2026-05-12 14:15:00',
        'Nível do rio acima da cota de inundação. Evacuar áreas ribeirinhas e proteger silos.'),
    ('Geada Forte Serra Gaúcha',      'Problemas climáticos', 'Média',
        TIMESTAMP '2026-06-01 04:00:00',
        'Previsão de geada com temperaturas negativas. Risco para culturas sensíveis como café e hortaliças.'),
    ('Rompimento de Barragem',        'Erro humano',          'Alta',
        TIMESTAMP '2026-04-22 09:45:00',
        'Falha estrutural em barragem de rejeitos. Acionar plano de emergência e evacuação imediata a jusante.'),
    ('Seca Prolongada Sertão',        'Problemas climáticos', 'Média',
        TIMESTAMP '2026-03-05 12:00:00',
        'Estiagem severa com déficit hídrico acumulado. Racionar irrigação e priorizar reservatórios.'),
    ('Granizo Região dos Vinhedos',   'Problemas climáticos', 'Baixa',
        TIMESTAMP '2026-05-28 17:20:00',
        'Possibilidade de granizo de pequeno porte. Proteger mudas e estufas.');

INSERT INTO Enderecos (CNPJ, ERD_PlusCode) VALUES
    ('11222333000181', '588MC9X8+5R'),
    ('11222333000181', '588MC9V9+Q2'),
    ('44555666000172', '58GR2J4C+8X'),
    ('77888999000163', '6GCRMQVH+3F'),
    ('12345678000199', '584PXMW7+9C'),
    ('12345678000199', '584PXMR5+2J'),
    ('98765432000110', '6FR5C2H8+WW');

INSERT INTO RegioesAfetadas (Alertas_ID, ERD_PlusCode) VALUES
    ((SELECT ID FROM Alertas WHERE Nome = 'Tempestade Severa Litoral Sul'), '588MC9X8+'),
    ((SELECT ID FROM Alertas WHERE Nome = 'Tempestade Severa Litoral Sul'), '588MC900+'),
    ((SELECT ID FROM Alertas WHERE Nome = 'Enchente Bacia do Rio Doce'),    '58GR2J00+'),
    ((SELECT ID FROM Alertas WHERE Nome = 'Enchente Bacia do Rio Doce'),    '58GR2000+'),
    ((SELECT ID FROM Alertas WHERE Nome = 'Enchente Bacia do Rio Doce'),    '58GR3000+'),
    ((SELECT ID FROM Alertas WHERE Nome = 'Geada Forte Serra Gaúcha'),      '6GCRMQ00+'),
    ((SELECT ID FROM Alertas WHERE Nome = 'Rompimento de Barragem'),        '6FR5C200+'),
    ((SELECT ID FROM Alertas WHERE Nome = 'Rompimento de Barragem'),        '6FR50000+'),
    ((SELECT ID FROM Alertas WHERE Nome = 'Seca Prolongada Sertão'),        '78XJ0000+'),
    ((SELECT ID FROM Alertas WHERE Nome = 'Granizo Região dos Vinhedos'),   '584PXM00+');

INSERT INTO NotificacoesRecebidas (CNPJ, Alertas_ID) VALUES
    ('11222333000181', (SELECT ID FROM Alertas WHERE Nome = 'Tempestade Severa Litoral Sul')),
    ('11222333000181', (SELECT ID FROM Alertas WHERE Nome = 'Granizo Região dos Vinhedos')),
    ('44555666000172', (SELECT ID FROM Alertas WHERE Nome = 'Enchente Bacia do Rio Doce')),
    ('44555666000172', (SELECT ID FROM Alertas WHERE Nome = 'Seca Prolongada Sertão')),
    ('77888999000163', (SELECT ID FROM Alertas WHERE Nome = 'Rompimento de Barragem')),
    ('12345678000199', (SELECT ID FROM Alertas WHERE Nome = 'Tempestade Severa Litoral Sul')),
    ('12345678000199', (SELECT ID FROM Alertas WHERE Nome = 'Geada Forte Serra Gaúcha')),
    ('12345678000199', (SELECT ID FROM Alertas WHERE Nome = 'Granizo Região dos Vinhedos')),
    ('98765432000110', (SELECT ID FROM Alertas WHERE Nome = 'Enchente Bacia do Rio Doce')),
    ('98765432000110', (SELECT ID FROM Alertas WHERE Nome = 'Seca Prolongada Sertão'));

COMMIT;
