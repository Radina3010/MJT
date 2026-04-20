# Todoist-like Task Manager

A console-based client-server task management application built with Java.

## Features

- User registration and authentication
- Task management (add, update, delete, complete)
- Dashboard showing today's tasks
- Collaboration support — shared projects with task assignment
- Data persistence in JSON format
- Multi-client server support

## Technologies

- Java
- Java NIO 
- JSON
- JUnit & Mockito (testing)

## Project Structure
```
src
└── bg.sofia.uni.fmi.mjt.taskmanager
    ├── client
    │   └── TaskManagerClient.java
    ├── command
    │   ├── handlers
    │   │     └── (...)
    │   ├── Command.java
    │   ├── CommandCreator.java
    │   ├── CommandExecutor.java
    │   ├── CommandRules.java
    │   └── CommandValidator.java
    ├── exception
    ├── extractors
    │   ├── BooleanExtractor.java
    │   ├── Extractor.java
    │   └── LocalDateExtractor.java
    ├── model
    │   ├── entity
    │   │     ├── Collaboration.java
    │   │     ├── Task.java
    │   │     ├── TaskKey.java
    │   │     └── User.java
    │   ├── repository
    │   │     ├── CollaborationRepository.java
    │   │     ├── PersonalTasksRepository.java
    │   │     └── UserRepository.java
    │   └── TaskManagerStorage.java
    └── server
        └── TaskManagerServer.java

test
└── bg.sofia.uni.fmi.mjt.taskmanager
     └── (...)
```
