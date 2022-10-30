# GitHub api wrapper
This is GitHub api wrapper for searching repositories which is not forks by username.

## Startup

### Starting with enty point
1. For starting application on your local machine need to use `GitIntegrationApplication`

### Increasing rate limit for requests
To increase rate limit need to be authorized in GitHub using client id value from application.properties.
URL to get access token `https://github.com/login/oauth/authorize?client_id={clientId}`
Use this access token as a query parameter 'token' to get list not fork repositories. 
### Starting with Docker
For staring application with docker you need to:
1. build application with command: `./gradlew build`
2. build your docker image with command from project folder: `docker build --build-arg JAR_FILE=build/libs/*.jar -t {yourOrganization}/{yourAppName} .`
3. run your application with command: `docker run -p 8080:8080 {yourOrganization}/{yourAppName}`

### Stopping application
1. run command: `docker stop {containerId}`

### Reference Documentation
For further reference, please consider the following sections:
* [Official GitHub documentation](https://docs.github.com/en/rest)