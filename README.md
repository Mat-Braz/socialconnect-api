# Social Connect API

API RESTful desenvolvida para otimizar a gestão administrativa de instituições sociais, permitindo o registo e o controlo eficiente de beneficiários, doadores, doações e inventário de produtos.

## 🚀 Tecnologias Utilizadas

* **Java 21**
* **Spring Boot** (Web, Data JPA, Validation)
* **Flyway** (Gestão de migrações de base de dados)
* **PostgreSQL** (Base de dados principal) / **H2 Database** (Base de dados em memória para testes)
* **Springdoc OpenAPI (Swagger UI)** para documentação interativa
* **Lombok** para redução de código boilerplate
* **JUnit 5 & Testcontainers** para testes de integração

## ⚙️ Arquitetura e Módulos

O projeto segue uma arquitetura em camadas bem definida (`Controller`, `Service`, `Repository`, `DTO`, `Model`), dividida nos seguintes domínios principais:

1. **Beneficiários**: Gestão de dados pessoais, moradas e registo de vulnerabilidade social.
2. **Doadores**: Controlo de pessoas físicas e jurídicas com validações customizadas de CPF e contactos.
3. **Doações**: Registo e categorização de entradas de apoio (alimentos, vestuário, valores financeiros).
4. **Produtos (Inventário)**: Controlo de stock atual, stock mínimo, unidades de medida e categorias via enums.

## 📝 Documentação da API (Swagger)

Com a aplicação a correr, a documentação interativa dos endpoints pode ser acedida diretamente no navegador através do link:

```text
http://localhost:8080/swagger-ui.html