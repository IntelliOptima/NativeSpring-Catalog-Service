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
2. Remeber to add JAVA_TOOL_OPTIONS=-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:<(YOUR-PORT-CHOICE)> to your environment variables, in your docker / docker compose yaml file for run command.

```json
{
            "type": "java",
            "name": "Spring Boot-OrderServiceApplication<catalog-service> debugger",
            "request": "attach",
            "hostName": "localhost",
            "port": 8001,
}
```

## Setting up Tilt

After many hours of trying to get tilt to work with my project, I finally got it to work.
Since working on windows, variables names should be wrapped in percent signs

```bash
# Build
custom_build(
    # Name of the container image
    ref = 'catalog-service',
    # Command to build the container image
    command = 'gradlew bootBuildImage --imageName %EXPECTED_REF%', # Notice the %EXPECTED_REF% variable instead of $EXPECTED_REF
    # Files to watch that trigger a new build
    deps = ['build.gradle', 'src']
)


# Deploy
k8s_yaml(['k8s/resources/base/deployment.yml', 'k8s/resources/base/service.yml'])

# Manage
k8s_resource('catalog-service', port_forwards=['9001'])
```

## Achieving the same result with Kustomize on Tilt

If you rather want to use kustomize, you can use the following code

```bash
# Build
custom_build(
    # Name of the container image
    ref = 'catalog-service',
    # Command to build the container image
    command = 'gradlew bootBuildImage --imageName %EXPECTED_REF%',
    # Files to watch that trigger a new build
    deps = ['build.gradle', 'src']
)


# Deploy
k8s_yaml(kustomize('k8s/resources/base'))     # Notice the kustomize function refering to the folder containing the kustomization.yml file

# Manage
k8s_resource('catalog-service', port_forwards=['9001'])
```

-------------------------------------------------------------------------------------------------------------------

## Live Reload with Tilt

## 1. Add the following to your build.gradle file under dependencies

```bash
developmentOnly("org.springframework.boot:spring-boot-devtools")
```

## Then add the following to your application.properties file

```bash
spring.devtools.livereload.enabled=true
```

## Finally, add the following to your Tiltfile

```bash
# Build
custom_build(
    # Name of the container image
    ref = 'catalog-service',
    # Command to build the container image
    command = 'gradlew bootBuildImage --imageName %EXPECTED_REF%',
    # Files to watch that trigger a new build
    deps = ['build.gradle', './src/main/resources/application*.properties'],

    live_update=[                   # <----------------- Add this for live rolaod on changes within the src folder
        # Sync changed files
        sync('./src', '/workspace/src'),        # <------ This maps the src folder to the src folder in the container made with buildPack
    ]
)


# Deploy
k8s_yaml(kustomize('k8s/resources/base'))

# Manage
k8s_resource('catalog-service', port_forwards=['9001'])
```

## Using KubeConform to validate your kubernetes yaml files

read docs [here](https://medium.com/@dangreenlee_/continually-validate-kubernetes-manifests-using-kubeconform-and-githubactions-ed74ed3ba4ca)

applying kubeconform to workflow append to build stage

```yml
- name: Validate Kubernetes manifests
        uses: yokawasa/action-setup-kube-tools@v0.9.3
        with:
          setup-tools: |
            kubeconform
            kustomize
          kubeconform: '0.5.0'
          kustomize: '4.5.7'
      - run: |
          kustomize build k8s/resources/base | kubeconform -verbose
```

