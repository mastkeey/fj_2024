## Изменения:
Добавлены следующие Ендпоинты:
- POST: /api/v1/registration
- POST: /api/v1/auth
- POST: /api/v1/change-pass
- POST: /api/v1/logout
- GET: /api/v1/user
- GET: /api/v1/admin

## 1. POST: /api/v1/registration

Регистрирует нового пользователя и присваивает ему роль USER, не требует аутентификации

Тело запроса:
```json
{
    "username" : "username",
    "password" : "username"
}
```

## 2. POST: /api/v1/auth

Аутентифицирует пользователя и возвращает JWT Access token, который требуется поместить в 
Header Authorization, для доступа к ресурсам, где требуется аутентификация:

```text
Authorization: Bearer <token>
```

По дефолту TTl Токена составляет 10 минут, но если передать Boolean параметр запроса 
rememberMe равный true, то TTL Токена будет 30 дней

Тело запроса:
```json
{
    "username" : "username",
    "password" : "username"
}
```
```text
rememberMe: true
```


## 3. POST: /api/v1/change-pass
Смена пароля для пользователя, не требует аутентификации

Тело запроса:
```json
{
    "username" : "username",
    "newPassword" : "newPassword",
    "confirmPassword" : "confirmPassword",
    "verificationCode" : "verificationCode"
}
```

VerificationCode всегда равен ```"0000"```

## 4. POST: /api/v1/logout

Logout, требует аутентификации и наличия Токена в Header'ах

## 5. GET: /api/v1/user
User Data, требует аутентификации и наличии роли USER или ADMIN у пользователя

Тело ответа:
```text
userData
```

## 6. GET: /api/v1/admin
Admin Data, требует аутентификации и наличии роли ADMIN у пользователя
Тело ответа:
```text
adminData
```

Также, при старте приложения, автоматически создается админская учетка с ролью ADMIN с кредами:
```text
username: admin
password: admin
```


Остальную часть приложения новые изменения никак не коснулись.








