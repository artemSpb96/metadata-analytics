# metadata-analytics

##metadata-api
####бд
Для работы metadata-api необходим [**postgresql**](https://www.postgresql.org/).
Параметры подключения можно указать в _application.properties_ или 
использовать [дефолтные](https://github.com/artemSpb96/metadata-analytics/blob/master/metadata-api/src/main/resources/application.properties).

Для инициализации бд нужно выполнить [скрипт](https://github.com/artemSpb96/metadata-analytics/blob/master/metadata-api/src/main/resources/static/metadata-ddl.sql) 

####api
Приложение можно запустить с помощью spring boot
```
cd metadata-api && mvn spring-boot:run
```

Документация API доступна `http://localhost:8080/swagger-ui.html`

##analytics-api
Приложение можно запустить с помощью spring boot
```
cd analytics-api && mvn spring-boot:run
```

Документация API доступна `http://localhost:8081/swagger-ui.html`

##metadata-collector
Для сбора метаданных на файловой системе, необходимо предварительно 
сохранить информацию о ней. Сделать это можно через metadata-api 
```
curl -X POST "http://localhost:8080/v{ver}/filesystems" -H "accept: */*" \
-H "Content-Type: application/json" -d "{\"name\":\"local hdfs\",\"url\":\"hdfs://localhost:9000\"}"

```

Запустить коллектор можно так:
```
cd metadata-collector
mvn clean install 
java -jar target/metadata-collector-0.0.1-SNAPSHOT.jar --filesystemId=1
```
