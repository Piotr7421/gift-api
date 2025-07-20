# Kid API Project

## Project Overview

The Kid API project is a comprehensive RESTful API designed to manage and record gifts for kids. The application leverages modern Spring Boot architecture with advanced design patterns including Strategy Pattern for flexible kid creation and asynchronous processing capabilities for efficient data import operations.

## Project Evolution

### Original Project Scope

The project began with a simple objective: **to record gifts for kids** with basic CRUD operations and fundamental business rules.

#### Initial Model Design
- **Kid**: Basic entity with name, surname, and date of birth
- **Gift**: Simple entity with name and price

#### Core Business Rule
- **Gift Limitation**: Each kid can have no more than 3 gifts

#### Original Features
**Kid Management:**
- Add a kid
- Download all kids
- Download a specific kid
- Update a kid
- Delete a kid

**Gift Management:**
- Add a gift for a certain kid
- Download gifts of a certain kid
- Download a specific gift from a specific kid
- Update a particular kid's gift
- Remove a gift from a particular kid

### Architectural Evolution

The project has evolved significantly from its original scope, demonstrating modern software engineering principles and advanced architectural patterns:

#### SOLID Principles Implementation

**Open/Closed Principle (OCP) - The "O" in SOLID:**
The most significant evolution was the introduction of the **Strategy Package**, which exemplifies the Open/Closed Principle:
- **Open for Extension**: New kid creation strategies can be added without modifying existing code
- **Closed for Modification**: Existing strategy implementations remain unchanged when new strategies are introduced
- **Extensibility**: The framework allows unlimited strategy implementations through the common interface

#### Object-Oriented Programming Enhancement

**Inheritance Implementation:**
The project evolved from a simple Kid entity to a sophisticated inheritance hierarchy:
- **Base Class**: Kid serves as the parent class with common attributes
- **Specialized Classes**: Boy and Girl inherit from Kid with specific characteristics
- **Single Table Inheritance**: Database strategy supporting polymorphic queries
- **Type Safety**: Compile-time validation of kid types through inheritance

#### Advanced Features Integration

**Asynchronous Data Processing:**
- **CSV Import Capability**: High-performance file processing for bulk data import
- **Non-blocking Operations**: Asynchronous processing preventing application thread blocking
- **Task Queue Management**: Sophisticated queue handling for concurrent file processing
- **Performance Optimization**: JdbcTemplate for efficient bulk database operations

**Aspect-Oriented Programming (AOP):**
- **Cross-Cutting Concerns**: Separation of logging and monitoring from business logic
- **AutoLogger Implementation**: Automatic audit trail for kid creation operations
- **Non-Intrusive Monitoring**: Business logic remains clean while adding observability
- **Compliance Support**: Automated tracking for regulatory requirements

### Technical Architecture Advancement

#### From Simple CRUD to Enterprise Architecture
The project transformation demonstrates several key architectural improvements:

**Design Pattern Integration:**
- **Strategy Pattern**: Flexible kid creation with runtime strategy selection
- **Repository Pattern**: Clean data access abstraction
- **Command Pattern**: Encapsulated request handling
- **Aspect-Oriented Programming**: Cross-cutting concern management

**Enterprise-Grade Features:**
- **Database Migration**: Liquibase for schema versioning and deployment
- **Object Mapping**: MapStruct for efficient DTO-Entity conversion
- **Concurrency Control**: Both optimistic and pessimistic locking strategies
- **Build Automation**: Maven with annotation processing support

**Performance and Scalability:**
- **Asynchronous Processing**: Non-blocking operations for improved throughput
- **Connection Pooling**: Optimized database resource management
- **Batch Processing**: Efficient bulk operations for large datasets
- **Strategic Locking**: Appropriate concurrency control based on use cases

### Business Logic Evolution

#### Enhanced Gift Management
While maintaining the core business rule of **maximum 3 gifts per kid**, the implementation now includes:
- **Pessimistic Locking**: Ensures gift limit enforcement in high-concurrency scenarios
- **Audit Logging**: Automatic tracking of gift-related operations
- **Type-Specific Logic**: Different gift handling based on kid types (Boy/Girl)

#### Extensible Kid Types
The evolution from a single Kid entity to specialized types enables:
- **Boy-Specific Attributes**: Pants length and other male-specific characteristics
- **Girl-Specific Attributes**: Skirt color and other female-specific characteristics
- **Future Extensibility**: Easy addition of new kid types without code modification

