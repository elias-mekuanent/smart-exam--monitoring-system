```                                                                                                                                                   
 ,---.                            ,--.   ,------.                            ,--.   ,--.               ,--.  ,--.                ,--.                
'   .-',--,--,--. ,--,--.,--.--.,-'  '-. |  .---',--.  ,--.,--,--.,--,--,--. |   `.'   | ,---. ,--,--, `--',-'  '-. ,---. ,--.--.`--',--,--,  ,---.  
`.  `-.|        |' ,-.  ||  .--''-.  .-' |  `--,  \  `'  /' ,-.  ||        | |  |'.'|  || .-. ||      \,--.'-.  .-'| .-. ||  .--',--.|      \| .-. | 
.-'    |  |  |  |\ '-'  ||  |     |  |   |  `---. /  /.  \\ '-'  ||  |  |  | |  |   |  |' '-' '|  ||  ||  |  |  |  ' '-' '|  |   |  ||  ||  |' '-' ' 
`-----'`--`--`--' `--`--'`--'     `--'   `------''--'  '--'`--`--'`--`--`--' `--'   `--' `---' `--''--'`--'  `--'   `---' `--'   `--'`--''--'.`-  /  
                                                                                                                                             `---' 
```
[![BUILD](https://github.com/senpare/smart-exam-monitoring/actions/workflows/maven.yml/badge.svg)](https://github.com/senpare/smart-exam-monitoring/actions/workflows/maven.yml/) [![BUILD](https://github.com/senpare/todo-microservices/actions/workflows/docker.yml/badge.svg)](https://github.com/senpare/todo-microservices/actions/workflows/docker.yml/) 


## Requirement
1. You need to have Docker installed on your machine.
2. Clone this repo. Because this is a private repo, you will be prompted to enter your username and password ([Use your token as a password](https://docs.github.com/en/enterprise-server@3.4/authentication/keeping-your-account-and-data-secure/creating-a-personal-access-token)).
    ```bash
   git clone https://github.com/senpare/Smart-Exam-Monitoring
   ```
# Setup

There two main steps to setup Smart-Exam-Monitoring web application
1. Start Keycloak authorization server
   1. ~~Go to keycloack directory~~ (**This steps is no longer required as keycloak is now moved to the main docker-compose.yml file**)
       ```bash
        cd {your-path}/Smart-Exam-Monitoring/keycloak
       ```
   2. ~~Start authorization server using docker command~~
       ```bash
        docker-compose up
       ```
   3. Keycloak server will start running http://localhost:8080/
   4. You can use the below credentials to access the admin console
      1. `Username`: admin@admin.com
      2. `Password`: admin
2. Start the main web application
   1. Go to root directory of the project
       ```bash
       cd {your-path}/Smart-Exam-Monitoring
       ```
   2. Run all services with a single command
      ```bash
       docker-compose up
      ```
   3. The api-gateway will start running at http://localhost:8081/ (**Note: the port for the api gateway may change so make sure to consult this readme file time to time)
     
# Developer Guide
  - Underlying services can not be accessed directly. All api calls to downstream services are proxied through the api-gateway.
  - Use `api/v1/{service-name}/{entity}` pattern e.g. `api/v1/user-service/users` to fetch all users from the api (**Note this is just an example URL).
  - You can check running services by going to http://localhost:8081/eureka/web url.
  - If you make any pull request, make sure the CI successfully  passes (check the [Actions](https://github.com/senpare/Smart-Exam-Monitoring/actions) tab).
  - If you make any changes to `docker-compose.yml` file or any of `Dockerfile`, you have to make sure `docker.yml` workflow passes. You can trigger this workflow manually by clicking the `Run workflow` button on the [Actions](https://github.com/senpare/Todo-Microservices/actions) tab.
  - After pulling changes from the remote repo, you will need to run `docker-compose up --build` to rebuild the images.
  - You can access pgAdmin (similar to MySQL workbench but for Postgres) using http://localhost:5050/
  - You can access [maildev](https://github.com/maildev/maildev) (a local SMTP server) using http://localhost:1080/. You will need to get confirmation code after registering users.

# Addresses
## API documentations
| Service                  | Local Address                         | Cloud Address                             |
|--------------------------|---------------------------------------|-------------------------------------------|
| api-gateway              | http://localhost:8081/swagger-ui.html | http://137.184.30.19:8081/swagger-ui.html |
| auth-service             | http://localhost:8080/swagger-ui.html | http://137.184.30.19:8080/swagger-ui.html |
| audio-processing-service | http://localhost:8082/swagger-ui.html | http://137.184.30.19:8082/swagger-ui.html |
| email-service            | http://localhost:8083/swagger-ui.htm  |                                           |
| exam-service             | http://localhost:8084/swagger-ui.html | http://137.184.30.19:8084/swagger-ui.html |
| payment-service          | http://localhost:8085/swagger-ui.html | http://137.184.30.19:8085/swagger-ui.html |
| test-service             | http://localhost:8086/swagger-ui.html |                                           |
| React app                | http://localhost:3030                 | http://137.184.30.19:3030      

## Tools

| Service                  | Address                               |
|--------------------------|---------------------------------------|
| api-gateway              | http://localhost:8081                 |
| eureka-server            | http://localhost:8761                 |
| keycloak                 | http://localhost:8080                 |
| frontend                 | http://localhost:3030                 |

* **Note**: Requests to underlying services are proxied through the api-gateway.So, all requests should be made to the api-gateway. e.g. `http://localhost:8081/api/v1/payment-service/payments`

# Troubleshoot
1. After updating your branch, you have to rebuild each image so that docker will pick up new changes   
   ```bash
   docker compose up --build
   ```
2. Databases created automatically if no database exist at the start of the postgres container.
   If a new service is added, you have to clear your postgres volume so that docker will create all the database from scratch
   1. Remove `postgres` container 
      ```
       docker rm -f postgres
      ```
   2. Remove postgres volume. It is usually a volume with the name `smart-exam-monitoring_postgres` but you can check it using 
      ```bash
       docker volume ls
      ```
      then
       ```bash
       docker volume rm smart-exam-monitoring_postgres
      ```
   3. Then recreate the container
      ```bash
      docker compose up postgres
      ```
      
