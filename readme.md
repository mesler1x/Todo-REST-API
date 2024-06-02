# TODO REST API
### Шаги для запуска проекта локально

1) ```git clone https://github.com/mesler1x/Todo-REST-API.git```

2) ```cd Todo-REST-API```

3) ```docker build -t todo-backend .```

4) ```docker compose up -d```

5) Для просмотра документации перейдите по енд поинту 
```http://localhost:8080/swagger-ui/index.html```. 
6) Авторизуйтесь под стартовым пользователем: username = ``sample``, password = ``sample``

### Если не работает Docker

1) В файл /etc/docker/daemon.json вставить:
```
{
    "registry-mirrors": ["https://mirror.gcr.io", "https://daocloud.io", "https://c.163.com/", "https://registry.docker-cn.com"]
}
```
2) ```sudo systemctl restart docker```

### Описание стека
* TestContainers
* Spring DATA JPA
* Spring Boot
* Spring Security
* PostgreSql
* Docker
* OpenAPI

### Результаты интеграционных тестов
![img.png](img.png)

