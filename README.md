# GitHub api wrapper
This is GitHub api wrapper for searching repositories which is not forks by username.

## Startup

### Starting with enty point
1. For starting application on your local machine need to use `GitIntegrationApplication`

### Increasing rate limit for requests
To increase rate limit need to be authorized in GitHub using client id value from application.properties.

First you need to create OAuth app on your GitHub account, bellow is tutorial how to do it
* [Create OAuth App](https://docs.github.com/en/developers/apps/building-oauth-apps/creating-an-oauth-app).

When your OAuth App is created you need to update `application.properties` and find 2 keys:
1. `github.auth.client-id` and replace value for your client id
2. `github.auth.client-secret` and replace value for your client secret

URL to get access token `https://github.com/login/oauth/authorize?client_id={clientId}`

This endpoint with your client id will forward you to callback method that implemented on the application, so application should be started before using this api, (in the postman can be issues with forwarding, if you have it, please use browser).

In the response you recived JWT token, You can use this access token as a query parameter 'token' to get list not fork repositories. 
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