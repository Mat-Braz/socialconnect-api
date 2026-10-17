# SocialConnect

Sistema de gestão para instituições sociais, com API REST em Spring Boot e interface web em React e TypeScript.

Projeto desenvolvido para a disciplina **Tópicos Especiais em Sistemas para Internet III**.

## Tecnologias

### Backend

- Java 21
- Spring Boot 4.1.1
- Spring Data JPA
- Bean Validation
- H2 para desenvolvimento local
- Flyway
- SpringDoc OpenAPI / Swagger UI
- Maven

### Frontend

- React
- TypeScript
- Vite
- Axios
- CSS responsivo

## Funcionalidades

### API REST

- Gerenciamento de beneficiários, doadores, doações e produtos.
- Cadastro, consulta, atualização e exclusão de beneficiários.
- Listagem paginada de beneficiários com filtros por nome e CPF.
- Validação dos dados de entrada.
- Tratamento de erros com respostas estruturadas.
- Documentação interativa pelo Swagger UI.
- CORS configurado para integração com o frontend local.

### Interface web

- Listagem paginada de beneficiários.
- Cadastro com nome, CPF, telefone e endereço.
- Validação de campos obrigatórios.
- Remoção da pontuação do CPF antes do envio.
- Mensagens de sucesso, erro e CPF duplicado.
- Retorno à listagem após o cadastro.
- Estados de carregamento e lista vazia.
- Opção de tentar novamente quando a API estiver indisponível.

## Estrutura do projeto

```text
socialconnect-api/
├── README.md
├── socialconnect-api/
│   ├── .mvn/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   └── resources/
│   │   └── test/
│   ├── mvnw
│   ├── mvnw.cmd
│   └── pom.xml
└── socialconnect-front/
    ├── src/
    │   ├── components/
    │   ├── services/
    │   ├── types/
    │   ├── App.tsx
    │   ├── main.tsx
    │   └── styles.css
    ├── .env.example
    ├── package.json
    ├── package-lock.json
    └── vite.config.ts
```

## Pré-requisitos

- JDK 21.
- Maven 3.9 ou superior, ou Maven Wrapper incluído no backend.
- Node.js 22.12 ou superior.
- npm.

## Como executar

### 1. Clonar o repositório

```bash
git clone https://github.com/Mat-Braz/socialconnect-api.git
cd socialconnect-api
```

### 2. Iniciar o backend

No primeiro terminal, a partir da raiz do repositório:

```bash
cd socialconnect-api
mvn spring-boot:run
```

Alternativa usando o Maven Wrapper no Linux/macOS:

```bash
chmod +x mvnw
./mvnw spring-boot:run
```

No Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

A API estará disponível em:

```text
http://localhost:8080
```

### 3. Iniciar o frontend

Em outro terminal, a partir da raiz do repositório:

```bash
cd socialconnect-front
npm ci
npm run dev
```

Acesse:

```text
http://localhost:5173
```

Também é possível acessar por `http://127.0.0.1:5173`.

Mantenha os dois terminais em execução durante o uso da aplicação.

## Configuração da integração

O frontend utiliza a URL padrão:

```text
http://localhost:8080/api/v1
```

Para alterar essa URL, crie um arquivo `.env.local` na pasta do frontend:

```env
VITE_API_URL=http://localhost:8080/api/v1
```

Reinicie o frontend após alterar as variáveis de ambiente.

O backend permite requisições das seguintes origens:

- `http://localhost:5173`
- `http://127.0.0.1:5173`

Caso altere o endereço ou a porta do frontend, atualize também a classe `CorsConfig.java`.

## Endpoints de beneficiários

| Método | Endpoint | Descrição |
| --- | --- | --- |
| GET | `/api/v1/beneficiarios` | Listar com paginação e filtros |
| GET | `/api/v1/beneficiarios/{id}` | Consultar por ID |
| POST | `/api/v1/beneficiarios` | Cadastrar |
| PUT | `/api/v1/beneficiarios/{id}` | Atualizar todos os dados |
| PATCH | `/api/v1/beneficiarios/{id}` | Atualizar parcialmente |
| DELETE | `/api/v1/beneficiarios/{id}` | Excluir |

Exemplo de consulta paginada:

```http
GET /api/v1/beneficiarios?page=0&size=10
```

Exemplo de corpo para cadastro:

```json
{
  "nome": "Pessoa de Exemplo",
  "cpf": "52998224725",
  "telefone": "11999990000",
  "endereco": "Rua de Exemplo, 100"
}
```

O frontend exige o preenchimento dos quatro campos. O backend gera automaticamente o identificador e a data de cadastro.

## Documentação da API

Com o backend em execução, acesse:

- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/api-docs

## Testes

Na pasta do backend:

```bash
mvn test
```

Os testes padrão utilizam H2 e não exigem Docker.

A cobertura inclui:

- Inicialização da aplicação.
- Serviço e endpoints de doações.
- Cadastro e listagem de beneficiários.
- Validação de dados.
- CPF duplicado.
- Listagem vazia.
- Permissões e restrições de CORS.

## Build

### Backend

Na pasta do backend:

```bash
mvn clean package
```

Para executar o JAR gerado:

```bash
java -jar target/api-0.0.1-SNAPSHOT.jar
```

### Frontend

Na pasta do frontend:

```bash
npm run build
```

O build será gerado na pasta `dist/`.

Para conferir o resultado localmente:

```bash
npm run preview
```

## Banco de dados

O ambiente de desenvolvimento utiliza H2 em memória.

Os dados são temporários e são apagados ao encerrar ou reiniciar a aplicação.

O projeto contém dependências para PostgreSQL, mas sua utilização exige configurar a conexão e revisar a estratégia de criação e migração do banco.

## Observações

- A validação atual do CPF verifica o tamanho, mas não calcula os dígitos verificadores.
- As telas implementadas nesta etapa atendem à listagem e ao cadastro de beneficiários solicitados na aula 09.
- Material UI, React Hook Form e Zod fazem parte da etapa seguinte da disciplina.

## Autor

Desenvolvido por **Mateus Braz Vieira**.

GitHub: [Mat-Braz](https://github.com/Mat-Braz)