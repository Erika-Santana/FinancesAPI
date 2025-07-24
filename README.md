

# Projeto: API de Gerenciamento de Transações Financeiras

Projeto de uma API para o controle de transações financeira do usuário, desenvolvida como uma atividade avaliativa da disciplina de Desenvolvimento Web do curso de Análise e Desenvolvimento de Sistemas do IFSP-Araraquara.
A API permite criar, listar, atualizar, excluir e resumir transações de receitas e despesas.

## Pré-requisitos e Execução

A aplicação utiliza um *pool* de conexões configurado via JNDI. Para executá-la, é necessário configurar um *DataSource* no seu servidor de aplicação (como Apache Tomcat) com o nome `jdbc/Erika`.

## Endpoints da API

Abaixo estão detalhados todos os endpoints disponíveis na API.

-----

### 1\. POST /transacoes – Criar nova transação

**Descrição:** Cadastra uma nova transação financeira no banco de dados.

**Método HTTP:** `POST`

**Endpoint:** `/transacoes`

**Corpo da requisição (JSON):**
O cliente deve enviar um objeto JSON sem os campos `ID` e `dateCreation`, que são gerados pelo servidor.

```json
{
  "descricao": "Salário do mês",
  "valor": 5000.00,
  "tipoTransacao": "RECEITAS",
  "tipoCategoria": "SALARIO"
}
```

**Códigos de resposta:**

  * `201 Created` – Transação criada com sucesso.
  * `400 Bad Request` – Dados da requisição inválidos ou ausentes.
  * `500 Internal Server Error` – Erro interno ao persistir os dados.

-----

### 2\. GET /transacoes – Listar transações

**Descrição:** Retorna uma lista de transações, com suporte a filtros e paginação.

**Método HTTP:** `GET`

**Endpoint:** `/transacoes`

**Parâmetros de consulta (Query Params):**

  * `page` (opcional): Número da página para paginação. Padrão: `1`.
  * `limit` (opcional): Quantidade de transações por página. Padrão: `10`.
  * `tipo` (opcional): Filtra por tipo de transação. Valores possíveis: `RECEITAS`, `DESPESAS`.
  * `categoria` (opcional): Filtra por categoria. Valores possíveis: `ALIMENTACAO`, `SAUDE`, `LAZER`, `ESCOLAR`, `TRANSPORTE`, `SALARIO`, `REEMBOLSO`, `FREELANCER`.
  * `mes` (opcional): Filtra por mês (ex: `7` para Julho).
  * `ano` (opcional): Filtra por ano (ex: `2025`).

**Códigos de resposta:**

  * `200 OK` – Lista de transações retornada com sucesso.
  * `400 Bad Request` – Parâmetro de filtro inválido (ex: `tipo` ou `categoria` com valor inexistente).
  * `500 Internal Server Error` – Erro interno no servidor.

**Corpo da resposta (JSON):**

```json
[
  {
    "ID": 1,
    "descricao": "Salário do mês",
    "tipoTransacao": "RECEITAS",
    "tipoCategoria": "SALARIO",
    "dateCreation": "2025-07-23T10:30:00",
    "valor": 5000.00
  },
  {
    "ID": 2,
    "descricao": "Almoço no restaurante",
    "tipoTransacao": "DESPESAS",
    "tipoCategoria": "ALIMENTACAO",
    "dateCreation": "2025-07-23T13:15:00",
    "valor": 35.50
  }
]
```

-----

### 3\. GET /transacoes/{id} – Buscar transação por ID

**Descrição:** Retorna uma transação específica com base no seu ID.

**Método HTTP:** `GET`

**Endpoint:** `/transacoes/{id}`

**Códigos de resposta:**

  * `200 OK` – Transação encontrada e retornada com sucesso.
  * `400 Bad Request` – O ID fornecido na URL é inválido.
  * `404 Not Found` – Nenhuma transação encontrada para o ID fornecido.
  * `500 Internal Server Error` – Erro interno no servidor.

**Corpo da resposta (JSON):**

```json
{
  "ID": 1,
  "descricao": "Salário do mês",
  "tipoTransacao": "RECEITAS",
  "tipoCategoria": "SALARIO",
  "dateCreation": "2025-07-23T10:30:00",
  "valor": 5000.00
}
```

-----

### 4\. PUT /transacoes/{id} – Atualizar transação

**Descrição:** Atualiza os dados de uma transação existente com base no seu ID.

**Método HTTP:** `PUT`

**Endpoint:** `/transacoes/{id}`

**Corpo da requisição (JSON):**

```json
{
  "descricao": "Compra de supermercado",
  "valor": 450.75,
  "tipoTransacao": "DESPESAS",
  "tipoCategoria": "ALIMENTACAO"
}
```

**Códigos de resposta:**

  * `204 No Content` – Transação atualizada com sucesso.
  * `400 Bad Request` – Dados da requisição inválidos ou ID inválido.
  * `404 Not Found` – Nenhuma transação encontrada para o ID fornecido.
  * `500 Internal Server Error` – Erro ao atualizar a transação.

-----

### 5\. DELETE /transacoes/{id} – Excluir transação

**Descrição:** Exclui uma transação com base no seu ID.

**Método HTTP:** `DELETE`

**Endpoint:** `/transacoes/{id}`

**Códigos de resposta:**

  * `204 No Content` – Transação excluída com sucesso.
  * `400 Bad Request` – O ID fornecido na URL é inválido.
  * `404 Not Found` – Nenhuma transação encontrada para o ID fornecido.
  * `500 Internal Server Error` – Erro interno no servidor.

-----

### 6\. GET /transacoes/resumo – Obter resumo financeiro

**Descrição:** Retorna um resumo com o total de receitas, despesas, saldo e os valores totais para cada categoria.

**Método HTTP:** `GET`

**Endpoint:** `/transacoes/resumo`

**Códigos de resposta:**

  * `200 OK` – Resumo financeiro retornado com sucesso.
  * `500 Internal Server Error` – Erro interno ao calcular o resumo.

**Corpo da resposta (JSON):**

```json
{
  "receitasPorCategoria": {
    "SALARIO": 5000.00,
    "FREELANCER": 800.00
  },
  "despesasPorCategoria": {
    "ALIMENTACAO": 450.50,
    "TRANSPORTE": 150.00,
    "LAZER": 200.00
  },
  "saldoTotal": 4999.50,
  "totalReceitas": 5800.00,
  "totalDespesas": 800.50
}
```
### Desenvolvedora 
Érika Santana Alves AQ3022722

### Vídeo de demonstração da parte do usuário 

https://drive.google.com/file/d/10uPLpPkj3oVQtvGfbe14KxuCYqGOCFvv/view?usp=drive_link

### Link do Front
https://github.com/Erika-Santana/FinancesFront
