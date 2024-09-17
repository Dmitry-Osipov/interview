# REST и SOAP
## Оглавление
- [REST](#rest)
- [Плюсы REST](#плюсы-rest)
- [Минусы REST](#минусы-rest)
- [SOAP](#soap)
- [Плюсы SOAP](#плюсы-soap)
- [Минусы SOAP](#минусы-soap)
- [Сравнительная таблица](#сравнительная-таблица)
- [Пример REST](#пример-rest)
- [Пример SOAP](#пример-soap)
## REST
REST — это архитектурный стиль, используемый для создания распределенных систем, особенно веб-сервисов. Он основан на 
работе с ресурсами, где каждый ресурс идентифицируется уникальным URI, и взаимодействие происходит через стандартные
HTTP методы (GET, POST, PUT, DELETE и т.д.).

- Ресурсы: Все в системе представляется как ресурсы. Например, пользователь, заказ или продукт могут быть ресурсами.
- URI: Ресурсы идентифицируются через уникальные URI.
- Методы HTTP: Для работы с ресурсами REST использует стандартные методы HTTP:
  - GET: для получения ресурса.
  - POST: для создания нового ресурса.
  - PUT: для обновления ресурса.
  - DELETE: для удаления ресурса.
- Безсессионность: Взаимодействие между клиентом и сервером безсессионное, то есть каждая запрос содержит всю 
необходимую информацию для его обработки.
- Клиент-сервер: REST предполагает чёткое разделение между клиентом и сервером. Сервер предоставляет ресурсы, а клиент
их запрашивает.
- Кэширование: REST поддерживает кэширование, что позволяет значительно повысить производительность, минимизируя 
количество обращений к серверу.
## Плюсы REST
- Простота и легкость: REST использует стандартные протоколы, такие как HTTP, что упрощает его реализацию.
- Масштабируемость: Поскольку REST взаимодействия безсессионны, это делает его хорошо подходящим для масштабируемых
систем.
- Гибкость форматов: В отличие от SOAP, REST не ограничен использованием XML и поддерживает разные форматы данных,
такие как JSON, XML, YAML и другие.
- Поддержка кэширования: REST хорошо работает с кэшированием, что улучшает производительность.
- Легкая интеграция с вебом: REST естественно вписывается в архитектуру современных веб-приложений, так как работает 
поверх HTTP.
## Минусы REST
- Безсессионность: REST не сохраняет состояние между запросами. Это может быть недостатком для приложений, требующих 
работы с пользовательскими сессиями.
- Ограниченная безопасность: REST полагается на стандартные механизмы безопасности HTTP, такие как SSL/TLS. Если нужно
более сложное управление безопасностью, это потребует дополнительной настройки.
- Неполная поддержка стандартизации: REST не имеет строгого формата взаимодействия, что может привести к различным 
интерпретациям API и проблемам совместимости.
- Ограниченные возможности транзакций: В отличие от SOAP, REST не предоставляет встроенных возможностей для выполнения 
сложных транзакционных операций (например, ACID транзакций).
## SOAP
SOAP — это протокол для обмена структурированными сообщениями между системами в распределенной среде. В отличие от
REST, SOAP является более строгим стандартом с четко определенными правилами взаимодействия.

- XML: SOAP использует исключительно XML для форматирования сообщений. Сообщения SOAP представляют собой XML-документы,
которые включают в себя заголовки и тело сообщения.
- Транспортный протокол: SOAP может работать поверх различных протоколов, таких как HTTP, SMTP, TCP, но чаще всего
используется поверх HTTP.
- WS-Security: SOAP предоставляет расширенные возможности по безопасности через спецификацию WS-Security, что делает
его подходящим для корпоративных решений с высокими требованиями к безопасности.
- WSDL (Web Services Description Language): Это язык для описания веб-сервисов, которые взаимодействуют через SOAP.
WSDL позволяет описывать интерфейсы SOAP и параметры, которые они принимают и возвращают.
## Плюсы SOAP
- Формализм и стандартизация: SOAP имеет строгое соблюдение стандартов, таких как WSDL для описания интерфейсов,
что позволяет легко интегрировать системы.
- Поддержка сложных операций: SOAP поддерживает транзакции, оркестрацию процессов и другие сложные взаимодействия,
что делает его подходящим для корпоративных приложений.
- Расширенные возможности безопасности: SOAP поддерживает WS-Security, который включает в себя шифрование, подписи,
аутентификацию и другие механизмы безопасности на уровне сообщений.
- Независимость от транспортного протокола: SOAP может использоваться не только с HTTP, но и с другими транспортными
протоколами, такими как SMTP или TCP.
- Надежность передачи данных: SOAP поддерживает гарантированную доставку сообщений и устойчивость к сбоям через
WS-ReliableMessaging.
## Минусы SOAP
- Сложность: SOAP сообщения более громоздки, так как все данные передаются в формате XML, что усложняет их парсинг и
требует больше вычислительных ресурсов.
- Медлительность: Из-за использования XML и обширных заголовков сообщения SOAP имеют больший объем, что приводит к
более медленной передаче данных по сравнению с REST.
- Меньшая гибкость: SOAP ограничен в плане форматов данных и строго зависит от XML, в то время как REST поддерживает
JSON и другие форматы.
- Меньшая совместимость с вебом: SOAP был разработан до появления современных веб-технологий, таких как AJAX, поэтому
его интеграция с веб-приложениями может быть сложнее, чем REST.
- Кэширование отсутствует: SOAP не поддерживает кэширование на уровне HTTP, что делает его менее эффективным для систем
с большим количеством чтений.
## Сравнительная таблица
| Характеристика         | REST                               | SOAP                                     |
|------------------------|------------------------------------|------------------------------------------|
| Протокол               | Архитектурный стиль (HTTP)         | Строгий протокол                         |
| Формат данных          | JSON, XML, YAML, любые другие      | Только XML                               |
| Стандарт безопасности  | SSL/TLS, OAuth                     | WS-Security                              |
| Поддержка транзакций   | Нет                                | Да                                       |
| Лёгкость разработки    | Проще в реализации                 | Сложнее в разработке                     |
| Поддержка кэширования  | Да                                 | Нет                                      |
| Поддержка сессионности | Нет                                | Да                                       |
| Время отклика          | Быстрое, засчёт легковесных данных | Медленное, засчёт громоздкости сообщений |
| Описание сервисов      | OpenAPI/Swagger                    | WSDL                                     |
## Пример REST
### Получение списка всех пользователей
Запрос:
```http request
GET /users HTTP/1.1
Host: api.example.com
Content-Type: application/json
```
Ответ:
```json
[
    {
        "id": 1,
        "name": "John Doe",
        "email": "john@example.com"
    },
    {
        "id": 2,
        "name": "Jane Smith",
        "email": "jane@example.com"
    }
]
```
### Получение информации о конкретном пользователе
Запрос:
```http request
GET /users/1 HTTP/1.1
Host: api.example.com
Content-Type: application/json
```
Ответ:
```json
{
    "id": 1,
    "name": "John Doe",
    "email": "john@example.com"
}
```
### Создание нового пользователя
Запрос:
```http request
POST /users HTTP/1.1
Host: api.example.com
Content-Type: application/json

{
    "name": "Alice Johnson",
    "email": "alice@example.com"
}
```
Ответ:
```json
{
    "id": 3,
    "name": "Alice Johnson",
    "email": "alice@example.com"
}
```
### Обновление информации о пользователе
Запрос:
```http request
PUT /users/3 HTTP/1.1
Host: api.example.com
Content-Type: application/json

{
    "name": "Alice Johnson",
    "email": "alice.new@example.com"
}
```
Ответ:
```json
{
    "id": 3,
    "name": "Alice Johnson",
    "email": "alice.new@example.com"
}
```
### Удаление пользователя
Запрос:
```http request
DELETE /users/3 HTTP/1.1
Host: api.example.com
Content-Type: application/json
```
Отвеет:
```json
{
    "message": "User deleted successfully"
}
```
## Пример SOAP
### Получение всех пользователей
Запрос:
```xml
POST /UserService HTTP/1.1
Host: api.example.com
Content-Type: text/xml; charset=utf-8
SOAPAction: "getAllUsers"

<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:usr="http://example.com/users">
   <soapenv:Header/>
   <soapenv:Body>
      <usr:GetAllUsersRequest/>
   </soapenv:Body>
</soapenv:Envelope>
```
Ответ:
```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:usr="http://example.com/users">
   <soapenv:Header/>
   <soapenv:Body>
      <usr:GetAllUsersResponse>
         <usr:User>
            <usr:id>1</usr:id>
            <usr:name>John Doe</usr:name>
            <usr:email>john@example.com</usr:email>
         </usr:User>
         <usr:User>
            <usr:id>2</usr:id>
            <usr:name>Jane Smith</usr:name>
            <usr:email>jane@example.com</usr:email>
         </usr:User>
      </usr:GetAllUsersResponse>
   </soapenv:Body>
</soapenv:Envelope>
```
### Получение информации о пользователе
Запрос:
```xml
POST /UserService HTTP/1.1
Host: api.example.com
Content-Type: text/xml; charset=utf-8
SOAPAction: "getUser"

<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:usr="http://example.com/users">
   <soapenv:Header/>
   <soapenv:Body>
      <usr:GetUserRequest>
         <usr:id>1</usr:id>
      </usr:GetUserRequest>
   </soapenv:Body>
</soapenv:Envelope>
```
Ответ:
```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:usr="http://example.com/users">
   <soapenv:Header/>
   <soapenv:Body>
      <usr:GetUserResponse>
         <usr:id>1</usr:id>
         <usr:name>John Doe</usr:name>
         <usr:email>john@example.com</usr:email>
      </usr:GetUserResponse>
   </soapenv:Body>
</soapenv:Envelope>
```
### Создание нового пользователя
Запрос:
```xml
POST /UserService HTTP/1.1
Host: api.example.com
Content-Type: text/xml; charset=utf-8
SOAPAction: "createUser"

<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:usr="http://example.com/users">
   <soapenv:Header/>
   <soapenv:Body>
      <usr:CreateUserRequest>
         <usr:name>Alice Johnson</usr:name>
         <usr:email>alice@example.com</usr:email>
      </usr:CreateUserRequest>
   </soapenv:Body>
</soapenv:Envelope>
```
Ответ:
```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:usr="http://example.com/users">
   <soapenv:Header/>
   <soapenv:Body>
      <usr:CreateUserResponse>
         <usr:id>3</usr:id>
         <usr:name>Alice Johnson</usr:name>
         <usr:email>alice@example.com</usr:email>
      </usr:CreateUserResponse>
   </soapenv:Body>
</soapenv:Envelope>
```
### Обновление информации о пользователе
Зарос:
```xml
POST /UserService HTTP/1.1
Host: api.example.com
Content-Type: text/xml; charset=utf-8
SOAPAction: "updateUser"

<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:usr="http://example.com/users">
   <soapenv:Header/>
   <soapenv:Body>
      <usr:UpdateUserRequest>
         <usr:id>1</usr:id>
         <usr:name>John Doe Updated</usr:name>
         <usr:email>john.updated@example.com</usr:email>
      </usr:UpdateUserRequest>
   </soapenv:Body>
</soapenv:Envelope>
```
Ответ:
```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:usr="http://example.com/users">
   <soapenv:Header/>
   <soapenv:Body>
      <usr:UpdateUserResponse>
         <usr:id>1</usr:id>
         <usr:name>John Doe Updated</usr:name>
         <usr:email>john.updated@example.com</usr:email>
         <usr:message>User updated successfully</usr:message>
      </usr:UpdateUserResponse>
   </soapenv:Body>
</soapenv:Envelope>
```
### Удаление пользователя
Запрос:
```xml
POST /UserService HTTP/1.1
Host: api.example.com
Content-Type: text/xml; charset=utf-8
SOAPAction: "deleteUser"

<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:usr="http://example.com/users">
   <soapenv:Header/>
   <soapenv:Body>
      <usr:DeleteUserRequest>
         <usr:id>3</usr:id>
      </usr:DeleteUserRequest>
   </soapenv:Body>
</soapenv:Envelope>
```
Ответ:
```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:usr="http://example.com/users">
   <soapenv:Header/>
   <soapenv:Body>
      <usr:DeleteUserResponse>
         <usr:message>User deleted successfully</usr:message>
      </usr:DeleteUserResponse>
   </soapenv:Body>
</soapenv:Envelope>
```
