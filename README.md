# 📱 Backend — Assistência Técnica

API REST desenvolvida para gerenciamento de uma assistência técnica de celulares, responsável por centralizar clientes, aparelhos, ordens de serviço, vendas e controle financeiro.

O projeto foi desenvolvido com **Java e Spring Boot**, utilizando **PostgreSQL** para persistência de dados e **JWT + Spring Security** para autenticação e autorização.

## 🚀 Tecnologias

* Java 21
* Spring Boot
* Spring Security
* JWT
* JPA / Hibernate
* PostgreSQL
* Maven
* Docker

## ⚙️ Funcionalidades

* 🔐 Autenticação e autorização com JWT
* 👤 Cadastro e gerenciamento de clientes
* 📱 Cadastro e gerenciamento de aparelhos
* 🔧 Controle de ordens de serviço
* 🛠️ Acompanhamento do status dos serviços
* 💰 Controle de entradas e saídas financeiras
* 🛒 Gerenciamento de vendas
* 📊 Consulta de informações financeiras
* 🔒 Proteção de rotas com Spring Security

## 🔐 Autenticação

A API utiliza **JWT (JSON Web Token)** para autenticação.

Após realizar o login, o usuário recebe um token que deve ser enviado nas requisições protegidas:

```http
Authorization: Bearer <token>
```

As senhas são armazenadas utilizando técnicas de criptografia/hash, evitando seu armazenamento em texto puro.

## 🏗️ Estrutura

O projeto segue uma arquitetura organizada em camadas:

```text
src/
└── main/
    ├── controller/
    ├── service/
    ├── repository/
    ├── entity/
    ├── dto/
    ├── security/
    └── config/
```

Essa separação facilita a manutenção, organização e evolução da aplicação.

## ⚙️ Como executar

### 1. Clone o repositório

```bash
git clone <URL_DO_REPOSITORIO>
cd <NOME_DO_PROJETO>
```

### 2. Configure as variáveis de ambiente

Crie um arquivo `.env` ou configure as variáveis no ambiente de execução:

```env
DATABASE_URL=jdbc:postgresql://<HOST>:5432/<DATABASE>
DATABASE_USERNAME=<USERNAME>
DATABASE_PASSWORD=<PASSWORD>
JWT_SECRET=<SECRET>
```

> ⚠️ Nunca envie credenciais, senhas ou chaves JWT para o GitHub.

### 3. Execute a aplicação

Com Maven:

```bash
./mvnw spring-boot:run
```

Ou:

```bash
mvn spring-boot:run
```

A API estará disponível, por padrão, em:

```text
http://localhost:8080
```

## 🐳 Docker

O projeto também possui suporte para execução utilizando Docker.

```bash
docker build -t assistencia-backend .
docker run -p 8080:8080 assistencia-backend
```

## 🎯 Objetivo

Projeto desenvolvido para aplicar na prática conceitos de:

* Desenvolvimento de APIs REST
* Spring Boot
* Spring Security
* JWT
* Banco de dados relacional
* Arquitetura em camadas
* Docker
* Integração entre frontend e backend

## 👨‍💻 Desenvolvedor

**Leonardo Jermano**

Desenvolvedor Full Stack em formação, com foco em desenvolvimento web e construção de APIs.

**Stack:** Java • Spring Boot • Angular • React • Node.js • PostgreSQL • Docker • Git
