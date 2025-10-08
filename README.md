Sistema de Tarefas — Java 21 + Spring Boot 3.3 + Angular 19

Resumo
- API REST para gestão de tarefas e projetos (backend Java 21/Spring Boot 3.3).
- SPA Angular 19 (Angular Material) para CRUD básico de tarefas.
- Banco H2 em memória com sintaxe compatível com DB2 (MODE=DB2), scripts `schema.sql` e `data.sql`.

Como rodar
- Pré‑requisitos: Java 21, Node 20+, npm 9+, portas 8080 e 4200 livres.

Backend (porta 8080)
- `cd backend`
- `spring-boot:run`
- Swagger UI: `http://localhost:8080/swagger-ui.html`

Frontend (porta 4200)
- `cd frontend`
- `npm ci`
- `npm start`
- App: `http://localhost:4200`

Funcionalidades implementadas
- Tarefas
  - Listagem paginada com filtro por projeto e ordenação por data (asc/desc).
  - Exclusão de tarefa.
  - Criação de tarefa com formulário reativo e validação.
- Projetos
  - Endpoint para listar projetos e popular selects no frontend.

Arquitetura (backend)
- Camadas:
  - Controller: expõe endpoints REST (`/api/v1/tarefas`, `/api/v1/projetos`).
  - Service: regras de negócio, validações e orquestração.
  - Repository: Spring Data JPA para acesso a dados.
  - Model/Entity: `Projeto`, `Tarefa` (mapeadas a `PROJETO`, `TAREFA`).
  - DTOs: `TarefaRequest` (entrada com validação), `TarefaResponse` (saída desacoplada).
- Tratamento de erros: `GlobalExceptionHandler` com `ErrorResponse` padronizado.
- CORS: liberado para `http://localhost:4200`.
- Documentação: `SwaggerConfig` com OpenAPI via `springdoc-openapi`.
- Banco: H2 em memória com `MODE=DB2` para compatibilidade de sintaxe.
- Scripts SQL: `backend/src/main/resources/schema.sql`, `data.sql` e exemplos em `sql-exemplos-db2.sql`.

Decisões de design (backend)
- Ordenação server‑side parametrizada (`sortBy`, `sortDir`)
- DTOs para isolar o domínio do contrato externo e permitir evolução de schema.
- Validações de limites (100/500/30) alinhadas ao DDL.

Arquitetura (frontend)
- Angular 19 e Angular Material.
- Serviços HTTP: `TarefaService`, `ProjetoService`
- Páginas/Componentes:
  - `TarefaListComponent`: tabela com dados resumidos, filtro por projeto, ordenação e paginação.
  - `TarefaFormComponent`: formulário reativo com validação.
- Estilos: tema Material prebuilt `azure-blue.css`, melhorias visuais pontuais (grid, cards, chips).

Tecnologias
- Java 21, Spring Boot 3.3, Spring Web, Spring Data JPA, Bean Validation.
- H2 Database (memória) com `MODE=DB2`, Springdoc OpenAPI, Lombok.
- Angular 19, Angular Material, RxJS.

SQL e compatibilidade DB2
- DDL: `backend/src/main/resources/schema.sql` (tabelas, default em `DATA_CRIACAO`, índice composto).
- Seed: `backend/src/main/resources/data.sql`.
- Exemplos: `backend/src/main/resources/sql-exemplos-db2.sql` (paginação, join, agrupamento por status).

Como validar rapidamente
- Subir backend e acessar Swagger UI para testar endpoints.
- H2 Console para inspecionar dados e executar os exemplos de SQL.
- Subir frontend e validar listagem, filtros, ordenação e criação.

Futuros incrementos sugeridos (próximas sprints)
- Editar tarefa (PUT) e obter por id (GET `/tarefas/{id}`), com formulário compartilhado para edição.
- Enum de status no backend.
- Autenticação (Keycloak/JWT) e perfis (dev/prod) com configurações separadas.

Parte 4 — Experiência com Flex
- Respondida no arquivo separado `PARTE_4_FLEX.md`.

