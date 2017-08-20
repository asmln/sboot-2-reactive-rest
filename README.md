# Тестовая задача.<br />
<br />
Test:<br />
```sh
mvnw test
```
Run:<br />
```sh
mvnw spring-boot:run
```
порт: 9090 можно изменить в application.properties или параметром -Dserver.port={port}<br />

maven, spring-boot-starter-webflux, netty, spring-boot-starter-test, reactor-test<br />
<br />
MockMvc не подружился с reactive (см test/java/com/sbrw/aut/AuthApplicationMMIntegrationTests.java)
или просто я не знаю как это сделать.<br />
<br />
# API:<br />

<b>POST /register</b><br />

in:<br />
```
{
  email: "уникальный идентификатор пользователя",
  password: "пароль"
}
```

out ok:<br />
```
{
  success: true,
  message: "",
  token: "токен подтверждения, уникальная случайная строка"
}
```

out error:<br />
```
{
  success: false,
  message: "описание ошибки",
  error: "error_code"
}
```

<b>POST /confirm</b><br />

in:
```
{
  token: "confirmation token"
}
```

out ок:
```
{
  success: true,
  message: "",
  user: {
    id: числовой уникальный идентификатор пользователя,
    email: "уникальный идентификатор пользователя",
    created: unix timestamp (msec)
  }
}
```

out error:
```
{
  success: false,
  message: "описание ошибки",
  error: "error_code"
}
```

<b>POST /login</b><br />

in:
```
{
  email: "уникальный идентификатор пользователя",
  password: "пароль"
}
```

out ok:
```
{
  success: true,
  message: "",
  user: {
    id: числовой уникальный идентификатор пользователя,
    email: "уникальный идентификатор пользователя",
    created: unix timestamp (msec)
  }
}
```

out error:
```
{
  success: false,
  message: "описание ошибки",
  error: "error_code"
}
```
