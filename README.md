
<h1 align="center">Order parser</h1>
<p align="center">
 <b>Desafio Luiza Labs</b></br>
</p>

# Sumário

- [Requisitos](#requisitos)
- [Variaveis de ambiente](#variaveis-de-ambiente)
- [Executar app](#executar-aplicação-via-docker)
- [Testes](#testes)
- [Coverage](#coverage)


# Requisitos

[Java 21](https://www.oracle.com/br/java/technologies/downloads/#java21)

[Docker 26.0.2](https://docs.docker.com/engine/release-notes/26.0/#2602)

[Docker-Compose 2.27.0](https://docs.docker.com/compose/release-notes/#2270)

[Mysql 5.7](https://dev.mysql.com/downloads/windows/installer/5.7.html)

---
# Variaveis de ambiente
<table>
  <thead>
    <tr>
      <th>Env</th>
      <th>Valor padrão</th> 
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>MYSQL_URL</td>
      <td>jdbc:mysql://localhost:3306/dbparser?allowPublicKeyRetrieval=true&useSSL=false</td> 
    </tr>
    <tr>
      <td>MYSQL_USERNAME</td>
      <td>admin</td> 
    </tr>
    <tr>
      <td>MYSQL_PASSWORD</td>
      <td>admin</td> 
    </tr>
  </tbody>
</table> 

---
# Executar aplicação via Docker

```
neste modo antes é necessario configurar as envs do ambiente
docker build . -t order-parser
docker run -p 8080:8080 order-parser
```
# Executar aplicação via Docker compose
```
docker compose up -d
```

---
# Testes
```
./mvnw test
```

# Coverage
```
./mvnw package

./target/site/jacoco/index.html
```