# Sharing service

This is a microservice for sharing management of spreadsheets. The service allows to share/unshare documents, separate sheets or separate cells of Excel files. It also provides the possibility to load all available content for a user by document id. 

The service logic assumes that:
- each document has a unique identifier which is unique within the whole system
- each sheet has its own identifier, and it is unique within the document
- each cell has its own identifier, and it is unique within the document

The service doesn't know about a creator and doesn't check the creator's permission (if he has access to items and can share/unshare them). Based on the business requirements only a document creator can grant access to other users/groups. If we want to change it then we need to modify the service and add an extra layer (service) to check permissions.

To share items the service doesn't use either user emails or user ids. It uses `shareeIds`. Here a brief explanation what's this:
- During the creation of a user/company the item gets id and one extra field named shareeId. The field is constant and never changes.
- Certain users can have a role which allows them to create new groups (like "sales", "developers", "management"...) and put users or other groups there
- During a group creation, each group also gets shareeId 
- When a creator shares content he sees all available groups/users and can choose them to share. For example he can select groups "sales", "developers" and separate user/users like "Bill Gates". Since all of them has unique `shareeIds` we can share content using the field

### Basic principles how to share/unshare content
   
   There is "SharingRequest" object to share/unshare content.
   Using the object it's possible to share a whole document or individual sheets/cells:
   - If the only `documentId` is provided and `sheetCellIds` field is absent or empty then all document will be shared/unshared 
   - If a `sheetCellIds` entry contains the only key which is `sheetId` and value is empty then the whole sheet will shared/unshared
   - If a `sheetCellIds` contains a list of values which are `cellId` then these particular cells will be shared/unshared for the sheet 
   
### Basic principles how to load shared content
    
- To request available for a user content along with `documentId` need to provide a list of all available for the user `shareeIds` 
- The service will return optional `SharedDocument` object
- If the object is absent the document is totally unavailable
- If the object has only `documentId` field and `sheetCellIds` field is empty this means all document is shared for provided shareeIds
- If `sheetCellIds` is not empty and an entry contains the only key which is `sheetId` and value is empty then the whole sheet is shared
- If a `sheetCellIds` contains a list of values which are `cellId` then these particular cells are shared for the sheet

## Structure of the service

The service is written in Java 11 and based on spring-boot 2 framework. Maven is using for building the project. The service contains 3 modules:

- `sharing-service-client`: this is a java client which can be imported and used in another java application for simple communication with the service 
- `sharing-service-common`: common module which contains DTOs and constants needed for `client` ans `ws` modules 
- `sharing-service-ws`: REST web service

## How to build and run the service

### Using an IDE

1. Clone or download the repository
2. Open the project in your favorite IDE (e.q. Intellij Idea)
3. The service does not use a database. For simplification, it stores all content in memory. Hence it is not necessary to install something else 
4. Run spring-boot `Application` class of `sharing-service-ws` module. The service will be running on the default port `8080`

### Using command line

1. Clone or download the repository
2. Build the project using `mvn clean package'
3. The service does not use a database. For simplification, it stores all content in memory. Hence it is not necessary to install something else
4. Start the service: `java -jar sharing-service-ws/target/sharing-service-ws-1.0-SNAPSHOT.jar`
5. Alternatively, run the app using Docker: `docker-compose up` 


## How to use the service

1. Open swagger-api ui: `http://localhost:8080/swagger-ui.html`
2. Here you can see all available endpoints and models and try to execute examples.

## HTTP java client

If you need to use the service from another java program, you can use client provided by the service. The usage is pretty simple. Just build and import maven module `sharing-service-client`. If your service is using Spring you need to import the module in your configuration: `@Import(SharingServiceClientConfig.class)` and override the service base url if necessary: `-Dsharing.service.base.url=YOUR_SERVICE_URL`. If you use plain java program you can create the client by your self. See a usage example in `SharingServiceClientUsage`. Also using the example you can try how the service works. Just open it in your IDEA and run `main` method (Note: the service should be running).

### TODO list

- Add requests validation
- Add errors handling
