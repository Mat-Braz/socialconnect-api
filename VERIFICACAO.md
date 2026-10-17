# Verificação da entrega — Aula 09

Data: 17/10/2026.

## Resultados

- Backend: compilação e execução dos 12 testes concluídas, sem falhas, erros ou testes ignorados.
  - 1 teste de inicialização do contexto.
  - 2 testes existentes do serviço de doações.
  - 3 testes existentes do controller de doações, agora usando H2 e MockMvc configurado.
  - 6 novos testes: cadastro/listagem paginada, CPF duplicado, validação de entrada,
    listagem vazia, preflight CORS para POST JSON e rejeição de origem não permitida.
- Frontend: `npm run build` passou (TypeScript estrito + build Vite).
- Backend: `mvn package -DskipTests` passou após a execução bem-sucedida dos testes.
- JAR gerado iniciou na porta 8080. Foram confirmados POST 201 e página 2 com 1 de 11 registros.

## Conferência pelo navegador

Com o frontend em `http://127.0.0.1:5173` e o backend real:

- Estado vazio exibido corretamente.
- Envio do formulário vazio apresentou erros nos quatro campos.
- Cadastro com dados fictícios e CPF com pontuação foi enviado com sucesso.
- Mensagem de sucesso e retorno à listagem confirmados; novo registro apareceu na tabela.
- Novo envio do mesmo CPF exibiu mensagem geral e erro no campo CPF.
- API desligada: mensagem de conexão e botão “Tentar novamente” confirmados.
- API reiniciada: “Tentar novamente” recuperou a listagem.
- Com 11 registros fictícios, os botões Próxima e Anterior navegaram entre as duas páginas.
- A segunda página mostrou apenas o 11º registro.
- Aparência da listagem inspecionada e registrada em `preview-listagem.png`.

## Ambiente e limites

Validação executada em Linux com Java 25.0.4.1, Maven 3.9.11, Node 22.23.1 e npm 10.9.8.
O projeto mantém o nível Java 21 configurado no `pom.xml`; não houve execução em uma JVM 21 nesta conferência.
A instalação npm foi fixada em `package-lock.json` para reprodução com `npm ci`.

Os testes usam H2. Não foi executada uma bateria com PostgreSQL/Testcontainers.
A configuração do Maven Wrapper foi restaurada; os builds foram feitos com o Maven instalado.

Os dados usados nas verificações são fictícios e temporários, no H2 em memória;
nenhum cadastro foi incluído como carga inicial na entrega.
O ZIP contém os fontes completos e o build do frontend, sem caches, `node_modules` ou `target`.
