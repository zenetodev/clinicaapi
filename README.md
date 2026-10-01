# Clínica API

Backend REST para gerenciamento de usuários e consultas odontológicas, construído com Java 21 e Spring Boot 4.

## Stack

- Java 21
- Spring Boot 4.1
- Spring Web MVC
- Spring Data JPA
- PostgreSQL/Supabase
- Flyway
- Spring Security e JWT
- Bean Validation
- OpenAPI/Swagger
- JUnit 5, Mockito e MockMvc

## Funcionalidades

- Cadastro de usuários como paciente ou dentista.
- Senhas armazenadas somente como hash BCrypt.
- Login com emissão de JWT.
- Autorização por role.
- Agendamento, confirmação e cancelamento de consultas.
- Consulta por identificador.
- Listagem filtrada e paginada.
- Regras de propriedade para impedir acesso a consultas de outros usuários.
- Migrations versionadas do banco de dados.

## Requisitos

- JDK 21
- PostgreSQL compatível ou projeto Supabase
- Maven Wrapper incluído no projeto

## Configuração

As credenciais não ficam no código. Crie um arquivo `.env` na raiz, que é ignorado pelo Git:

```dotenv
DB_URL=jdbc:postgresql://<host>:<porta>/<database>?prepareThreshold=0
DB_USERNAME=<usuario>
DB_PASSWORD=<senha>
JWT_SECRET=<chave-com-pelo-menos-32-caracteres>
JWT_EXPIRATION_SECONDS=3600
```

O `.env` é carregado pelo comando de execução da IDE ou pelo terminal. Nunca publique esse arquivo.

## Documentação da API

Com a aplicação em execução:

- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`
- Health check: `http://localhost:8080/actuator/health`

### Endpoints principais

| Método | Rota | Acesso |
| --- | --- | --- |
| `POST` | `/usuarios` | Público |
| `POST` | `/usuarios/login` | Público |
| `POST` | `/agendamentos` | Role `PACIENTE` |
| `GET` | `/agendamentos` | Autenticado |
| `GET` | `/agendamentos/{consultaId}` | Autenticado |
| `POST` | `/agendamentos/confirmar` | Role `DENTISTA` |
| `POST` | `/agendamentos/cancelar` | Role `PACIENTE` ou `DENTISTA` |

As rotas protegidas exigem o header:

```http
Authorization: Bearer <jwt>
```

## Listagem de consultas

`GET /agendamentos` aceita os filtros `status`, `dataHoraInicio`, `dataHoraFim`, `pacienteId`, `dentistaId`, `pagina` e `tamanho`.

Exemplo:

```text
/agendamentos?status=AGENDADA&pagina=0&tamanho=20
```

O tamanho da página varia de 1 a 100 e o usuário só consegue consultar consultas vinculadas ao próprio paciente ou dentista.

## Banco de dados

O schema é controlado pelo Flyway. O Hibernate usa `ddl-auto=validate` e não altera tabelas automaticamente.

Migrations atuais:

- `V1__create_consultas.sql`
- `V2__create_usuarios.sql`
- `V3__add_usuario_senha_hash.sql`

## Testes

Os testes usam o PostgreSQL configurado no ambiente, sem banco H2. Com as variáveis do `.env` carregadas:

```powershell
.\mvnw.cmd test
```

Os testes unitários e web slice também podem ser executados individualmente pelo Maven:

```powershell
.\mvnw.cmd -Dtest=ConsultaControllerTest test
```

## Arquitetura

O código está organizado por contexto e responsabilidade:

```text
src/main/java/com/clinicasc/api
├── agendamento
│   ├── application
│   ├── domain
│   └── infrastructure
└── usuario
    ├── application
    ├── domain
    └── infrastructure
```

O domínio concentra regras de negócio, os casos de uso coordenam operações, e a infraestrutura implementa REST, persistência, segurança e configurações externas.

## Próximas evoluções

- Separar perfis de paciente e dentista em recursos próprios.
- Adicionar atualização e desativação de usuários.
- Implementar autorização por propriedade também para operações administrativas.
- Criar testes de integração de fluxo completo.
- Adicionar observabilidade, CI e documentação de deploy.