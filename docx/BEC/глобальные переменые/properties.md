.properties
```
db.host - хост подключения к основной бд
db.user - имя пользователя подключения к основной бд
db.password - пароль пользователя подключения к основной бд
url.config.back - путь хранения конфигов команд физический
url.static - путь до статических файлов физический
token.secret - секретный ключ для создание JWT токенов
```
 
application.properties
```
    spring.mail.host
    spring.mail.port
    spring.mail.username
    spring.mail.password
    spring.mail.protocol
    spring.mail.transport.protocol
    spring.mail.properties.mail.smtp.auth
    spring.mail.properties.mail.smtp.starttls.enable
    spring.resources.add-mappings=true - для загрузки статичных файлов
    spring.resources.static-locations=file:/var/www/static,classpath:static url статичных файлов
```
 