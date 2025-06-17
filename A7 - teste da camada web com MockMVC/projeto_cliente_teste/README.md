# Projeto Cliente - Testes da Camada Web com MockMVC

Este projeto implementa um sistema de gerenciamento de clientes usando Spring Boot com testes de integração da camada web utilizando MockMVC.

## Descrição do Sistema

O sistema permite o gerenciamento completo de clientes, incluindo operações de CRUD (Create, Read, Update, Delete) e funcionalidades de busca avançada por diferentes critérios.

### Entidade Client
A entidade Client possui os seguintes atributos:
- `id`: Identificador único (Long)
- `name`: Nome do cliente (String)
- `cpf`: CPF do cliente (String)
- `income`: Renda do cliente (Double)
- `birthDate`: Data de nascimento (Instant)
- `children`: Número de filhos (Integer)

## Endpoints Disponíveis

### Endpoints Principais

| Método | Endpoint | Descrição | Parâmetros |
|--------|----------|-----------|------------|
| GET | `/clients` | Lista todos os clientes paginados | page, linesPerPage, direction, orderBy |
| GET | `/clients/{id}` | Busca cliente por ID | id (path variable) |
| POST | `/clients` | Cria um novo cliente | ClientDTO no body |
| PUT | `/clients/{id}` | Atualiza cliente existente | id (path variable), ClientDTO no body |
| DELETE | `/clients/{id}` | Remove cliente por ID | id (path variable) |

### Endpoints de Busca Customizada

| Método | Endpoint | Descrição | Parâmetros |
|--------|----------|-----------|------------|
| GET | `/clients/income` | Busca clientes por renda exata | income, page, linesPerPage, direction, orderBy |
| GET | `/clients/incomeGreaterThan` | Busca clientes com renda maior que o valor | income, page, linesPerPage, direction, orderBy |
| GET | `/clients/cpfLike` | Busca clientes por CPF (busca parcial) | cpf, page, linesPerPage, direction, orderBy |

## Estrutura do Projeto

```
src/
├── main/
│   ├── java/
│   │   └── com/iftm/client/
│   │       ├── DsclientApplication.java
│   │       ├── dto/
│   │       │   └── ClientDTO.java
│   │       ├── entities/
│   │       │   └── Client.java
│   │       ├── repositories/
│   │       │   └── ClientRepository.java
│   │       ├── resources/
│   │       │   ├── ClientResource.java
│   │       │   └── exceptions/
│   │       │       ├── ResourceExceptionHandler.java
│   │       │       └── StandardError.java
│   │       └── services/
│   │           ├── ClientService.java
│   │           └── exceptions/
│   │               ├── DatabaseException.java
│   │               └── ResourceNotFoundException.java
│   └── resources/
│       ├── application.properties
│       ├── application-test.properties
│       └── import.sql
└── test/
    └── java/
        └── com/iftm/client/
            ├── DsclientApplicationTests.java
            ├── resources/
            │   ├── ClientResourcesTestsIT.java
            │   └── ClientResourcesTestsWithMockIT.java
            └── services/
                ├── ClientServiceIntegrationTest.java
                ├── ClientServiceIntegrationUnitMockito.java
                └── ClientServiceUnitTest.java
```

## Classes de Teste Implementadas

### 1. ClientResourcesTestsIT (Parte 1 - 70%)
Testes de integração que utilizam o banco de dados H2 em memória:

**Testes implementados:**
- `findAllShouldReturnPagedClients()` - Testa busca paginada de todos os clientes
- `findByIdShouldReturnClientWhenIdExists()` - Testa busca por ID existente
- `findByIdShouldReturnNotFoundWhenIdDoesNotExist()` - Testa busca por ID inexistente
- `findByIncomeShouldReturnClientsWhenIncomeExists()` - Testa busca por renda existente
- `findByIncomeShouldReturnEmptyPageWhenIncomeDoesNotExist()` - Testa busca por renda inexistente
- `findByIncomeGreaterThanShouldReturnClientsWhenIncomeExists()` - Testa busca por renda maior que valor
- `findByIncomeGreaterThanShouldReturnEmptyPageWhenNoClientsFound()` - Testa busca sem resultados
- `findByCpfLikeShouldReturnClientsWhenCpfExists()` - Testa busca por CPF existente
- `findByCpfLikeShouldReturnEmptyPageWhenCpfDoesNotExist()` - Testa busca por CPF inexistente
- `insertShouldReturnCreatedAndClientDTO()` - Testa criação de cliente
- `deleteShouldReturnNoContentWhenIdExists()` - Testa remoção com ID existente
- `deleteShouldReturnNotFoundWhenIdDoesNotExist()` - Testa remoção com ID inexistente
- `updateShouldReturnOkAndUpdatedClientWhenIdExists()` - Testa atualização com ID existente
- `updateShouldReturnNotFoundWhenIdDoesNotExist()` - Testa atualização com ID inexistente

### 2. ClientResourcesTestsWithMockIT (Parte 2 - 20%)
Testes que utilizam `@MockBean` para simular o comportamento do service:

**Características:**
- Utiliza `@WebMvcTest` para carregar apenas a camada web
- Mock completo da classe `ClientService`
- Testes isolados da camada de persistência
- Mesmos cenários de teste da Parte 1, mas com dados simulados

## Validações dos Testes

### Status Codes Testados
- **200 OK**: Para buscas e atualizações bem-sucedidas
- **201 CREATED**: Para criação de novos clientes
- **204 NO CONTENT**: Para remoções bem-sucedidas
- **404 NOT FOUND**: Para recursos não encontrados

### Validações JSON
Os testes verificam:
- Estrutura correta do JSON de resposta
- Valores específicos dos campos (id, name, cpf, income, children)
- Estrutura de paginação (content, totalElements, totalPages, size, number)
- Mensagens de erro padronizadas

### Exemplos de JSON de Resposta

**Cliente individual:**
```json
{
  "id": 3,
  "name": "Clarice Lispector",
  "cpf": "10919444522",
  "income": 3800.0,
  "birthDate": "1960-04-13T07:50:00Z",
  "children": 2
}
```

**Erro 404:**
```json
{
  "timestamp": "2024-08-20T00:36:05.891918Z",
  "status": 404,
  "error": "Resource not found",
  "message": "Entity not found",
  "path": "/clients/id/33"
}
```

## Tecnologias Utilizadas

- **Spring Boot 2.4.0**
- **Spring Data JPA**
- **Spring Web**
- **H2 Database** (para testes)
- **JUnit 5**
- **Mockito**
- **MockMVC**
- **Jackson** (para serialização JSON)

## Como Executar os Testes

```bash
# Executar todos os testes
mvn test

# Executar apenas os testes de integração
mvn test -Dtest=ClientResourcesTestsIT

# Executar apenas os testes com mock
mvn test -Dtest=ClientResourcesTestsWithMockIT
```

## Dados de Teste

O arquivo `import.sql` contém 12 registros de clientes para teste, incluindo:
- Conceição Evaristo (income: 1500.0)
- Lázaro Ramos (income: 2500.0)
- Clarice Lispector (income: 3800.0)
- E outros...

Estes dados são carregados automaticamente no banco H2 durante os testes de integração. 