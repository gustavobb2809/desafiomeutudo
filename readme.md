# Desafio técnico Meu tudo.

## API - Meu Tudo Bancario

##Passo-a-passo

- Rodar docker-compose.yml (docker-compose  up -d --build) que está dentro da pasta do projeto (Instalar o banco de dados MySql)
- Compilar o projeto com o maven e java 11 ( mvn clean install -DskipTests )
- Subir o projeto localmente com mvn spring-boot:run
- Quando subir o projeto pela primeira vez, ele vai rodar as migration (src/resources/db/migration)
- Para acessar o swagger-ui: http://localhost:8080/swagger-ui/index.html

## Usuários para teste

1. { "email": "gustavo.test@test.com", "password": "12345678" } | conta: ac-20221 e agência: ag-20221
2. { "email": "joao.test@test.com", "password": "12345678" } | conta: ac-20222 e agência: ag-20222

### Diagrama Entidade Relacionamento

![image](https://i.ibb.co/VMrm3wB/meutudo.png)


by Gustavo Barbosa Barros