### Development Methodology Improvements

#### Code Quality Enhancements
- **Lombok Integration**: Reduced boilerplate code for better maintainability
- **MapStruct Usage**: Type-safe mapping with compile-time validation
- **Comprehensive Testing**: Multiple testing strategies including H2 for integration tests

#### DevOps and Deployment
- **Spring Boot DevTools**: Enhanced development experience with hot reloading
- **Executable JAR Packaging**: Simplified deployment with embedded server
- **Environment Configuration**: Flexible configuration management with YAML

### Future-Proof Architecture

The current architecture provides a solid foundation for future enhancements:
- **Microservices Ready**: Modular design supporting service decomposition
- **Cloud Native**: Configuration and deployment patterns suitable for cloud environments
- **Event-Driven Capability**: AOP foundation enabling event-driven architecture
- **API Documentation Ready**: Structure supporting OpenAPI/Swagger integration

This evolution demonstrates how a simple CRUD application can be transformed into an enterprise-grade system while maintaining backward compatibility and adhering to software engineering best practices.

## Features

### Core Kid Management
- **Create a Kid**: Add new kids to the system with various creation strategies
- **Retrieve All Kids**: Get a complete list of all registered kids
- **Retrieve Specific Kid**: Fetch detailed information about a particular kid
- **Update Kid Information**: Modify existing kid data
- **Delete Kid**: Remove a kid from the system

### Gift Management
- **Add Gift**: Associate gifts with specific kids
- **Retrieve Kid's Gifts**: Get all gifts belonging to a particular kid
- **Retrieve Specific Gift**: Fetch details of a particular gift for a specific kid
- **Update Gift**: Modify gift information for a particular kid
- **Remove Gift**: Delete a gift from a particular kid's collection

### Strategy-Based Kid Creation
- **Multiple Creation Strategies**: Support for different kid creation approaches through the Strategy Pattern
- **Dynamic Strategy Selection**: Runtime selection of creation strategy based on request type
- **Extensible Strategy Framework**: Easy addition of new creation strategies without modifying existing code

### Advanced Data Import
- **Asynchronous CSV Import**: High-performance import of kid data from CSV files
- **Batch Processing**: Efficient database insertion using JdbcTemplate for optimal performance
- **Task Queue Management**: Managed asynchronous processing with ThreadPoolTaskExecutor
- **File-Based Data Loading**: Import from plik.csv located in the resources directory

### Cross-Cutting Concerns
- **Automatic Logging**: Aspect-oriented logging for kid creation operations
- **Transparent Monitoring**: Non-intrusive logging without modifying business logic
- **Audit Trail**: Automatic tracking of important business operations

## Technical Stack

### Core Frameworks
- **Spring Boot 3.5.3**: Main application framework for RESTful API development
- **Spring MVC**: HTTP request/response handling and REST controller management
- **Spring Data JPA**: Object-Relational Mapping and database interactions
- **Spring JDBC**: High-performance database operations for bulk data import
- **Spring Validation**: Request validation and data integrity
- **Spring Async**: Asynchronous processing capabilities for background tasks
- **Spring AOP**: Aspect-oriented programming for cross-cutting concerns

### Build and Project Management
- **Maven**: Build automation and dependency management tool
- **Maven Compiler Plugin**: Java compilation with annotation processing support
- **Spring Boot Maven Plugin**: Application packaging and execution

### Data Access Technologies
- **Jakarta Persistence (JPA)**: Entity management and ORM operations
- **JdbcTemplate**: High-performance database operations for bulk data import
- **Hibernate**: JPA implementation for database operations
- **MySQL Connector**: Production database connectivity
- **H2 Database**: In-memory database for testing

### Database Migration and Schema Management
- **Liquibase**: Database schema versioning and migration management
- **XML-based Changesets**: Structured database change tracking
- **Automated Schema Evolution**: Version-controlled database updates

### Object Mapping and Code Generation
- **MapStruct 1.6.3**: Compile-time object mapping between DTOs and entities
- **Annotation Processing**: Automatic mapper implementation generation
- **Type-safe Mapping**: Compile-time validation of object mappings

### Development Tools
- **Lombok**: Boilerplate code reduction through annotations
- **Spring Boot DevTools**: Development-time enhancements and hot reloading
- **SLF4J**: Logging framework integration

