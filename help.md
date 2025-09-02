# As a new user

## 1. I've registered my account with
```bash
curl -X 'POST' \
  'http://localhost:8080/auth/register' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "username": "novato40",
  "email": "novato40@email.com",
  "password": "Senha1234"
}'
```
The response was

```JSON
{
  "id": "cea797c4-9ee9-4657-a5fe-a9e59abe032b",
  "username": "novato40",
  "email": "novato40@email.com"
}
```

## 2. I logged in with:

```bash
curl -X 'POST' \
  'http://localhost:8080/auth/login' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "username": "novato40",
  "password": "Senha1234"
}'
```

The response was

```JSON
{
  "success": true,
  "message": "Login successful",
  "token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJyZXBseS1jb3JlIiwidXBuIjoibm92YXRvNDAiLCJncm91cHMiOlsidXNlciJdLCJpZCI6ImNlYTc5N2M0LTllZTktNDY1Ny1hNWZlLWE5ZTU5YWJlMDMyYiIsImV4cCI6MTc1Njg0MjQ5MzcxOSwiaWF0IjoxNzU2ODQyNDkwLCJqdGkiOiI5ZmE3OGE3ZS01OGRlLTQzODItOTVkZi03MGQ3MjMwODcyMWIifQ.NEJ6lK_Y3x7vu691VIX_4ceZEnnNuNGyXHdafXYaThUcFWgnzoLV1jBrbrtMQCNOa59S7F0vI9si28MSLr1oK8Vgmt4V-D31h-LPo5zSooP6Qr2QePOXm07d5U_WP3vR26t5Zvo6AlUGsy-S152shjDa9ES2A88r3uWINaA7cIU0AQshd7Ig3SDsNUVAaS5c6Ddx9NfX7b07hSLisBofwCBePE8xtP-hkOLDfw5oF7Yk_4urYTme88-0i4gi4_bfE35-2hQM0mLrmdb49VNBg_n8dB1BxQWhBDxue2Y-CzB3kHO7AWnTdDzFS9lzKk--OGHeA-gyr149JnuMR-J8Aw"
}
```

## 3. I've fetched my user information with

```bash
curl -X 'GET' \
  'http://localhost:8080/auth/authenticate' \
  -H 'accept: */*' \
  -H 'Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJyZXBseS1jb3JlIiwidXBuIjoibm92YXRvNDAiLCJncm91cHMiOlsidXNlciJdLCJpZCI6ImNlYTc5N2M0LTllZTktNDY1Ny1hNWZlLWE5ZTU5YWJlMDMyYiIsImV4cCI6MTc1Njg0MjQ5MzcxOSwiaWF0IjoxNzU2ODQyNDkwLCJqdGkiOiI5ZmE3OGE3ZS01OGRlLTQzODItOTVkZi03MGQ3MjMwODcyMWIifQ.NEJ6lK_Y3x7vu691VIX_4ceZEnnNuNGyXHdafXYaThUcFWgnzoLV1jBrbrtMQCNOa59S7F0vI9si28MSLr1oK8Vgmt4V-D31h-LPo5zSooP6Qr2QePOXm07d5U_WP3vR26t5Zvo6AlUGsy-S152shjDa9ES2A88r3uWINaA7cIU0AQshd7Ig3SDsNUVAaS5c6Ddx9NfX7b07hSLisBofwCBePE8xtP-hkOLDfw5oF7Yk_4urYTme88-0i4gi4_bfE35-2hQM0mLrmdb49VNBg_n8dB1BxQWhBDxue2Y-CzB3kHO7AWnTdDzFS9lzKk--OGHeA-gyr149JnuMR-J8Aw'
```

The response was
```JSON
{
  "userId": "cea797c4-9ee9-4657-a5fe-a9e59abe032b",
  "roles": [
    "user"
  ]
}
```

## 4. I've fetched my user account information with

GET - /users/{id} - getUserById

```bash
curl -X 'GET' \
  'http://localhost:8080/users/cea797c4-9ee9-4657-a5fe-a9e59abe032b' \
  -H 'accept: */*' \
  -H 'Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJyZXBseS1jb3JlIiwidXBuIjoibm92YXRvNDAiLCJncm91cHMiOlsidXNlciJdLCJpZCI6ImNlYTc5N2M0LTllZTktNDY1Ny1hNWZlLWE5ZTU5YWJlMDMyYiIsImV4cCI6MTc1Njg0MjQ5MzcxOSwiaWF0IjoxNzU2ODQyNDkwLCJqdGkiOiI5ZmE3OGE3ZS01OGRlLTQzODItOTVkZi03MGQ3MjMwODcyMWIifQ.NEJ6lK_Y3x7vu691VIX_4ceZEnnNuNGyXHdafXYaThUcFWgnzoLV1jBrbrtMQCNOa59S7F0vI9si28MSLr1oK8Vgmt4V-D31h-LPo5zSooP6Qr2QePOXm07d5U_WP3vR26t5Zvo6AlUGsy-S152shjDa9ES2A88r3uWINaA7cIU0AQshd7Ig3SDsNUVAaS5c6Ddx9NfX7b07hSLisBofwCBePE8xtP-hkOLDfw5oF7Yk_4urYTme88-0i4gi4_bfE35-2hQM0mLrmdb49VNBg_n8dB1BxQWhBDxue2Y-CzB3kHO7AWnTdDzFS9lzKk--OGHeA-gyr149JnuMR-J8Aw'  
```

