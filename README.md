# Тестовая задача.<br />
<br />
Test: mvnw test<br />
Run: mvnw spring-boot:run<br />
порт: 9090 можно изменить в application.properties или параметром -Dserver.port={port}<br />

maven, spring-boot-starter-webflux, netty, spring-boot-starter-test, reactor-test<br />
<br />
MockMvc не подружился с reactive (см test/java/com/sbrw/aut/AuthApplicationMMIntegrationTests.java)
или просто я не знаю как это сделать.<br />
<br />
# API:<br />

POST /register<br />

in:<br />
{
  email: "уникальный идентификатор пользователя",
  password: "пароль"
}<br />

out ok:<br />
{
  success: true,
  message: "",
  token: "токен подтверждения, уникальная случайная строка"
}<br />

out error:<br />
{
  success: false,
  message: "описание ошибки",
  error: "error_code"
}<br />

POST /confirm<br />

in:
{
  token: "confirmation token"
}<br />

out ок:
{
  success: true,
  message: "",
  user: {
    id: числовой уникальный идентификатор пользователя,
    email: "уникальный идентификатор пользователя",
    created: unix timestamp (msec)
  }
}<br />

out error:
{
  success: false,
  message: "описание ошибки",
  error: "error_code"
}<br />

POST /login<br />

in:
{
  email: "уникальный идентификатор пользователя",
  password: "пароль"
}<br />

out ok:
{
  success: true,
  message: "",
  user: {
    id: числовой уникальный идентификатор пользователя,
    email: "уникальный идентификатор пользователя",
    created: unix timestamp (msec)
  }
}<br />

out error:
{
  success: false,
  message: "описание ошибки",
  error: "error_code"
}<br />
