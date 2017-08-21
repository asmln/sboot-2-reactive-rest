# Тестовая задача.<br />
<br />
Test:<br />
mvnw test<br />

Run:<br />
mvnw spring-boot:run<br />

порт: 9090, можно изменить в application.properties или параметром -Dserver.port={port}<br />

maven, spring-boot-starter-webflux, netty, spring-boot-starter-test, reactor-test<br />
<br />
MockMvc не подружился с reactive (см test/java/com/sbrw/aut/AuthApplicationMMIntegrationTests.java)
или просто я не понял как это сделать.<br />
Использовал для тестов WebTestClient, mockito.<br />
<br />
Пример обращения к API:
```
curl -H "Content-Type: application/json" -X POST -d '{"email":"test@email.com","password":"123"}' http://{local_ip}:9090/register
```
Особенности работы API:<br />
Пользователь считается зарегистрированным сразу после вызова <b>/register</b>. Можно сразу вызывать <b>/login</b>.<br />
Если вызвать <b>/register</b> повторно с теми же данными, что и в первый раз - будет получен новый токен подтверждения, старый "испортится". Если при повторном вызове будет другой пароль, то будет сообщение о том, что пользователь уже существует.<br />
Вызов <b>/login</b> выполняет аутентификацию (это по условию задачи). Авторизация не выполняется, ролей и прав нет, закрытых сервисов нет, security не добавлял.

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
