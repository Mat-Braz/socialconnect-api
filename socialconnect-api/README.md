# SocialConnect API

API RESTful para gestão de instituições sociais. Java 21, Spring Boot 4.1.1,
JPA e H2 em desenvolvimento.

```bash
mvn spring-boot:run
```

Sem Maven instalado, use `./mvnw spring-boot:run` ou `mvnw.cmd spring-boot:run`.

- API: http://localhost:8080/api/v1/beneficiarios
- Swagger: http://localhost:8080/swagger-ui.html
- Testes com H2, sem Docker: `mvn test`
- Gerar JAR: `mvn package`
- CORS permite `http://localhost:5173` e `http://127.0.0.1:5173`.

O banco é temporário; os dados são apagados ao reiniciar.
As instruções completas de integração com o frontend estão em `../README.md`.