### Testing Framework
- **Spring Boot Test**: Comprehensive testing support with test slices
- **JUnit**: Unit testing framework
- **H2 Database**: In-memory testing database

## Application Architecture

### Architectural Patterns

#### MVC (Model-View-Controller)
- **Controllers**: REST endpoints handling HTTP requests (KidController, KidStrategyController)
- **Services**: Business logic implementation (KidService, KidStrategyService)
- **Models**: Data entities and command objects (Kid, CreateKidCommand, CreateKidStrategyCommand)
- **Repositories**: Data access layer (KidRepository)

#### Layered Architecture
The application follows a traditional layered architecture pattern:

- **Controller Layer**: REST endpoints handling HTTP requests
- **Service Layer**: Business logic implementation
- **Repository Layer**: Data access abstraction
- **Database Layer**: MySQL/H2 with Liquibase schema management

#### Cross-Cutting Concerns Layer
- **Aspect Layer**: AOP-based logging and monitoring
- **Security Layer**: Authentication and authorization (future enhancement)
- **Caching Layer**: Performance optimization (future enhancement)

## Design Patterns Implementation
  
### Strategy Pattern
- **Interface**: KidCreationStrategy defines the contract for kid creation strategies
- **Context**: KidStrategyService manages strategy selection and execution
- **Concrete Strategies**: Multiple implementations for different kid creation approaches
- **Strategy Selection**: Dynamic strategy resolution using Spring's dependency injection with Map-based strategy registry

### Aspect-Oriented Programming (AOP)

The application implements AOP through the AutoLogger aspect to handle cross-cutting concerns:

#### AutoLogger Aspect Implementation
- **Aspect Class**: AutoLogger handles automatic logging for kid creation operations
- **Pointcut Definition**: Targets specific methods in KidService for logging
- **Advice Type**: After advice executes logging after successful kid creation
- **Parameter Binding**: Captures method parameters for detailed logging

#### AOP Configuration
- **EnableAspectJAutoProxy**: Enables AspectJ-based AOP in the main application class
- **Component Registration**: AutoLogger is registered as a Spring component
- **Weaving**: Runtime proxy-based weaving for aspect execution

#### Logging Strategy
- **Target Operations**: Focuses on kid creation operations in KidService.save method
- **Parameter Extraction**: Logs relevant information from CreateKidCommand
- **Non-Intrusive**: Business logic remains unchanged while adding logging capability
- **SLF4J Integration**: Uses standard logging framework for consistent log output

#### Benefits of AOP Implementation
- **Separation of Concerns**: Logging logic is separated from business logic
- **Code Reusability**: Logging aspect can be applied to multiple methods
- **Maintainability**: Changes to logging behavior don't affect business code
- **Consistency**: Uniform logging approach across the application
- **Performance**: Minimal overhead through proxy-based implementation

#### AOP Use Cases in the Project
- **Audit Logging**: Automatic tracking of kid creation events
- **Business Monitoring**: Monitoring of critical business operations
- **Debugging Support**: Enhanced debugging capabilities through detailed logging
- **Compliance**: Audit trail for regulatory requirements

#### Future AOP Enhancements
- **Performance Monitoring**: Method execution time tracking
- **Security Logging**: Authentication and authorization event logging
- **Error Handling**: Centralized exception logging and handling
- **Caching**: Aspect-based caching for frequently accessed data

### Mapper Pattern (MapStruct)
- **Compile-time Mapping**: Automatic generation of mapping code between entities and DTOs
- **Type Safety**: Compile-time validation of mapping configurations
- **Performance Optimization**: Generated code without reflection overhead
- **Custom Mapping Logic**: Support for complex mapping scenarios

### Dependency Injection Pattern
- **Constructor Injection**: Used throughout the application for better testability
- **Service Registration**: Automatic strategy registration through Spring's component scanning

### Repository Pattern
- **Data Abstraction**: KidRepository abstracts database operations
- **JPA Integration**: Seamless integration with Spring Data JPA

### Command Pattern
- **Command Objects**: CreateKidStrategyCommand encapsulates request parameters
- **Parameter Mapping**: Flexible parameter handling through Map-based approach

## Database Design and Migration

### Liquibase Database Migration
The project uses Liquibase for database schema management with structured changesets.

#### Master Changelog Structure
The master changelog includes references to individual changeset files:
- Kid table creation changeset
- Gift table creation changeset
- Foreign key relationship establishment

