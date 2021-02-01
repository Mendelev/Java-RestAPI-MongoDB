# Web Service REST java
>O projeto de testes para experimentar o funcionamento da integração de Java com MongoDB.</p>

#### Tecnologis/libs utilizadas:
* Java
* Jersey
* JSON
* XML
* Maven
* MySql
* SQL
* Tomcat 8
* GIT
* Docker

# setup do projeto
## banco de dados
#### 1 - entrar no console do MongoDB.
Suba o mongoDB com o Docker utilizando o seguinte comando no terminal (ou CMD)

>docker run --name mongo-test -p 8082:27017 -e ME_CONFIG_MONGODB_ADMINUSERNAME="root" -e ME_CONFIG_MONGODB_ADMINPASSWORD="example" -d mongo:4.4.3-bionic

#### 2 - criar um usuário no MongoDB
Acesse o container com o comando

>docker exec -it mongo-test /bin/sh

Acesse o mongo com o comando

>mongo

Crie um usuário

>db.createUser({
    user: 'sample_user',
    pwd: 'pass',
    roles: [
        { role: 'readWrite', db: 'test' }
    ]
})
```

#### 3- abra o eclipse, vá até 
File> Import> GIT> Projects from Git> Next>

Clone URI> Next>
  
>URI: https://github.com/manoelsmotoso/Web-Service-REST-java.git

Next> Next >Fisish

>com o projeto importado no eclipse, clique com o botão direito sobre o mesmo e selecione.

 Run as> Run on server> Tomcat v8.5

>depois 

'Download and install'>

>selecione o diretorio onde sera instalado o Servidor Tomcat.

### 4- rode o projeto
>clique com o botao direito dp mouse sobre o projeto e vá até,

Run as> Run on server>

>selecione o servidor Tomcat criado no passo 1, e clique em 'Finish', depois abra o navegador em http://localhost:8080/rest-api/

### 5 - criar, listar e deletar clientes
>recomendo usar o [Postman](https://www.getpostman.com "postman") para testar essa api.
#### CRUD

>#### criar
#method -  uri

POST - http://localhost:8080/rest-api
```json
#headers 
{ "Content-type": "application/json" } }

#body
{
"id":"",
"name":"",
"climate":"",
"terrain":""
}
```
>#### atualizar 
method -  uri


PUT -  http://localhost:8080/rest-api/{id}
```json
#headers 
{ "Content-type": "application/json" }

#body
{
"id":"",
"name":"",
"climate":"",
"terrain":""
}
```

>#### listar todos
method -  uri

GET - http://localhost:8080/rest-api

>#### buscar por id 
method -  uri

GET - http://localhost:8080/rest-api/{id}

>#### buscar por nome 
method -  uri

GET - http://localhost:8080/rest-api/{name}

>#### deletar
method -  uri

DELETE - http://localhost:8080/rest-api/{id}


#### 6- Testes Unitários
Os testes unitários foram produzidos para os métodos no PlanetDao, para rodá-los, clique com o botão direito no projeto -> Run As -> Junit Test.

Os testes foram feitos usando um Fake MongoDB, de modo que para rodá-los não depende de conexão com o servidor MongoDB

