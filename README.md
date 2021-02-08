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

#### 3 - Integração com ELK
A aplicação gera eventos no ELK para monitoração a partir do filebeat, para permitir que os logs sejam capturados pelo ELK, crie uma pasta chamada logs em C:\ ou altere o path na classe logger para a pasta em que se deseja ter os logs gerados.

Caso não haja um cluster ELK de pé, no CMD (Terminal) acesse a pasta ELK do projeto e rode os comandos (recomendo colocar o ELK de pé no Linux Ubuntu):
>docker-compose up -d

Se o ELK for levantado no Linux, rode também o comando.
>sysctl -w vm.max_map_count=262144

Assim um conjunto ELK de único nó será estabelecido na máquina alvo.

Com um cluster ELK estabelecido para coletar os eventos da aplicação, acesse a pasta filebeat e edite o arquivo filebeat.yml para que os IPs correspondam aos IPs do cluster ELK. Após editar, abra o CMD (Terminal) e rode o comando:

>.\filebeat.exe -c .\filebeat.yml -e

Com o filebeat enviando os eventos e a aplicação rodando, acesse o kibana no browser pelo IP:5601 e defina um index pattern com filebeat*
Guia ->https://www.elastic.co/guide/en/kibana/6.8/tutorial-define-index.html

Assim a aplicação enviará os eventos conforme abaixo
![alt text](https://imgur.com/8M77NwF)

#### 4- abra o eclipse, vá até 
File> Import> GIT> Projects from Git> Next>

Clone URI> Next>
  
>URI: https://github.com/manoelsmotoso/Web-Service-REST-java.git

Next> Next >Fisish

>com o projeto importado no eclipse, clique com o botão direito sobre o mesmo e selecione.

 Run as> Run on server> Tomcat v8.5

>depois 

'Download and install'>

>selecione o diretorio onde sera instalado o Servidor Tomcat.

### 5- rode o projeto
>clique com o botao direito dp mouse sobre o projeto e vá até,

Run as> Run on server>

>selecione o servidor Tomcat criado no passo 1, e clique em 'Finish', depois abra o navegador em http://localhost:8080/rest-api/

### 6 - criar, listar e deletar clientes
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


#### 7- Testes Unitários
Os testes unitários foram produzidos para os métodos no PlanetDao, para rodá-los, clique com o botão direito no projeto -> Run As -> Junit Test.

Os testes foram feitos usando um Fake MongoDB, de modo que para rodá-los não depende de conexão com o servidor MongoDB



