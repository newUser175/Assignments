# Algebraic Equation Solver

A Spring Boot web application that stores and evaluates algebraic equations using postfix trees (expression trees). The application provides RESTful APIs for storing equations, retrieving them, and evaluating them with variable substitution.

## Features

- **Store Algebraic Equations**: Parse and save equations in postfix tree structure
- **Retrieve Stored Equations**: Fetch list of stored equations reconstructed from postfix trees
- **Evaluate Equations**: Substitute variable values and calculate results using postfix trees
- **Web Interface**: Clean HTML/CSS/JS frontend for easy interaction
- **Comprehensive Testing**: Unit tests with high coverage
- **Error Handling**: Robust validation and error messages

## Technology Stack

- **Backend**: Spring Boot 3.2.0, Java 17
- **Frontend**: HTML5, CSS3, JavaScript (ES6+)
- **Testing**: JUnit 5, Spring Boot Test
- **Build Tool**: Maven
- **Code Coverage**: JaCoCo

## API Endpoints

### 1. Store an Algebraic Equation
- **URL**: `/api/equations/store`
- **Method**: `POST`
- **Request Body**:
```json
{
  "equation": "3x + 2y - z"
}
```
- **Response**:
```json
{
  "message": "Equation stored successfully",
  "equationId": "1"
}
```

### 2. Retrieve Stored Equations
- **URL**: `/api/equations`
- **Method**: `GET`
- **Response**:
```json
{
  "equations": [
    {
      "equationId": "1",
      "equation": "3x + 2y - z"
    },
    {
      "equationId": "2",
      "equation": "x^2 + y^2 - 4"
    }
  ]
}
```

### 3. Evaluate an Equation
- **URL**: `/api/equations/{equationId}/evaluate`
- **Method**: `POST`
- **Request Body**:
```json
{
  "variables": {
    "x": 2,
    "y": 3,
    "z": 1
  }
}
```
- **Response**:
```json
{
  "equationId": "1",
  "equation": "3x + 2y - z",
  "variables": {
    "x": 2,
    "y": 3,
    "z": 1
  },
  "result": 11
}
```

### 4. Health Check
- **URL**: `/api/equations/health`
- **Method**: `GET`
- **Response**: `"Equation Solver API is running!"`

## Setup Instructions

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher

### Running the Application

1. **Clone or navigate to the project directory**:
```bash
cd equation-solver
```

2. **Build the project**:
```bash
./mvnw clean compile
```

3. **Run tests**:
```bash
./mvnw test
```

4. **Generate test coverage report**:
```bash
./mvnw jacoco:report
```
Coverage report will be available at `target/site/jacoco/index.html`

5. **Run the application**:
```bash
./mvnw spring-boot:run
```

6. **Access the application**:
- Web Interface: http://localhost:8081
- API Base URL: http://localhost:8081/api/equations

## Testing with Postman

### Store Equation
```
POST http://localhost:8081/api/equations/store
Content-Type: application/json

{
  "equation": "3x + 2y - z"
}
```

### Get All Equations
```
GET http://localhost:8081/api/equations
```

### Evaluate Equation
```
POST http://localhost:8081/api/equations/1/evaluate
Content-Type: application/json

{
  "variables": {
    "x": 2,
    "y": 3,
    "z": 1
  }
}
```

## Supported Operations

The application supports the following mathematical operations:
- Addition (`+`)
- Subtraction (`-`)
- Multiplication (`*`)
- Division (`/`)
- Exponentiation (`^`)
- Parentheses for grouping

## Example Equations

- `3x + 2y - z`
- `x^2 + y^2 - 4`
- `2a * b + c / d`
- `(x + y) * z`
- `x + y * z - w^2`

## Architecture

### Core Components

1. **ExpressionParser**: Converts infix expressions to postfix trees using the Shunting Yard algorithm
2. **EquationEvaluator**: Evaluates postfix trees with variable substitution
3. **EquationService**: Main business logic for storing and managing equations
4. **EquationController**: REST API endpoints
5. **TreeNode**: Represents nodes in the postfix expression tree

### Design Patterns Used

- **Service Layer Pattern**: Separation of business logic
- **Repository Pattern**: Data access abstraction (in-memory storage)
- **DTO Pattern**: Data transfer objects for API communication
- **Builder Pattern**: For creating complex objects
- **Strategy Pattern**: For different node types in expression tree

## Error Handling

The application provides comprehensive error handling for:
- Invalid equation syntax
- Missing variables during evaluation
- Division by zero
- Malformed requests
- Non-existent equation IDs

## Test Coverage

The application includes comprehensive test coverage:
- Unit tests for all service classes
- Integration tests for controllers
- Edge case testing
- Error scenario testing

Run tests with coverage:
```bash
./mvnw clean test jacoco:report
```

## Future Enhancements

- Support for more mathematical functions (sin, cos, log, etc.)
- Persistent storage with database
- User authentication and equation ownership
- Equation history and versioning
- Mathematical expression validation improvements
- Support for complex numbers

## Contributing

1. Follow the existing code style and patterns
2. Write comprehensive tests for new features
3. Update documentation for API changes
4. Ensure all tests pass before submitting changes

## License

This project is developed as part of a technical assignment and is for educational purposes.