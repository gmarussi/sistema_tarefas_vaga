INSERT INTO PROJETO (NOME) VALUES ('Projeto Angular');
INSERT INTO PROJETO (NOME) VALUES ('Projeto Spring Boot');

INSERT INTO TAREFA (TITULO, DESCRICAO, STATUS, DATA_CRIACAO, ID_PROJETO)
VALUES 
('Criar componentes iniciais', 'Montar layout base', 'Aberta', CURRENT_TIMESTAMP, 1),
('Configurar rotas', 'Adicionar navegação principal', 'Aberta', CURRENT_TIMESTAMP, 1),
('Criar serviço HTTP', 'Consumir API de tarefas', 'Concluída', CURRENT_TIMESTAMP, 1),
('Criar entidades', 'Modelar classes Projeto e Tarefa', 'Aberta', CURRENT_TIMESTAMP, 2),
('Configurar H2', 'Subir banco em memória', 'Concluída', CURRENT_TIMESTAMP, 2),
('Criar endpoints REST', 'Implementar CRUD', 'Aberta', CURRENT_TIMESTAMP, 2),
('Implementar paginação', 'Adicionar parâmetros page e size', 'Aberta', CURRENT_TIMESTAMP, 2),
('Adicionar filtro de projeto', 'Filtrar tarefas por idProjeto', 'Aberta', CURRENT_TIMESTAMP, 2),
('Criar tela de listagem', 'Grid com exclusão', 'Aberta', CURRENT_TIMESTAMP, 1),
('Criar formulário de tarefa', 'Campos título e projeto', 'Concluída', CURRENT_TIMESTAMP, 1),
('Testar API com Postman', 'Validar endpoints', 'Aberta', CURRENT_TIMESTAMP, 2),
('Criar DTOs opcionais', 'Separar camadas', 'Aberta', CURRENT_TIMESTAMP, 2),
('Gerar build Angular', 'Integrar com backend', 'Aberta', CURRENT_TIMESTAMP, 1),
('Criar README', 'Documentar projeto', 'Aberta', CURRENT_TIMESTAMP, 1),
('Finalizar entrega', 'Push no GitHub', 'Aberta', CURRENT_TIMESTAMP, 2);
