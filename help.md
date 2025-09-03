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


# As an administrator

## 1. I've logged in with default password Senha1234 
```bash
curl -X 'POST' \
  'http://localhost:8080/auth/login' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "username": "admin",
  "password": "Senha1234"
}'

```

The response was
```JSON
{
  "success": true,
  "message": "Login successful",
  "token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJyZXBseS1jb3JlIiwidXBuIjoiYWRtaW4iLCJncm91cHMiOlsiYWRtaW4iXSwiaWQiOiJlZmIyZjRjMS1iZjMxLTQ5OTUtODUzZC03Y2Q1MDI2ZTdiNDciLCJleHAiOjE3NTY5MDgzNTQwNTYsImlhdCI6MTc1NjkwODM1MCwianRpIjoiNjFiM2E3MmItYmZiMy00YTk3LWI4NzItZjgzOTllNmExODM4In0.UyR-A6G7DVvOpsCC2arTVv8aNGjCJr2NLgterVcFcxkaLKY2V5oKdCAnSrI84I7kk-Zvs1JQ39NLXwhMn_H-D0vupb4pRIMnkRSuhZrVka7UoOHQOOq5tZvlmV0nutDKeVtwZ1tW9DqVB63aECR5KDYUJX9PHri1CJqVMRHICSaLDB-8X4OsaQp8Kelq5PCk74dooOX-0nYu6W9129k6PS63rZF16f5DTC0pdnraeS2z4n6wyOHfsk1lXG3FhN_mDEXj92icKucGrK_AwS4w0xXm7TodD2Svvt4SGWxbGkuBROTuVSmCzNWPU39J3aD0wHGE--RNLtcYBqdqqJSZHA"
}
```

## 2.5. I've fetched my user information with

```bash

curl -X 'GET' \
  'http://localhost:8080/auth/authenticate' \
  -H 'accept: */*' \
  -H 'Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJyZXBseS1jb3JlIiwidXBuIjoiYWRtaW4iLCJncm91cHMiOlsiYWRtaW4iXSwiaWQiOiJlZmIyZjRjMS1iZjMxLTQ5OTUtODUzZC03Y2Q1MDI2ZTdiNDciLCJleHAiOjE3NTY5MDgzNTQwNTYsImlhdCI6MTc1NjkwODM1MCwianRpIjoiNjFiM2E3MmItYmZiMy00YTk3LWI4NzItZjgzOTllNmExODM4In0.UyR-A6G7DVvOpsCC2arTVv8aNGjCJr2NLgterVcFcxkaLKY2V5oKdCAnSrI84I7kk-Zvs1JQ39NLXwhMn_H-D0vupb4pRIMnkRSuhZrVka7UoOHQOOq5tZvlmV0nutDKeVtwZ1tW9DqVB63aECR5KDYUJX9PHri1CJqVMRHICSaLDB-8X4OsaQp8Kelq5PCk74dooOX-0nYu6W9129k6PS63rZF16f5DTC0pdnraeS2z4n6wyOHfsk1lXG3FhN_mDEXj92icKucGrK_AwS4w0xXm7TodD2Svvt4SGWxbGkuBROTuVSmCzNWPU39J3aD0wHGE--RNLtcYBqdqqJSZHA'
```

The response was
```JSON
{
  "userId": "efb2f4c1-bf31-4995-853d-7cd5026e7b47",
  "roles": [
    "admin"
  ]
}
```

## 3. I've fetched all users with
```bash
curl -X 'GET' \
  'http://localhost:8080/users?limit=15&page=1' \
  -H 'accept: */*' \
  -H 'Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJyZXBseS1jb3JlIiwidXBuIjoiYWRtaW4iLCJncm91cHMiOlsiYWRtaW4iXSwiaWQiOiJlZmIyZjRjMS1iZjMxLTQ5OTUtODUzZC03Y2Q1MDI2ZTdiNDciLCJleHAiOjE3NTY5MDgzNTQwNTYsImlhdCI6MTc1NjkwODM1MCwianRpIjoiNjFiM2E3MmItYmZiMy00YTk3LWI4NzItZjgzOTllNmExODM4In0.UyR-A6G7DVvOpsCC2arTVv8aNGjCJr2NLgterVcFcxkaLKY2V5oKdCAnSrI84I7kk-Zvs1JQ39NLXwhMn_H-D0vupb4pRIMnkRSuhZrVka7UoOHQOOq5tZvlmV0nutDKeVtwZ1tW9DqVB63aECR5KDYUJX9PHri1CJqVMRHICSaLDB-8X4OsaQp8Kelq5PCk74dooOX-0nYu6W9129k6PS63rZF16f5DTC0pdnraeS2z4n6wyOHfsk1lXG3FhN_mDEXj92icKucGrK_AwS4w0xXm7TodD2Svvt4SGWxbGkuBROTuVSmCzNWPU39J3aD0wHGE--RNLtcYBqdqqJSZHA'
```

The response was:
```JSON
{
  "data": [
    {
      "id": "6a4c8777-fc25-4098-8f84-bc08ae8c46e6",
      "username": "newuser",
      "email": "newuser@email.com"
    },
    {
      "id": "b1d5cfe9-8348-4aa1-97da-e327b755090e",
      "username": "manager",
      "email": "manager@email.com"
    },
    {
      "id": "cb764263-94de-430e-98b8-231a813366af",
      "username": "support",
      "email": "support@email.com"
    },
    {
      "id": "efb2f4c1-bf31-4995-853d-7cd5026e7b47",
      "username": "admin",
      "email": "adm@testmail.com"
    }
  ],
  "currentPage": 1,
  "perPage": 15,
  "totalItems": 4,
  "totalPages": 1
}
```
The correct response is a list that is not empty


