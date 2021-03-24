# Coding Exercise

## Design
There are 3 components: Client, API server, Hardware Simulator  
Client is implemented with HTML and vanilla Javascript, API server and Hardware Simulator in Java  
Client and API server communicate using Sever Sent Events. On start, an event source is created to continuosly capture data from server. On stop, a request is sent to stop endpoint and event source is closed.  
API server and Hardware Simulator communicate using a simple text based protocol over Java sockets. The supported commands are `START TimeInSeconds` (e.g `START 5`) and `STOP`. The connection is kept open and data is sent until given time in seconds is elapsed or STOP command is given.


## Assumptions/Limitations
- Hardware simulator listens to connections on a specific IP/port, assuming the hardware does the same.
- The client code is part of the API server project since they are static files. They could be bundled with webpack and deployed on their own server. To keep the running of the application simple, they are served using the same web server for now
- The display on client side can be cleaned up with styling
- We could use websockets for production code but since the given use case is simple, server sent events are used to push data to the client
- Error handling and documentation in code can be improved
- Due to time constraints, I did not include a docker compose configuration to simplify deployments  

## Prerequisites
Java Version 11 [https://adoptopenjdk.net/]  
Maven [https://maven.apache.org/install.html]

## Configuration
- The hardware simulator can be configured to run on a specific port via command line (default: 4444)
- The API server can be configured to run on a specific port (default: 10000)
- We can specify the location (IP/Port) of the hardware in `application.properties` on the API server to connect to a simulator or real hardware (default: 127.0.0.1/4444)

## Running the application
1. Install any prerequisites
2. Download the code
3. Open a terminal and navigate to the code folder
3. Enter the following in the terminal to create a jar file and run the hardware simulator  
`cd simulator`  
`mvn clean install`  
`java -jar target/simulator-1.0-SNAPSHOT-jar-with-dependencies.jar 4444`  
4. Open a new terminal and navigate to the code folder
5. Enter the following in the terminal to start the API server  
`cd api-server`  
`mvn spring-boot:run`  
6. Open a browser and enter the following  
`http://localhost:10000/`
7. Enter a time in seconds and click 'Start' button

