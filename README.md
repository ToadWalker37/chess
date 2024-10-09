# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```

## Sequence Diagram Source Code: Phase 2

Note: Input into sequencediagram.org to view visually.

actor Client
participant Server
participant Handler
participant Service
participant DataAccess
database db

entryspacing 0.9
group #navy Registration #white
Client -> Server: [POST] /user\n{"username":" ", "password":" ", "email":" "}
Server --> Client: if request isn't UserData obj in proper JSON, 400\n{ "message": "Error: bad request" }
Server -> Handler: if request is UserData obj in proper JSON \n{"username":" ", "password":" ", "email":" "}
Handler -> Service: register(UserData)
Service -> DataAccess: getUser(UserData.username)
Service --> Handler:If error, RegisterResult:\n{ "message": "Error: getUser() failed" }
DataAccess -> db:Find UserData by username
DataAccess --> Service: dbResult
Service --> Handler: if dbResult not null\nRegisterResult: { "message": "Error: already taken" }
Service -> DataAccess:if dbResult is null\ncreateUser(userData)
DataAccess -> db:Add UserData
Service --> Handler:If error, RegisterResult:\n{ "message": "Error: createUser() failed" }
Service -> Service:createAuthToken(userName)
Service --> Handler: If error, RegisterResult:\n{ "message": "Error: createAuthToken() failed" }\nElse, RegisterResult:\n{ "username" : " ", "authToken" : " " }
Handler --> Server: RegisterResult
Server --> Client: if RegisterResult.message\ncase alreadytaken: 403, RegisterResult\ncase default: 500, RegisterResult\nelse 200, RegisterResult
end

group #orange Login #white
Client -> Server: [POST] /session\n{ "username":" ", "password":" " }
Server --> Client: if request isn't un/pw obj in proper JSON, 400\n{ "message": "Error: bad request" }
Server -> Handler: if request is un/pw obj in proper JSON \n{"username":" ", "password":" "}
Handler -> Service: login(LoginRequest)
Service -> DataAccess: getUser(LoginRequest.username)
DataAccess -> db:Find UserData by username
DataAccess --> Service: dbResult
Service --> Handler:If error, LoginResult:\n{ "message": "Error: getUser() failed" }
Service --> Handler: if dbResult null, LoginResult: \n{ "message": "Error: username invalid" }
Service -> Service:if dbResult not null\nauthenticateUser(password,userData)
Service --> Handler:If auth failed, LoginResult:\n{ "message": "Error: unauthorized" }
Service -> Service:createAuthToken(userName)
Service --> Handler: If error, LoginResult:\n{ "message": "Error: createAuthToken() failed" }\nElse, LoginResult:\n{ "username" : " ", "authToken" : " " }
Handler --> Server: LoginResult = LoginResult serialized
Server --> Client: if LoginResult.message\ncase unauthorized: 401, LoginResult\ncase default: 500, LoginResult\nelse 200, LoginResult
end

group #green Logout #white
Client -> Server: [DELETE] /session\nauthToken
Server -> Handler: authToken
Handler -> Service: logout(authToken)
Service -> Service: validateAuthToken(authToken)
Service --> Handler:If error, LogoutResult:\n{ "message": "Error: unauthorized" }
Service -> Service:deleteAuthToken(authToken)
Service --> Handler: If error, LogoutResult:\n{ "message": "Error: deleteAuthToken() failed" }\nElse, LogoutResult: {}
Handler --> Server: LogoutResult = LogoutResult serialized
Server --> Client: if LogoutResult.message\ncase unauthorized: 401, LogoutResult\ncase default: 500, LogoutResult\nelse 200, LogoutResult
end

group #red List Games #white
Client -> Server: [GET] /game\nauthToken
Server -> Handler: authToken
Handler -> Service: listGames(authToken)
Service -> Service: validateAuthToken(authToken)
Service --> Handler:If error, ListResult:\n{ "message": "Error: unauthorized" }
Service -> DataAccess: getGames()
DataAccess -> db: Get all games
Service --> Handler:If error, ListResult:\n{ "message": "Error: getGames() failed" }
DataAccess --> Service: ListResult: GameData object list
Service --> Handler: ListResult
Handler --> Server:ListResult = ListResult serialized
Server --> Client: if ListResult.message\ncase unauthorized: 401, ListResult\ncase default: 500, ListResult\nelse 200, ListResult
end

group #purple Create Game #white
Client -> Server: [POST] /game\nauthToken\n{ "gameName":" " }
Server --> Client: if request isn't gameName in proper JSON\n400 { "message": "Error: bad request" }
Server -> Handler: if request is gameName in proper JSON \n{"gameName":" "}, authToken
Handler -> Service: makeGame(authToken)
Service -> Service: validateAuthToken(authToken)
Service --> Handler:If error, CreateResult:\n{ "message": "Error: unauthorized" }
Service -> DataAccess: createGame(gameName)
DataAccess -> db: Create game
DataAccess --> Service: CreateResult
Service --> Handler:If error, CreateResult:\n{ "message": "Error: createGame() failed" }\nElse, CreateResult: { "gameID": 1234 }
Handler --> Server: CreateResult = CreateResult serialized
Server --> Client: if CreateResult.message\ncase unauthorized: 401, CreateResult\ncase default: 500, CreateResult\nelse 200, CreateResult
end

group #yellow Join Game #black
Client -> Server: [PUT] /game\nauthToken\n{ "playerColor":"WHITE/BLACK", "gameID": 1234 }
Server --> Client: if request isn't correct object in proper JSON\n400 { "message": "Error: bad request" }
Server -> Handler: if request is correct object in proper JSON \n"playerColor":"WHITE/BLACK", "gameID": 1234 }, authToken
Handler -> Service: joinGame(authToken)
Service -> Service: validateAuthToken(authToken)
Service --> Handler:If error, JoinResult:\n{ "message": "Error: unauthorized" }
Service -> DataAccess: getGame(gameID)
DataAccess -> db: Get game
DataAccess --> Service: game
Service --> Handler:If no game, JoinResult:\n{ "message": "Error: invalid game" }
Service -> Service: validatePlayer(playerColor)
Service --> Handler:If error, JoinResult:\n{ "message": "Error: already taken" }
Service -> DataAccess: addPlayer(playerColor, gameID)
DataAccess -> db: Update game with player
Service --> Handler:If error, JoinResult:\n{ "message": "Error: addPlayer() failed" }\nElse, JoinResult: {}
Handler --> Server: JoinResult = JoinResult serialized
Server --> Client: if JoinResult.message\ncase unauthorized: 401, \ncase alreadytaken: 403, JoinResult\ncase default: 500, JoinResult\nelse 200, JoinResult
end

group #gray Clear application #white
Client -> Server: [DELETE] /db
Server -> Handler: {"action": "delete"}
Handler -> Service: delete()
Service -> DataAccess: delete()
DataAccess -> db: Delete everything from database
Service --> Handler: if success, DeleteResult: {}\nElse, DeleteResult: { "message": "Error: clear failed" }
Handler --> Server: DeleteResult = DeleteResult serialized
Server --> Client: if DeleteResult.message\n500, { "message": "Error: (description of error)" }\nelse, 200 {}
end