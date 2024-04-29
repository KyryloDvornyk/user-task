## About

Test project for Clear Solutions. All requirements are implemented. Added H2 in-memory database for easier working with entities. 
Also added additional Entity for storing address object.

## Usage

There are 6 endpoints:
```
1. POST /users - to create user
2. GET /users/{id} - to get user by id
3. GET /users?from=2000-01-01&to=2010-01-01 - to get all users by date range(with date example)
4. PUT /users/{id} to fully update user
5. PATCH /users/{id} to partially update user
6. DELETE /users/{is} to delete user
```

Here is an example of the body for POST/PUT/PATCH request:
```
{
        "email": "username@email.com",
        "firstName": "John",
        "lastName": "Johnson",
        "birthDate": "2000-01-31",
        "address": {"street" : "Shevchenko str", "houseNumber" : "12"},
        "phoneNumber": "+380671852382"
    }
```