The response was:

```JSON
{
  "id": "cea797c4-9ee9-4657-a5fe-a9e59abe032b",
  "username": "novato40",
  "email": "novato40@email.com"
}
```

## 5. I've updated my password with:
PUT - /users/{id}/password - updateUserPassword

```bash
curl -X 'PUT' \
  'http://localhost:8080/users/cea797c4-9ee9-4657-a5fe-a9e59abe032b/password' \
  -H 'accept: */*' \
  -H 'Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJyZXBseS1jb3JlIiwidXBuIjoibm92YXRvNDAiLCJncm91cHMiOlsidXNlciJdLCJpZCI6ImNlYTc5N2M0LTllZTktNDY1Ny1hNWZlLWE5ZTU5YWJlMDMyYiIsImV4cCI6MTc1Njg0MjQ5MzcxOSwiaWF0IjoxNzU2ODQyNDkwLCJqdGkiOiI5ZmE3OGE3ZS01OGRlLTQzODItOTVkZi03MGQ3MjMwODcyMWIifQ.NEJ6lK_Y3x7vu691VIX_4ceZEnnNuNGyXHdafXYaThUcFWgnzoLV1jBrbrtMQCNOa59S7F0vI9si28MSLr1oK8Vgmt4V-D31h-LPo5zSooP6Qr2QePOXm07d5U_WP3vR26t5Zvo6AlUGsy-S152shjDa9ES2A88r3uWINaA7cIU0AQshd7Ig3SDsNUVAaS5c6Ddx9NfX7b07hSLisBofwCBePE8xtP-hkOLDfw5oF7Yk_4urYTme88-0i4gi4_bfE35-2hQM0mLrmdb49VNBg_n8dB1BxQWhBDxue2Y-CzB3kHO7AWnTdDzFS9lzKk--OGHeA-gyr149JnuMR-J8Aw' \
  -H 'Content-Type: application/json' \
  -d '{
  "password": "Pass1234"
}'
```

The response was:

204 no content

## 6. I've updated my email with:
PUT - /users/{id}/email - updateUserEmail

```bash
curl -X 'PUT' \
  'http://localhost:8080/users/cea797c4-9ee9-4657-a5fe-a9e59abe032b/email' \
  -H 'accept: */*' \
  -H 'Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJyZXBseS1jb3JlIiwidXBuIjoibm92YXRvNDAiLCJncm91cHMiOlsidXNlciJdLCJpZCI6ImNlYTc5N2M0LTllZTktNDY1Ny1hNWZlLWE5ZTU5YWJlMDMyYiIsImV4cCI6MTc1Njg0MjQ5MzcxOSwiaWF0IjoxNzU2ODQyNDkwLCJqdGkiOiI5ZmE3OGE3ZS01OGRlLTQzODItOTVkZi03MGQ3MjMwODcyMWIifQ.NEJ6lK_Y3x7vu691VIX_4ceZEnnNuNGyXHdafXYaThUcFWgnzoLV1jBrbrtMQCNOa59S7F0vI9si28MSLr1oK8Vgmt4V-D31h-LPo5zSooP6Qr2QePOXm07d5U_WP3vR26t5Zvo6AlUGsy-S152shjDa9ES2A88r3uWINaA7cIU0AQshd7Ig3SDsNUVAaS5c6Ddx9NfX7b07hSLisBofwCBePE8xtP-hkOLDfw5oF7Yk_4urYTme88-0i4gi4_bfE35-2hQM0mLrmdb49VNBg_n8dB1BxQWhBDxue2Y-CzB3kHO7AWnTdDzFS9lzKk--OGHeA-gyr149JnuMR-J8Aw' \
  -H 'Content-Type: application/json' \
  -d '{
  "email": "user4040@email.com"
}'
```

The response was:

204 no content


## 7. I've updated my username with:
PUT - /users/{id}/username - updateUserUsername

