# Demo Itaú - Serviço de Cálculo de Seguro

## Visão Geral
Este projeto é uma API REST desenvolvida com Spring Boot que oferece funcionalidades para cálculo de seguro de veículos com base no valor do veículo e na localização do cliente.

## Funcionalidades Principais

### 1. Cálculo de Seguro
O serviço calcula o valor do seguro de um veículo com base em:
- Valor do veículo
- Localização do cliente (estado)

As regras de cálculo são:
- Veículos com valor até R$ 70.000:
  - 5% do valor para clientes de SP
  - 4% do valor para clientes de outros estados
- Veículos com valor entre 70.001 e 100.000:
  - 5,5% do valor independente da localização
- Veículos com valor acima de R$ 100.000:
  - 6% do valor independente da localização

### 2. Armazenamento de Cotações
Todas as cotações de seguro são armazenadas no banco de dados para consulta posterior.

### 3. Consulta de Cotações
O serviço permite consultar todas as cotações realizadas ou uma cotação específica por ID.


## Arquitetura 
A arquitetura escolhida foi o padrão MVC, com a separação dos pacotes 

## Estrutura do Projeto
```
src/main/java/br/dev/ldemo/itau/
├── config/                  # Configurações da aplicação
├── controller/              # Controladores REST
├── dto/                     # Objetos de transferência de dados
├── entity/                  # Entidades e modelos de domínio
├── exception/               # Exception
├── repository/              # Repositórios para acesso ao banco de dados
└── service/                 # Lógica de negócios
```

## Endpoints da API

### Cálculo de Seguro
```
POST /customers/quote
```
Calcula o seguro para um veículo com base nos dados do cliente.

**Corpo da Requisição:**
```json
{
  "customer": {
    "name": "Nome do Cliente",
    "document": "12345678900",
    "birthDate": "1990-01-01",
    "location": "SP",
    "vehicle_value": 50000.0
  }
}
```

**Resposta:**
```json
{
  "name": "Nome do Cliente",
  "location": "SP",
  "vehicle_value": 2500.0
}
```

### Consulta de Todas as Cotações
```
GET /customers/quotes
```
Retorna todas as cotações de seguro armazenadas.

**Resposta:**
```json
[
  {
    "id": 1,
    "name": "Nome do Cliente 1",
    "location": "SP",
    "vehicle_value": 2500.0
  },
  {
    "id": 2,
    "name": "Nome do Cliente 2",
    "location": "RJ",
    "vehicle_value": 2000.0
  }
]
```

### Consulta de Cotação por ID
```
GET /customers/quote/{id}
```
Retorna uma cotação específica pelo ID.

**Resposta:**
```json
{
  "id": 1,
  "name": "Nome do Cliente",
  "location": "SP",
  "vehicle_value": 2500.0
}
```

## Link Relatório Jacoco
- `target/site/jacoco/index.html`

## Configuração e Execução

### Requisitos
- Java 21 ou superior
- Springboot 3.4.5
- Maven

### Executando a Aplicação
1. Clone o repositório
2. Execute o comando: `mvn spring-boot:run`
3. A aplicação estará disponível em: `http://localhost:8087`

### Banco de Dados
A aplicação utiliza um banco de dados H2 em memória. O console do H2 está habilitado e pode ser acessado em:
```
http://localhost:8087/h2-console
```

Credenciais:
- JDBC URL: `jdbc:h2:mem:testdb`
- Usuário: `sa`
- Senha: (vazio)

## Documentação da API
A documentação completa da API está disponível através do Swagger UI:
```
http://localhost:8087/swagger-ui.html
```

## Tecnologias Utilizadas
- Spring Boot
- Spring Data JPA
- H2 Database
- Lombok
- Swagger/OpenAPI