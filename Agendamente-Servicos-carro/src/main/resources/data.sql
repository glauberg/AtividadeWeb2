-- Perfil
INSERT INTO perfil (id, nome) VALUES (1, 'ROLE_CLIENTE');
INSERT INTO perfil (id, nome) VALUES (2, 'ROLE_MECANICO');
INSERT INTO perfil (id, nome) VALUES (3, 'ROLE_GERENTE');
ALTER SEQUENCE perfil_id_seq RESTART WITH 10;

-- Pessoa (id, nome, cpf, telefone)
INSERT INTO pessoa (id, nome, cpf, telefone) VALUES (1, 'Administrador Geral', '00000000000', '84900000000');
INSERT INTO pessoa (id, nome, cpf, telefone) VALUES (2, 'Cliente João', '11111111111', '84911111111');
INSERT INTO pessoa (id, nome, cpf, telefone) VALUES (3, 'Cliente Maria', '22222222222', '84922222222');
INSERT INTO pessoa (id, nome, cpf, telefone) VALUES (4, 'Cliente Carlos', '33333333333', '84933333333');
INSERT INTO pessoa (id, nome, cpf, telefone) VALUES (5, 'Mecânico Pedro', '44444444444', '84944444444');
INSERT INTO pessoa (id, nome, cpf, telefone) VALUES (6, 'Mecânico Lucas', '55555555555', '84955555555');
INSERT INTO pessoa (id, nome, cpf, telefone) VALUES (7, 'Gerente Ana', '66666666666', '84966666666');
ALTER SEQUENCE pessoa_id_seq RESTART WITH 10;

-- Funcionario (id igual ao da pessoa)
INSERT INTO funcionario (id) VALUES (1);
INSERT INTO funcionario (id) VALUES (5);
INSERT INTO funcionario (id) VALUES (6);
INSERT INTO funcionario (id) VALUES (7);

-- Cliente (id igual ao da pessoa)
INSERT INTO cliente (id) VALUES (2);
INSERT INTO cliente (id) VALUES (3);
INSERT INTO cliente (id) VALUES (4);

-- Usuario (ativo, email, senha, perfil_id, pessoa_id)
INSERT INTO usuario (id, ativo, email, senha, perfil_id, pessoa_id) VALUES (1, true, 'admin@oficina.com', 'senha123', 3, 1);
INSERT INTO usuario (id, ativo, email, senha, perfil_id, pessoa_id) VALUES (2, true, 'joao@email.com', 'senha123', 1, 2);
INSERT INTO usuario (id, ativo, email, senha, perfil_id, pessoa_id) VALUES (3, true, 'maria@email.com', 'senha123', 1, 3);
INSERT INTO usuario (id, ativo, email, senha, perfil_id, pessoa_id) VALUES (4, true, 'carlos@email.com', 'senha123', 1, 4);
INSERT INTO usuario (id, ativo, email, senha, perfil_id, pessoa_id) VALUES (5, true, 'pedro.mec@oficina.com', 'senha123', 2, 5);
INSERT INTO usuario (id, ativo, email, senha, perfil_id, pessoa_id) VALUES (6, true, 'lucas.mec@oficina.com', 'senha123', 2, 6);
INSERT INTO usuario (id, ativo, email, senha, perfil_id, pessoa_id) VALUES (7, true, 'ana.gerente@oficina.com', 'senha123', 3, 7);
ALTER SEQUENCE usuario_id_seq RESTART WITH 10;

-- Veiculo (id, marca, modelo, placa, cliente_id)
INSERT INTO veiculo (id, marca, modelo, placa, cliente_id) VALUES (1, 'Toyota', 'Corolla', 'ABC-1234', 2);
INSERT INTO veiculo (id, marca, modelo, placa, cliente_id) VALUES (2, 'Honda', 'Civic', 'DEF-5678', 2);
INSERT INTO veiculo (id, marca, modelo, placa, cliente_id) VALUES (3, 'Ford', 'Fiesta', 'GHI-9012', 3);
INSERT INTO veiculo (id, marca, modelo, placa, cliente_id) VALUES (4, 'Chevrolet', 'Onix', 'JKL-3456', 3);
INSERT INTO veiculo (id, marca, modelo, placa, cliente_id) VALUES (5, 'Hyundai', 'HB20', 'MNO-7890', 4);
INSERT INTO veiculo (id, marca, modelo, placa, cliente_id) VALUES (6, 'Fiat', 'Argo', 'PQR-1234', 4);
ALTER SEQUENCE veiculo_id_seq RESTART WITH 10;

-- Servico (id, nome, preco_base)
INSERT INTO servico (id, nome, preco_base) VALUES (1, 'Troca de Óleo', 150.00);
INSERT INTO servico (id, nome, preco_base) VALUES (2, 'Alinhamento', 100.00);
INSERT INTO servico (id, nome, preco_base) VALUES (3, 'Balanceamento', 80.00);
INSERT INTO servico (id, nome, preco_base) VALUES (4, 'Revisão Geral', 450.00);
INSERT INTO servico (id, nome, preco_base) VALUES (5, 'Troca de Pastilhas', 200.00);
INSERT INTO servico (id, nome, preco_base) VALUES (6, 'Limpeza de Bicos', 120.00);
ALTER SEQUENCE servico_id_seq RESTART WITH 10;

