# Serverless Microservcie Implementation with AWS Lambda, AWS APIGateway and AWS DynamoDB

## Overview

In microservices approach, software is composed of small independent services that communicate over well-defined APIs.
Serve less apps are architected such that developers can focus on their core competency—writing the actual business logic instead of responsibilities like operating system (OS) access control, OS patching, provisioning, right-sizing, scaling, and availability.
In serverless architecture, by implementing the microservices using `AWS APIGatewa`, `AWS Lambda` and `AWS DynamoDB` underlying AWS platform manages those responsibilities for you.

Purpose of this project is to provide serverless microservice implementation using the following technologies. 

-  AWS Services 
    -  AWS API Gateway, Lambda and DynamoDB
- Programming Language 
    - JAVA 8 
- Build Tool 
    - Gradle
- Unit Testing 
    - Junit, Hamcrest, Mockito 

# Prerequisites
  - Create an AWS account [https://aws.amazon.com/]
  - Configure AWS CLI [http://docs.aws.amazon.com/cli/latest/userguide/cli-chap-getting-started.html]

## Architecture

Following diagram shows the overall architecture of the Microservcie.

![](https://github.com/muditha-silva/readme-images/blob/master/lambda-proxy-service.png)

### APIGateway 

APIGateway uses Lambda proxy integration, by doing so requests will be proxied to Lambda. 

Please refer following diagram  

![](https://github.com/muditha-silva/readme-images/blob/master/apiGW.png)

### API endpoints 

| Resource | `POST` | `GET` | `PUT` | `DELETE` |
| ------ |  ------ | ------ | ------ | ------ |
| `/v1/employee` | Bulk Create Employees | List Employees by Search Criteria | Bulk Update Employees | Delete Employees by ids | 
| `/v1/employee/{id}` | N/A | Retrieve a employee | Update a employee | Delete a employee

### AWS Lambda

APIGateway proxied the contents of the client’s HTTPS request to Lambda function for execution. Those contents include request metadata, request headers, and the request body. 

Following shows a sample a proxed request for the resource `/v1/employee` for the `POST` operation.

```
{
    "resource": "/v1/employee",
    "path": "/v1/employee",
    "httpMethod": "POST",
    "headers": null,
    "queryStringParameters": null,
    "pathParameters": null,
    "stageVariables": null,
    "requestContext": {
        "path": "/v1/employee",
        "accountId": "",
        "resourceId": "",
        "stage": "test-invoke-stage",
        "requestId": "test-invoke-request",
        "identity": {
            "cognitoIdentityPoolId": null,
            "accountId": "",
            "cognitoIdentityId": null,
            "caller": "",
            "apiKey": "test-invoke-api-key",
            "sourceIp": "test-invoke-source-ip",
            "accessKey": "",
            "cognitoAuthenticationType": null,
            "cognitoAuthenticationProvider": null,
            "userArn": "",
            "userAgent": "Apache-HttpClient/4.5.x (Java/1.8.0_144)",
            "user": ""
        },
        "resourcePath": "/v1/employee",
        "httpMethod": "POST",
        "apiId": "js4o6av9ob"
    },
    "body": "[  \r\n   {  \r\n      \"id\":\"0001\",\r\n      \"type\":\"SWE\",\r\n      \"firstName\":\"XYZ\",\r\n      \"lastName\":\"PQR\",\r\n      \"addressLine1\":\"Address1\",\r\n      \"addressLine2\":\"Address2\",\r\n      \"postalCode\":\"900\",\r\n      \"city\":\"Colombo\",\r\n      \"countryCode\":\"SL\",\r\n      \"country\":\"Sri Lanka\"\r\n   }\r\n]",
    "isBase64Encoded": false
}
```

This project includes a complete Lambda proxy implementation `lambda\lambda-proxy-api` using JAVA 8 with unit testing. 
Lambda function handler will invoke the appropriate invoker based on the request `resource` and `httpMethod`.

#### Lambda function creation.

Using the AWS console create an IAM Role `lambda_basic_execution` for the Lambda function execution. For this scenario two policies were attached to the role `arn:aws:iam::aws:policy/AWSLambdaFullAccess` and `arn:aws:iam::aws:policy/AmazonDynamoDBFullAccess`

Please refer to following diagram.

![](https://github.com/muditha-silva/readme-images/blob/master/role.png)

Using the AWS console create the Lambda fuction `lambda-proxy-api`. 

Please refer to following diagram.

![](https://github.com/muditha-silva/readme-images/blob/master/lambda1.png)
![](https://github.com/muditha-silva/readme-images/blob/master/lambda2.png)

#### Lambda Function Build Command 

Using the Gradle wrapper build a deployable archive of the Lambda function.

#####  Linux and Mac OS X
```sh
$ /{PROJECT_HOME}/lambda-proxy-service/lambda/lambda-proxy-api/./gradlew clean build -x test --refresh-dependencies
```

#####  Windows
```sh
{PROJECT_HOME}\lambda-proxy-service\lambda\lambda-proxy-api>gradlew clean build -x test --refresh-dependencies
```

Deployable archive `payload.zip` will be created in the following location `/{PROJECT_HOME}/lambda-proxy-service/lambda/lambda-proxy-api/build/distributions`

#####  Update the Lambda function with the executable archive using AWS CLI
```sh 
D:\workspace\lambda-proxy-service\lambda\lambda-proxy-api>aws lambda update-function-code --function-name lambda-proxy-a
pi --zip-file fileb://build/distributions/payload-1.0.0.zip
```

### AWS DynamoDB 

Following diagram shows the `Employee` table design

![](https://github.com/muditha-silva/readme-images/blob/master/EmployeeTable.png)