#### Migration Features
- **Version Control**: All database changes are tracked and versioned
- **Rollback Support**: Ability to rollback database changes
- **Environment Consistency**: Same schema across development, testing, and production
- **Automated Deployment**: Database updates during application startup

### Table Structure

#### Kid Table
Core entity with inheritance strategy support containing:
- Primary key with auto-increment
- Basic information fields (first_name, last_name, birth_date)
- Discriminator column (kid_type) for inheritance
- Version field for optimistic locking
- Type-specific columns for different kid types

#### Gift Table
Gift management with foreign key relationships containing:
- Primary key with auto-increment
- Gift details (name, price)
- Foreign key reference to Kid table
- Version field for optimistic locking

### Entity Inheritance Strategy
- **Single Table Inheritance**: Kid entity uses SINGLE_TABLE inheritance strategy
- **Discriminator Column**: kid_type column differentiates between kid types
- **Polymorphic Queries**: Support for querying different kid types through base class

### Concurrency Control and Locking Strategies

#### Optimistic Locking
- **Version Control**: Uses @Version annotation for automatic version management
- **Conflict Detection**: Detects conflicts at commit time using version fields
- **Low Contention Scenarios**: Ideal when conflicts are rare and concurrent reads are common
- **Implementation**: findWithLockingById method in KidRepository

#### Pessimistic Locking
- **Exclusive Access**: Creates database-level locks preventing other transactions from accessing the entity
- **High Contention Scenarios**: Ensures data consistency when multiple transactions are likely to modify the same entity
- **Implementation**: findWithPessimisticLockingById method in KidRepository

**Use Cases for Pessimistic Locking:**
- Gift limit validation when checking if a kid can receive additional gifts
- Critical data updates where data consistency is paramount
- High-contention operations with multiple users modifying the same kid simultaneously

**Benefits:**
- Guarantees exclusive access to the entity
- Prevents phantom reads and ensures transaction isolation
- Critical for operations like gift limit enforcement

**Considerations:**
- Reduced concurrency and potential performance impact
- Risk of deadlocks if not used carefully
- Possible lock timeout exceptions

### Entity Relationships
- **One-to-Many**: Kid to Gifts relationship with bidirectional mapping
- **Cascade Operations**: Proper cascade configuration for entity lifecycle management
- **Foreign Key Constraints**: Database-level referential integrity

## Build Configuration

### Maven Configuration
The project uses Maven for build automation with comprehensive annotation processing support.

#### Annotation Processing
- **Lombok**: Automatic generation of boilerplate code
- **MapStruct**: Compile-time mapper generation with type safety

#### Build Features
- **Lombok Processing**: Automatic generation of getters, setters, constructors
- **MapStruct Processing**: Compile-time mapper generation
- **Spring Boot Packaging**: Executable JAR creation with embedded server
- **Development Tools**: Hot reloading and development enhancements

### Application Configuration
The project uses application.yaml for configuration management covering:

#### Database Configuration
- MySQL connection settings for production
- H2 in-memory database for testing
- Connection pooling optimization

#### Liquibase Configuration
- Master changelog location
- Automatic migration enablement
- Environment-specific settings

#### JPA Configuration
- Hibernate dialect configuration
- Performance optimization settings
- Entity scanning configuration

#### AOP Configuration
- AspectJ auto-proxy enablement in main application class
- Aspect component scanning and registration
- Proxy-based weaving configuration

## Object Mapping with MapStruct

### Mapping Strategy
- **Entity to DTO Mapping**: Automatic conversion between database entities and API DTOs
- **Compile-time Generation**: Mappers are generated during compilation
- **Custom Mapping Rules**: Support for complex field transformations
- **Null Handling**: Configurable null value strategies

### Mapper Implementation
MapStruct generates implementation classes automatically based on interface definitions, providing:
- Type-safe mapping between entities and DTOs
- Compile-time validation of mapping configurations
- Performance optimization through direct field access
- Support for complex mapping scenarios

## Performance Optimizations

### Asynchronous Processing
- **Non-blocking Operations**: CSV import doesn't block main application thread
- **Task Queue**: Managed queue for handling multiple import requests
- **Thread Pool Management**: Configurable thread pool for optimal resource utilization

