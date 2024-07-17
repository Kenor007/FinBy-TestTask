# **Тестовое задание для FinBy**

## Описание

### [Задание](./documentation/task.pdf)

## Используемые технологии

* **Gradle**
* **Spring Boot 3.2.5**
* **Java 17**
* **Packaging Jar**
* **H2 database**
* **logs**
* **Lombok**
* **Liquibase**
* **MapStruct**
* **Swagger**

## Как запустить проект

* `git clone https://github.com/Kenor007/FinBy-TestTask.git`
* Открыть проект с помощью IDE

## Сборка и запуск проекта

* `./gradlew clean build`
* Перейти в директорию `build/libs` и запустить проект командной строкой `java -jar ProductCardTestTask-1.0.jar`
* Открыть в браузере Swagger `http://localhost:8092/swagger-ui/index.html` для ознакомления со всеми эндпоинтами

## Особенности

При запуске приложения происходит заполнение базы данных тестовыми данными (посмотреть данные можно через `h2-console`
по
адресу `http://localhost:8092/h2-console`). Приложение запускается на порту `8092`. Реализована валидация входных
данных. В
случае не валидных данных в ответе указаны сообщение с описанием исключительной ситуации, HttpStatus, описание и
timestamp.
При создании либо изменении в БД изменяется поле "Описание" в соответствии с примером в задании. Так же происходит
загрузка фото по адресу `"imageUrl"` и сохраняется с именем `"photoName"` в папке `resources/images/source_image`, далее
происходит запрос к API(`htps://www.remove.bg/api`) с исходной фотографией для удаления фона с изображения, изменённое
фото сохраняется в папке `resources/images/processed_image` и добавляется в карточку товара.

### [Коллекция Postman](./documentation/products_postman_collection.json)