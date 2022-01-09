# Technical test 

## How to use

+ Set the JAVA_HOME property to a valid JAVA 11+ location.
+ launch: `./gradlew bootRun`

## Documentation

There is postman collection at the root of this repo called `Technical test.postman_collection.json`.

Swagger UI is also accessible via `<host>/swagger-ui/index.html`.

+ Create a user: 
  + Url: http://localhost:8080/users
  + Method: PUT
  + Response: 201 Created. Location of the user in the header.
  + paypload:
```json
{
  "username": "string",
  "birthday": "dd/MM/yyyy",
  "location": "string",
  "sexe": "MALE|FEMALE|OTHER|UNDEFINED",
  "phone": "xx xx xx xx xx"
}
```


+ Get a user:
  + Url: http://localhost:8080/users/{id}
  + Method: GET
  + Response: 200 Ok

```json
{
"id": 1,
"username": "alan",
"birthday": "08/09/1992",
"location": "mérignac",
"phone": "06 23 23 23 23",
"sexe": "MALE"
}
```
