# Mini File Service

## Description
<p>&nbsp; This is a mini file service application.</p>

<p>&nbsp; Project Requirements are defined here 'minifileservice/docs/Mini File Service Design.doc'.</p>

## How to Setup Application
<p>&nbsp; First you need to install postgresql for database.</p>
<p>&nbsp; You should set the username as 'postgres' and password as '12345'. If not, you should update the datasource username and password according to your postgresql. You can do it from src/main/resources/application.properties.</p>

<p>&nbsp; After the install, create a database named 'mini_file_service_db'</p>

<p>&nbsp; Then you can clone the project.</p>
<p>&nbsp; After the clone, git bash to the project folder and write the following</p>
- mvn clean install <br/>
- cd target <br/>
- java -jar minifileservice-0.0.1-SNAPSHOT.jar <br/><br/>

That's it. Now you can [click this link](http://localhost:8080/swagger-ui/) to open Swagger UI and test the project or you can test the project on Postman.

## Notes
<p>&nbsp; During the initialition of the project two user are created. These are 'admin' and 'user'. Here is the details of the users: </p>
- username: admin              - username: user<br/>
- password: admin              - password: user<br/>
- user role: ADMIN_USER        - user role: REGULAR_USER<br/>

## Test Application On Swagger UI
<p>&nbsp; Open [this link](http://localhost:8080/swagger-ui/) to test the project on Swagger UI.</p>

- First send a login request and copy the token in the response.<br/>
- username : admin and password: admin
- username : user and password: user
<br/>
<img src = "/docs/images/Swagger1.png">
<br/><br/>

- Then click to the Authorize button and type 'Bearer ' then paste the token here (Bearer + space + token). When you click to the Authorize button swagger ui will be authorized and all other request will be runnuble.
<br/>
<img src = "/docs/images/Swagger2.png">
<br/><br/>
Now you can test all the methods.


## Test Application On Postman
<p>&nbsp; Get exported collection files from 'minifileservice/docs/postman collections' and import them in the postman. After the import please select LocalHost environment. Then you can start to test application.</p>

- First run the 'Authenticate' request with the username and password. This request sets 'token' environment. After this request all other request will be runnuble.
<br/>
<img src = "/docs/images/PostmanAuth.png">
<br/><br/>

Now you can test all the methods. Some requests have path variable. You may need to update this path variables.


<br/>
<img src = "/docs/images/Postman3.png">
<br/><br/>

<br/>
<img src = "/docs/images/Postman1.png">
<br/><br/>


<br/>
<img src = "/docs/images/Postman2.png">
<br/><br/>


---

by [Hakan Kahraman](https://github.com/hakankhrmn)