-- Produto (id, estoque, nome, preco_venda)
INSERT INTO produto (id, estoque, nome, preco_venda) VALUES (1, 50, 'Óleo Sintético 5W40', 45.00);
INSERT INTO produto (id, estoque, nome, preco_venda) VALUES (2, 30, 'Filtro de Óleo', 25.00);
INSERT INTO produto (id, estoque, nome, preco_venda) VALUES (3, 100, 'Pastilha de Freio', 120.00);
INSERT INTO produto (id, estoque, nome, preco_venda) VALUES (4, 20, 'Filtro de Ar', 35.00);
INSERT INTO produto (id, estoque, nome, preco_venda) VALUES (5, 40, 'Fluido de Freio', 55.00);
INSERT INTO produto (id, estoque, nome, preco_venda) VALUES (6, 15, 'Vela de Ignição', 40.00);
ALTER SEQUENCE produto_id_seq RESTART WITH 10;

-- Agendamento (id, data_hora, status, valor_total, cliente_id, funcionario_id, veiculo_id)
INSERT INTO agendamento (id, data_hora, status, valor_total, cliente_id, funcionario_id, veiculo_id) VALUES (1, '2026-05-10 08:00:00', 'AGENDADO', 220.00, 2, 5, 1);
INSERT INTO agendamento (id, data_hora, status, valor_total, cliente_id, funcionario_id, veiculo_id) VALUES (2, '2026-05-10 10:00:00', 'EM_MANUTENCAO', 300.00, 3, 6, 3);
INSERT INTO agendamento (id, data_hora, status, valor_total, cliente_id, funcionario_id, veiculo_id) VALUES (3, '2026-05-11 14:00:00', 'CONCLUIDO', 450.00, 4, 5, 4);
INSERT INTO agendamento (id, data_hora, status, valor_total, cliente_id, funcionario_id, veiculo_id) VALUES (4, '2026-05-11 16:00:00', 'CANCELADO', 0.00, 4, 6, 5);
INSERT INTO agendamento (id, data_hora, status, valor_total, cliente_id, funcionario_id, veiculo_id) VALUES (5, '2026-05-12 09:00:00', 'AGENDADO', 180.00, 2, 5, 2);
INSERT INTO agendamento (id, data_hora, status, valor_total, cliente_id, funcionario_id, veiculo_id) VALUES (6, '2026-05-12 11:00:00', 'CONCLUIDO', 550.00, 4, 6, 6);
ALTER SEQUENCE agendamento_id_seq RESTART WITH 10;

-- Pagamento (id, data_pagamento, metodo, status, valor_pago, agendamento_id)
INSERT INTO pagamento (id, data_pagamento, metodo, status, valor_pago, agendamento_id) VALUES (1, NULL, 'PIX', 'PENDENTE', 220.00, 1);
INSERT INTO pagamento (id, data_pagamento, metodo, status, valor_pago, agendamento_id) VALUES (2, NULL, 'CARTAO_CREDITO', 'PENDENTE', 300.00, 2);
INSERT INTO pagamento (id, data_pagamento, metodo, status, valor_pago, agendamento_id) VALUES (3, '2026-05-11 15:30:00', 'DINHEIRO', 'PAGO', 450.00, 3);
INSERT INTO pagamento (id, data_pagamento, metodo, status, valor_pago, agendamento_id) VALUES (4, NULL, 'PIX', 'RECUSADO', 0.00, 4);
INSERT INTO pagamento (id, data_pagamento, metodo, status, valor_pago, agendamento_id) VALUES (5, NULL, 'CARTAO_CREDITO', 'PENDENTE', 180.00, 5);
INSERT INTO pagamento (id, data_pagamento, metodo, status, valor_pago, agendamento_id) VALUES (6, '2026-05-12 12:00:00', 'PIX', 'PAGO', 550.00, 6);
ALTER SEQUENCE pagamento_id_seq RESTART WITH 10;

-- ItemProduto (id, preco_unitario_historico, quantidade, agendamento_id, produto_id)
INSERT INTO item_produto (id, preco_unitario_historico, quantidade, agendamento_id, produto_id) VALUES (1, 45.00, 4, 1, 1);
INSERT INTO item_produto (id, preco_unitario_historico, quantidade, agendamento_id, produto_id) VALUES (2, 25.00, 1, 1, 2);
INSERT INTO item_produto (id, preco_unitario_historico, quantidade, agendamento_id, produto_id) VALUES (3, 120.00, 2, 2, 3);
INSERT INTO item_produto (id, preco_unitario_historico, quantidade, agendamento_id, produto_id) VALUES (4, 55.00, 1, 2, 5);
INSERT INTO item_produto (id, preco_unitario_historico, quantidade, agendamento_id, produto_id) VALUES (5, 40.00, 4, 3, 6);
ALTER SEQUENCE item_produto_id_seq RESTART WITH 10;

-- Agendamento_Servico (agendamento_id, servico_id)
INSERT INTO agendamento_servico (agendamento_id, servico_id) VALUES (1, 1);
INSERT INTO agendamento_servico (agendamento_id, servico_id) VALUES (2, 5);
INSERT INTO agendamento_servico (agendamento_id, servico_id) VALUES (3, 4);
INSERT INTO agendamento_servico (agendamento_id, servico_id) VALUES (5, 2);
INSERT INTO agendamento_servico (agendamento_id, servico_id) VALUES (5, 3);
INSERT INTO agendamento_servico (agendamento_id, servico_id) VALUES (6, 4);