### Database Optimizations
- **Batch Processing**: JdbcTemplate for efficient bulk insertions
- **Connection Pooling**: Optimized database connection management
- **Lazy Loading**: Efficient entity relationship loading
- **Liquibase Performance**: Optimized changelog execution
- **Strategic Locking**: Appropriate use of optimistic vs pessimistic locking based on use case

### Mapping Performance
- **Compile-time Generation**: MapStruct eliminates reflection overhead
- **Direct Field Access**: Generated mappers use direct field assignment
- **Minimal Object Creation**: Efficient memory usage in mapping operations

### AOP Performance Considerations
- **Proxy-based Weaving**: Minimal runtime overhead through Spring's proxy mechanism
- **Selective Pointcuts**: Targeted advice application to avoid unnecessary interception
- **Efficient Logging**: Optimized logging operations to minimize performance impact

## Development Workflow

### Database Migration Workflow
1. **Create Changeset**: Add new XML changeset file
2. **Update Master**: Include new changeset in master changelog
3. **Test Migration**: Verify changes in development environment
4. **Deploy**: Automatic migration during application startup

### Build Process
Standard Maven lifecycle with annotation processing:
- Clean and compile with annotation processing
- Run tests with H2 database
- Package application into executable JAR
- Run application with Spring Boot Maven plugin

### AOP Development Workflow
1. **Identify Cross-Cutting Concerns**: Determine which concerns should be handled by aspects
2. **Define Pointcuts**: Specify where aspects should be applied
3. **Implement Advice**: Create the logic to be executed at pointcuts
4. **Test Aspect Behavior**: Verify that aspects work correctly without affecting business logic

## API Endpoints

### Strategy-Based Kid Creation
POST endpoint for creating kids using different strategies:
- Accepts strategy type and parameters
- Dynamic strategy selection based on request
- Flexible parameter handling through Map-based approach
- Automatic logging through AOP when kids are created

### CSV Data Import
POST endpoint for asynchronous CSV import:
- Processes plik.csv from resources directory
- Asynchronous processing with task queue management
- High-performance insertion using JdbcTemplate

## File Structure

### Source Code Organization
- **Main Package**: pl.spring.giftapi
- **Model Package**: Entity definitions and data models
- **Repository Package**: Data access layer interfaces
- **Mapper Package**: MapStruct mapper interfaces
- **Strategy Package**: Strategy pattern implementation
- **Aspect Package**: AOP aspects for cross-cutting concerns

### Resources Organization
- **Configuration**: application.yaml for environment settings
- **Data Files**: plik.csv for import functionality
- **Database**: Liquibase changelog files and migration scripts

### Generated Sources
- **Annotation Processing**: MapStruct generated mappers
- **Target Directory**: Compiled classes and generated sources

## Getting Started

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher
- MySQL Database (or H2 for testing)

### Setup Instructions

#### Repository Setup
1. Clone the repository
2. Navigate to project directory

#### Database Setup
1. Create MySQL database named gift_api
2. Configure connection settings in application.yaml

#### Application Build
1. Clean and compile project with Maven
2. Run tests to verify setup
3. Package application into executable JAR

#### Application Execution
1. Run application using Spring Boot Maven plugin
2. Verify database schema creation by Liquibase
3. Confirm MapStruct mapper generation
4. Verify AOP aspect registration and functionality
5. Access application at localhost:8080

### CSV Import Format
The plik.csv file should contain kid data with columns:
- firstName: Child's first name
- lastName: Child's last name
- birthDate: Date of birth in YYYY-MM-DD format
- kidType: Type of kid (STANDARD, PREMIUM, etc.)

## Future Enhancements
- Additional kid creation strategies
- Real-time import progress tracking
- Enhanced error handling for CSV processing
- REST API documentation with OpenAPI/Swagger
- Caching layer for improved performance
- Event-driven architecture for gift notifications
- Database migration rollback procedures
- Advanced MapStruct mapping configurations
- Lock timeout configuration and monitoring
- Deadlock detection and resolution strategies
- Extended AOP functionality for performance monitoring
- Security aspects for authentication and authorization
- Centralized exception handling through AOP

## Contributing
1. Fork the repository
2. Create a feature branch
3. Implement changes following existing patterns
4. Add appropriate Liquibase changesets for database changes
5. Ensure MapStruct mappers are properly configured
6. Consider concurrency implications and appropriate locking strategies
7. Follow AOP best practices for cross-cutting concerns
8. Add appropriate tests including aspect testing
9. Submit a pull request

## License
This project is licensed under the MIT License.