## 4. I've created a new user account with

```bash
curl -X 'POST' \
  'http://localhost:8080/users' \
  -H 'accept: */*' \
  -H 'Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJyZXBseS1jb3JlIiwidXBuIjoiYWRtaW4iLCJncm91cHMiOlsiYWRtaW4iXSwiaWQiOiJlZmIyZjRjMS1iZjMxLTQ5OTUtODUzZC03Y2Q1MDI2ZTdiNDciLCJleHAiOjE3NTY5MDgzNTQwNTYsImlhdCI6MTc1NjkwODM1MCwianRpIjoiNjFiM2E3MmItYmZiMy00YTk3LWI4NzItZjgzOTllNmExODM4In0.UyR-A6G7DVvOpsCC2arTVv8aNGjCJr2NLgterVcFcxkaLKY2V5oKdCAnSrI84I7kk-Zvs1JQ39NLXwhMn_H-D0vupb4pRIMnkRSuhZrVka7UoOHQOOq5tZvlmV0nutDKeVtwZ1tW9DqVB63aECR5KDYUJX9PHri1CJqVMRHICSaLDB-8X4OsaQp8Kelq5PCk74dooOX-0nYu6W9129k6PS63rZF16f5DTC0pdnraeS2z4n6wyOHfsk1lXG3FhN_mDEXj92icKucGrK_AwS4w0xXm7TodD2Svvt4SGWxbGkuBROTuVSmCzNWPU39J3aD0wHGE--RNLtcYBqdqqJSZHA' \
  -H 'Content-Type: application/json' \
  -d '{
  "username": "createduser1",
  "email": "createduser1@email.com",
  "password": "Senha1234"
}'
```

The response was
```JSON
{
  "id": "47143f51-6a9f-4cc3-b3cd-eeba6ee29c8c",
  "username": "createduser1",
  "email": "createduser1@email.com"
}
```

## 5. I've updated the roles of the user I've just created
```bash
curl -X 'PUT' \
  'http://localhost:8080/users/47143f51-6a9f-4cc3-b3cd-eeba6ee29c8c/roles' \
  -H 'accept: */*' \
  -H 'Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJyZXBseS1jb3JlIiwidXBuIjoiYWRtaW4iLCJncm91cHMiOlsiYWRtaW4iXSwiaWQiOiJlZmIyZjRjMS1iZjMxLTQ5OTUtODUzZC03Y2Q1MDI2ZTdiNDciLCJleHAiOjE3NTY5MDgzNTQwNTYsImlhdCI6MTc1NjkwODM1MCwianRpIjoiNjFiM2E3MmItYmZiMy00YTk3LWI4NzItZjgzOTllNmExODM4In0.UyR-A6G7DVvOpsCC2arTVv8aNGjCJr2NLgterVcFcxkaLKY2V5oKdCAnSrI84I7kk-Zvs1JQ39NLXwhMn_H-D0vupb4pRIMnkRSuhZrVka7UoOHQOOq5tZvlmV0nutDKeVtwZ1tW9DqVB63aECR5KDYUJX9PHri1CJqVMRHICSaLDB-8X4OsaQp8Kelq5PCk74dooOX-0nYu6W9129k6PS63rZF16f5DTC0pdnraeS2z4n6wyOHfsk1lXG3FhN_mDEXj92icKucGrK_AwS4w0xXm7TodD2Svvt4SGWxbGkuBROTuVSmCzNWPU39J3aD0wHGE--RNLtcYBqdqqJSZHA' \
  -H 'Content-Type: application/json' \
  -d '{
  "roleNames": [
    "manager",
    "support",
    "user"
  ]
}'
```

The response was:

204 no content


## 6. I've checked the user roles of the user who I've updated his roles

```bash
curl -X 'GET' \
  'http://localhost:8080/users/47143f51-6a9f-4cc3-b3cd-eeba6ee29c8c/roles' \
  -H 'accept: */*' \
  -H 'Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJyZXBseS1jb3JlIiwidXBuIjoiYWRtaW4iLCJncm91cHMiOlsiYWRtaW4iXSwiaWQiOiJlZmIyZjRjMS1iZjMxLTQ5OTUtODUzZC03Y2Q1MDI2ZTdiNDciLCJleHAiOjE3NTY5MDgzNTQwNTYsImlhdCI6MTc1NjkwODM1MCwianRpIjoiNjFiM2E3MmItYmZiMy00YTk3LWI4NzItZjgzOTllNmExODM4In0.UyR-A6G7DVvOpsCC2arTVv8aNGjCJr2NLgterVcFcxkaLKY2V5oKdCAnSrI84I7kk-Zvs1JQ39NLXwhMn_H-D0vupb4pRIMnkRSuhZrVka7UoOHQOOq5tZvlmV0nutDKeVtwZ1tW9DqVB63aECR5KDYUJX9PHri1CJqVMRHICSaLDB-8X4OsaQp8Kelq5PCk74dooOX-0nYu6W9129k6PS63rZF16f5DTC0pdnraeS2z4n6wyOHfsk1lXG3FhN_mDEXj92icKucGrK_AwS4w0xXm7TodD2Svvt4SGWxbGkuBROTuVSmCzNWPU39J3aD0wHGE--RNLtcYBqdqqJSZHA'
```


```JSON
{
  "roles": [
    "manager",
    "support",
    "user"
  ]
}
```