```bash
curl -X 'PUT' \
  'http://localhost:8080/users/cea797c4-9ee9-4657-a5fe-a9e59abe032b/username' \
  -H 'accept: */*' \
  -H 'Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJyZXBseS1jb3JlIiwidXBuIjoibm92YXRvNDAiLCJncm91cHMiOlsidXNlciJdLCJpZCI6ImNlYTc5N2M0LTllZTktNDY1Ny1hNWZlLWE5ZTU5YWJlMDMyYiIsImV4cCI6MTc1Njg0MjQ5MzcxOSwiaWF0IjoxNzU2ODQyNDkwLCJqdGkiOiI5ZmE3OGE3ZS01OGRlLTQzODItOTVkZi03MGQ3MjMwODcyMWIifQ.NEJ6lK_Y3x7vu691VIX_4ceZEnnNuNGyXHdafXYaThUcFWgnzoLV1jBrbrtMQCNOa59S7F0vI9si28MSLr1oK8Vgmt4V-D31h-LPo5zSooP6Qr2QePOXm07d5U_WP3vR26t5Zvo6AlUGsy-S152shjDa9ES2A88r3uWINaA7cIU0AQshd7Ig3SDsNUVAaS5c6Ddx9NfX7b07hSLisBofwCBePE8xtP-hkOLDfw5oF7Yk_4urYTme88-0i4gi4_bfE35-2hQM0mLrmdb49VNBg_n8dB1BxQWhBDxue2Y-CzB3kHO7AWnTdDzFS9lzKk--OGHeA-gyr149JnuMR-J8Aw' \
  -H 'Content-Type: application/json' \
  -d '{
  "username": "user4040"
}'
```

The response was:

204 no content

## 8. I've fetch my user roles with:
GET - /users/{targetId}/roles - fetchUserRoles

```bash
curl -X 'GET' \
  'http://localhost:8080/users/cea797c4-9ee9-4657-a5fe-a9e59abe032b/roles' \
  -H 'accept: */*' \
  -H 'Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJyZXBseS1jb3JlIiwidXBuIjoibm92YXRvNDAiLCJncm91cHMiOlsidXNlciJdLCJpZCI6ImNlYTc5N2M0LTllZTktNDY1Ny1hNWZlLWE5ZTU5YWJlMDMyYiIsImV4cCI6MTc1Njg0MjQ5MzcxOSwiaWF0IjoxNzU2ODQyNDkwLCJqdGkiOiI5ZmE3OGE3ZS01OGRlLTQzODItOTVkZi03MGQ3MjMwODcyMWIifQ.NEJ6lK_Y3x7vu691VIX_4ceZEnnNuNGyXHdafXYaThUcFWgnzoLV1jBrbrtMQCNOa59S7F0vI9si28MSLr1oK8Vgmt4V-D31h-LPo5zSooP6Qr2QePOXm07d5U_WP3vR26t5Zvo6AlUGsy-S152shjDa9ES2A88r3uWINaA7cIU0AQshd7Ig3SDsNUVAaS5c6Ddx9NfX7b07hSLisBofwCBePE8xtP-hkOLDfw5oF7Yk_4urYTme88-0i4gi4_bfE35-2hQM0mLrmdb49VNBg_n8dB1BxQWhBDxue2Y-CzB3kHO7AWnTdDzFS9lzKk--OGHeA-gyr149JnuMR-J8Aw'
```

the response was:
```JSON
{
"roles": [
"user"
]
}
```

## 9. I've deleted my user account with:
DELETE - /users/{id} - deleteUserById

```bash
curl -X 'DELETE' \
  'http://localhost:8080/users/cea797c4-9ee9-4657-a5fe-a9e59abe032b' \
  -H 'accept: */*' \
  -H 'Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJyZXBseS1jb3JlIiwidXBuIjoibm92YXRvNDAiLCJncm91cHMiOlsidXNlciJdLCJpZCI6ImNlYTc5N2M0LTllZTktNDY1Ny1hNWZlLWE5ZTU5YWJlMDMyYiIsImV4cCI6MTc1Njg0MjQ5MzcxOSwiaWF0IjoxNzU2ODQyNDkwLCJqdGkiOiI5ZmE3OGE3ZS01OGRlLTQzODItOTVkZi03MGQ3MjMwODcyMWIifQ.NEJ6lK_Y3x7vu691VIX_4ceZEnnNuNGyXHdafXYaThUcFWgnzoLV1jBrbrtMQCNOa59S7F0vI9si28MSLr1oK8Vgmt4V-D31h-LPo5zSooP6Qr2QePOXm07d5U_WP3vR26t5Zvo6AlUGsy-S152shjDa9ES2A88r3uWINaA7cIU0AQshd7Ig3SDsNUVAaS5c6Ddx9NfX7b07hSLisBofwCBePE8xtP-hkOLDfw5oF7Yk_4urYTme88-0i4gi4_bfE35-2hQM0mLrmdb49VNBg_n8dB1BxQWhBDxue2Y-CzB3kHO7AWnTdDzFS9lzKk--OGHeA-gyr149JnuMR-J8Aw'
```

The response was:

204 no content

--------


As an administrator
- I want to create a new account
    POST - /users - createUser

- I want to fetch all users
    GET - /users - fetchUsers

- I want to update a user's roles
    PUT - /users/{targetId}/roles - updateUserRole

As a developer
- I want an account with all the roles functions
    PUT - /users/me/upgrade/roles - upgradeUserRoles