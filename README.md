# Sistema de Agendamento de Oficina Automotiva

Projeto desenvolvido para gerenciar o agendamento de serviços automotivos (revisão, troca de óleo, alinhamento, etc.) permitindo que clientes possam agendar serviços de interesse, acompanhando horários disponíveis e status do atendimento.
O sistema também vai permitir gerenciamento por parte da oficina, como controle de agenda, serviços oferecidos e clientes cadastrados.

## Stack Tecnológica
- **Linguagem:** Java
- **Framework Principal:** Spring Boot
- **Persistência:** Spring Data JPA
- **Segurança:** Spring Security + JWT (autenticação e autorização)
- **Bancos de Dados:** PostgreSQL (Relacional - negócio) e MongoDB (NoSQL - auditoria)
- **Documentação:** Swagger / OpenAPI
- **Frontend:** React
- **Controle de Versão:** Git + GitHub

## Requisitos Funcionais (RF)
- **RF01: Cadastro de Usuários** - Registro de novos usuários (clientes e funcionários).
- **RF02: Autenticação e Login** - Sistema de login seguro com JWT.
- **RF03: Controle de Perfis de Acesso** - Gestão dos usuários com perfil Cliente, Gerente e Mecânicos.
- **RF04: Gerenciamento de Clientes** - Manutenção dos dados dos clientes pela oficina.
- **RF05: Gerenciamento de Veículos** - Vínculo de veículos aos clientes.
- **RF06: Gerenciamento do Catálogo de Serviços** - Gestão do catálogo de serviços oferecidos.
- **RF07: Criação de Agendamentos** - Registro de novas solicitações de serviço.
- **RF08: Listagem de Horários Disponíveis** - Consulta de disponibilidade de agenda.
- **RF09: Alteração e Cancelamento de Agendamentos** - Gestão do ciclo de vida do agendamento.
- **RF10: Controle de Status dos Serviços** - Acompanhamento do progresso do serviço (ex: Em execução, Finalizado).
- **RF11: Consultas Personalizadas por Período** - Filtros de serviços por período e status.
- **RF12: Consultas por Cliente/Veículo** - Filtros para localização de clientes e veículos. 

## Equipe e Divisão de Tarefas
- **Marlus Silva (marlus@imd.ufrn.br):** Modelagem, requisitos, configuração de banco, regras de negócio, endpoints REST, segurança e queries.
- **Bruno Silva (brunosfs@gmail.com):** Integração frontend, consumo da API, testes de endpoints e CRUDs auxiliares.
- **Glauber Galvão (glauber.galvao@gmail.com):** Auxílio na modelagem, criação de entidades, implementação de cadastros e suporte em testes.

