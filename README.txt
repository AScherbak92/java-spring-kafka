1.Directory of docker-compose file: Kafka+Java+Spring\Docker

When my-sql connection established and kafka servers are up ->
  -Order-service will be started on port 8080
  -Notification-service will be started on port 8081
  -Websocket-service will be started on port 8082

First open the websocket page in browser(localhost:8082) and click on "connect" button to handshake

To send message from Postman:
1.GET request for api-key: localhost:8080/api/auth/token
2.POST request for sending message: localhost:8080/api/orders
  -Copy and past token in Authorization header: Bearer <key>
  -Send a raw JSON body. Example:
{
    "price" : 1000,
    "description" : "Your description"
} 

After sending request the notification service will write a log,
websocket service will show this message on web page
