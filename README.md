Yoti SDK Back-end test
======================

## Running the Project:
1) Clone the project to your local directory:
```javascript
git clone https://github.com/NoamGoren/yoti-sdk-backend-test.git
```
2) cd to the cloned project and run the following:

```javascript
mvn clean install spring-boot:run
```
3) Use the main endpoint http://localhost:8080/hoover for ```POST``` requests with body for example:

```javascript
{
  "roomSize" : [5, 5],
  "coords" : [1, 2],
  "patches" : [
    [1, 0],
    [2, 2],
    [2, 3]
  ],
  "instructions" : "NNESEESWNWW"
}
```

## EndPoints
```POST``` - http://localhost:8080/hoover.


## Technologies
1) Spring Boot - Used to deliver ready-to-run applications that don't require any configuration on your side.
2) H2 - H2 can be an in-memory database and it has been choosed because it's easier to implement and can easily be replaced with any implementation. it is automatically configured on startup based on the @Entity classes inside the project. Due to its nature, the database structure and data are lost when the server is stopped.


## Database
 The database information can be reached at http://localhost:8080/h2 Use the following credentials when prompted:
 
```javascript
Driver class: org.h2.Driver 
JDBC url: jdbc:h2:file:~/embedded_db
User name: sa
Password:  
```

## Assumptions
1) Lower cased letters are equivalent to uppercase (nnse is equal to NNSE).
2) If the hoover is running into a wall, the program will not crash, the last 'Good' position will be saved.
3) 400 HTTP status is returned for format or illegal input errors:

```javascript
{
  "error_code" : "FORMAT_NOT_VALID",
  "error_message" : "The string can only contain the following characters: N,S,W,E."
}
```

## Testing
 - There are 28 unit and integration tests. you can run it using the command:
```
mvn clean test
```

## Introduction
You will write a service that navigates a imaginary robotic hoover (much like a Roomba) through an equally imaginary room based on:

- room dimensions as X and Y coordinates, identifying the top right corner of the room rectangle. This room is divided up in a grid based on these dimensions; a room that has dimensions X: 5 and Y: 5 has 5 columns and 5 rows, so 25 possible hoover positions. The bottom left corner is the point of origin for our coordinate system, so as the room contains all coordinates its bottom left corner is defined by X: 0 and Y: 0.
- locations of patches of dirt, also defined by X and Y coordinates identifying the bottom left corner of those grid positions.
- an initial hoover position (X and Y coordinates like patches of dirt)
- driving instructions (as cardinal directions where e.g. N and E mean "go north" and "go east" respectively)

The room will be rectangular, has no obstacles (except the room walls), no doors and all locations in the room will be clean (hoovering has no effect) except for the locations of the patches of dirt presented in the program input.

Placing the hoover on a patch of dirt ("hoovering") removes the patch of dirt so that patch is then clean for the remainder of the program run. The hoover is always on - there is no need to enable it.

Driving into a wall has no effect (the robot skids in place).

## Goal
The goal of the service is to take the room dimensions, the locations of the dirt patches, the hoover location and the driving instructions as input and to then output the following:

- The final hoover position (X, Y)
- The number of patches of dirt the robot cleaned up

The service must persist every input and output to a database.

## Input
Program input will be received in a json payload with the format described here.

Example:

```javascript
{
  "roomSize" : [5, 5],
  "coords" : [1, 2],
  "patches" : [
    [1, 0],
    [2, 2],
    [2, 3]
  ],
  "instructions" : "NNESEESWNWW"
}
```

## Output
Service output should be returned as a json payload.

Example (matching the input above):

```javascript
{
  "coords" : [1, 3],
  "patches" : 1
}
```

Where `coords` are the final coordinates of the hoover and patches is the number of cleaned patches.

## Deliverable
The service:

- is a web service
- must run on Mac OS X or Linux (x86-64)
- must be written in any of the languages that we support with our SDKs (Java, C#, Python, Ruby, PHP, Node, Go)
- can make use of any existing open source libraries that don't directly address the problem statement (use your best judgement).

Send us:

- The full source code, including any code written which is not part of the normal program run (scripts, tests)
- Clear instructions on how to obtain and run the program
- Please provide any deliverables and instructions using a public Github (or similar) Repository as several people will need to inspect the solution

## Evaluation
The point of the exercise is for us to see some of the code you wrote (and should be proud of).

We will especially consider:

- Code organisation
- Quality
- Readability
- Actually solving the problem

This test is based on the following gist https://gist.github.com/alirussell/9a519e07128b7eafcb50

