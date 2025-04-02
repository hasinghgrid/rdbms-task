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

## Installation and Usage

### Prerequisites
- Java 17+
- Maven

### Clone the Repository
```bash
git clone https://github.com/hasinghgrid/cache-system.git
cd cache-system
```

### Build the Project
```bash
mvn clean install
```

### Run the Application
```bash
java -jar target/cache-system.jar
```

## Static Code Analysis Reports

### 1. SpotBugs Report
Detects potential **bugs and performance issues** in the codebase.
![SpotBugs Report](images/spotbugs_report.png)

### 2. PMD Report
Analyzes **code style, best practices, and potential optimizations**.
![PMD Report](images/pmd_report.png)

### 3. Checkstyle Report
Ensures **coding standard compliance** based on Java style guidelines.
![Checkstyle Report](images/checkstyle_report.png)

### 4. JaCoCo Report
Measures **code coverage** to track test effectiveness.
![JaCoCo Report](images/jacoco_report.png)

## Contributing
Feel free to submit issues or pull requests to improve the cache system!

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

