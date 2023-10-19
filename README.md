# NativeSpring-Catalog-Service


## cmd to set up PostgresDB

```bash
docker run -d `
>>  --name polar-postgres `
>>  -e POSTGRES_USER=polardb `
>>  -e POSTGRES_PASSWORD=test1234 `
>>  -e POSTGRES_DB=polardb_catalog `
>>  -p 5433:5432 `
>> --restart=always `
>> postgres
```
