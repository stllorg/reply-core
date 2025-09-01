After login, I've sent GET request verify userId and roles





curl -X 'GET' \
  'http://localhost:8080/auth/authenticate' \
  -H 'accept: */*' \
  -H 'Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJyZXBseS1jb3JlIiwidXBuIjoibm92YXRvMzIiLCJncm91cHMiOlsidXNlciJdLCJpZCI6IjY0NmQzNGFmLWFlMGQtNDU2NC1hNjQzLTA0NThmZjE4YWE4MSIsImV4cCI6MTc1NjczMTM4NTg3OSwiaWF0IjoxNzU2NzMxMzgyLCJqdGkiOiI0ZDFjNjRjZC05MmZhLTQ0YWMtOGVkYS1jZDg5NzU0NzYzMDEifQ.cKQ1dRb5wZHMosbcT3iG7jriLTQwE4XosMf7JLpII_pCF-XShi8r2YbXosreqPTvPyLfhadElTY6XAJE59cL4P-WIppq1B0x9tWeWoNpyhGQVim_RQM7g91_yyUROpsfrnOa8cPJgE5XXjgQfrwBF9cFwTOFonw02g2q480LTgdPU9ccAPHaZifv5y602V2WoO_RZCymbgw_xc1vK3f6Z8fDygm-ByXPw0YhJXIW25Kn9mVRKGrDXtzEiKLBEt5Miz2XcY-HlGVFpiRI6sWLJOV05Z5gOo7k7RgyOzuq-77IIwzETO06mwtBZINewK1AsrMmfDmSwVST2R10yoX6qw'

  response



  {
  "userId": "646d34af-ae0d-4564-a643-0458ff18aa81",
  "roles": [
    "user"
  ]
}



After I have my userId I want to verify my user data with

curl -X 'GET' \
  'http://localhost:8080/users/646d34af-ae0d-4564-a643-0458ff18aa81' \
  -H 'accept: */*'


The response was

{
  "id": "646d34af-ae0d-4564-a643-0458ff18aa81",
  "username": "novato32",
  "email": "novato32@email.com",
  "password": "$2a$10$EcboQ5w0p/EcduMi1Ai85OfmpQddjw66xzRVrPpr9drxZvZFezig.",
  "createdAt": "2025-08-28T16:10:39.937167"
}
