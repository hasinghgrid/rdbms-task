# Cache System

## Overview
This project implements a flexible and efficient **cache system** that supports multiple **eviction policies** such as **Least Recently Used (LRU), Least Frequently Used (LFU), and Time-To-Live (TTL)**. It provides a generic caching mechanism with pluggable eviction strategies, ensuring scalability and performance optimization.

### Features:
- Supports **LRU, LFU, and TTL-based eviction policies**.
- Implements the **Strategy Pattern** to dynamically select eviction strategies.
- Follows **SOLID principles** for maintainability and extensibility.
- Includes **Static Code Analysis** using **SpotBugs, PMD, Checkstyle, and JaCoCo**.

## Design Principles and Patterns

### Strategy Pattern
The cache system follows the **Strategy Pattern** by encapsulating different eviction policies into separate classes, allowing dynamic selection of an eviction strategy at runtime via the **CacheFactory** class.

### SOLID Principles
- **Single Responsibility Principle (SRP)**: Each class has a single, well-defined responsibility (e.g., `GenericCache` manages storage, `EvictionPolicy` handles eviction logic, `CacheFactory` creates cache instances).
- **Open-Closed Principle (OCP)**: New eviction policies can be added without modifying the existing cache logic, as they extend the `EvictionPolicy<K>` interface.
- **Liskov Substitution Principle (LSP)**: The generic cache interacts with any eviction policy interchangeably without affecting functionality.
- **Interface Segregation Principle (ISP)**: The `Cache<K, V>` interface provides only essential cache operations, avoiding unnecessary dependencies.
- **Dependency Inversion Principle (DIP)**: The cache system depends on the abstraction (`EvictionPolicy<K>`) rather than concrete implementations of eviction policies.

## JDBC Integration

In addition to implementing various eviction policies for the cache system, I have also integrated **JDBC** for efficient database interaction. This implementation encapsulates the connection creation and usage in a utility class to streamline database operations. Here’s what was added:

- A **JDBC Utility Class**: This class handles the creation of database connections and provides methods for executing SQL queries with various argument types.
    - `void execute(String query, Object... args)`: Runs a query with no expected result (e.g., creating a table or inserting a record).
    - `void execute(String query, Consumer<PreparedStatement>)`: Runs a query with no expected result but allows greater flexibility by exposing the `PreparedStatement` to the caller.
    - `T findOne(String query, Function<ResultSet, T> mapper, Object... args)`: Runs a query and returns a single result. If more than one result is returned, it throws an exception.
    - `List<T> findMany(String query, Function<ResultSet, T> mapper, Object... args)`: Runs a query and returns multiple results as a list.

The repository uses **H2** in-memory database for testing with **PostgreSQL** as the target database. This integration allows seamless interaction with the cache system and ensures that the caching logic works effectively in conjunction with database operations.

### Comparison of Approaches
- **Approach 1** (`execute(String query, Object... args)`): This approach works well for simple queries where no flexibility is required, such as plain insertions or table creations.
- **Approach 2** (`execute(String query, Consumer<PreparedStatement>)`): This approach allows greater flexibility, exposing the `PreparedStatement` to the caller. However, it should be used carefully as it provides more control to the caller over the `PreparedStatement`.

## Static Code Analysis Reports

### 1. SpotBugs Report
Detects potential **bugs and performance issues** in the codebase.
![SpotBugs Screenshot](https://github.com/user-attachments/assets/9e3110d0-7802-4de2-bb37-d2598c978a07)

### 2. PMD Report
Analyzes **code style, best practices, and potential optimizations**.
![PMD Screenshot](https://github.com/user-attachments/assets/c956a98d-4d04-40b1-ad81-89fec4844424)

### 3. Checkstyle Report
Ensures **coding standard compliance** based on Java style guidelines.
![Checkstyle Screenshot](https://github.com/user-attachments/assets/d21c2da6-e12c-44ce-8a66-262dd72fa301)

### 4. JaCoCo Report
Measures **code coverage** to track test effectiveness.
![JaCoCo Screenshot](https://github.com/user-attachments/assets/5bc1d4e2-e983-4294-adfa-efd530c3552d)

## Code Coverage
![Code Coverage Screenshot](https://github.com/user-attachments/assets/c9463b91-8cbb-440a-9ebf-a21ad27fb574)

## Code Coverage After JDBC 
