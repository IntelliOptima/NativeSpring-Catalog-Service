# NativeSpring-Catalog-Service


## cmd to set up PostgresDB

```bash
docker run -d `
>>  --name polar-postgres `     ## name of the container for service discovery
>>  --net catalog-network `     # If you created a network
>>  -e POSTGRES_USER=polardb `
>>  -e POSTGRES_PASSWORD=test1234 `
>>  -e POSTGRES_DB=polardb_catalog `
>>  -p 5433:5432 `
>> --restart=always `
>> postgres
```

## cmd to set up catalog-service

```bash
docker run --rm -d `
>> --name catalog-service ` ## name of the container for service discovery
>> --net catalog-network `  # If you created a network
>> -p 9001:9001 `
>> -e SPRING_DATASOURCE_URL=jdbc:postgresql://polar-postgres:5432/polardb_catalog ` ### Hostname is the name of the postgres container (service discovry)
>> -e SPRING_PROFILES_ACTIVE=testdata `
>> catalog-service
```

## cmd to build with BuildPacks and push to gcr.io | All gradle code inside the **bootBuildImage Block**  

```bash
./gradlew bootBuildImage `                                                                                                          
>> --imageName ghcr.io/intellioptimacatalog-service `                                                                         
>> --publishImage `
>> -PregistryUrl=ghcr `
>> -PregistryUsername=<your-username> `
>> -PregistryToken=<your-token> `
```


## Remote Debugging with VS Code

1. Click Run, then click Add Configuration, append the folowing object configuration to the launch.json file.
2. Remeber to add <- JAVA_TOOL_OPTIONS=-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:<YOUR-PORT-CHOICE>> to your environment variables, in your docker / docker compose yaml file for run command.

```json
{
            "type": "java",
            "name": "Spring Boot-OrderServiceApplication<catalog-service> debugger",
            "request": "attach",
            "hostName": "localhost",
            "port": 8001,
}
```


