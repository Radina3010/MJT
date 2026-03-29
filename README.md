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
    ├── extractors.java
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
