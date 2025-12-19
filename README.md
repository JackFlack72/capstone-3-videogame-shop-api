# EasyShop E-Commerce API

A full-featured RESTful API for an e-commerce platform built with Spring Boot, featuring JWT authentication, shopping cart functionality, and comprehensive product management.

## Features

- **User Authentication & Authorization**
  - JWT-based authentication
  - Role-based access control (USER/ADMIN)
  - Secure password hashing with BCrypt

- **Product Management**
  - Browse products by category
  - Advanced filtering (price range, subcategory)
  - Featured products
  - Full CRUD operations for administrators

- **Shopping Cart**
  - Add/update/remove items
  - Persistent cart storage
  - Real-time total calculations

- **User Profiles**
  - Personal information management
  - Shipping address storage

- **Category Management**
  - Organize products by category
  - Admin-controlled category CRUD

## Tech Stack

- **Backend Framework:** Spring Boot 2.7.x
- **Database:** MySQL 8.0
- **Security:** Spring Security + JWT
- **Build Tool:** Maven
- **Java Version:** 11+

## Project Structure

```
easyshop/
├── src/
│   ├── main/
│   │   ├── java/org/yearup/
│   │   │   ├── configurations/      # Database and security config
│   │   │   ├── controllers/         # REST endpoints
│   │   │   ├── data/                # DAO interfaces
│   │   │   │   └── mysql/           # MySQL implementations
│   │   │   ├── models/              # Domain models
│   │   │   └── security/            # JWT and security components
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/org/yearup/         # Unit tests
└── database/                         # SQL scripts
    ├── create_database_easyshop.sql
    ├── create_database_clothingstore.sql
    ├── create_database_groceryapp.sql
    ├── create_database_recordshop.sql
    └── create_database_videogamestore.sql
```

## Getting Started

### Prerequisites

- Java JDK 11 or higher
- MySQL 8.0 or higher
- Maven 3.6+
- An IDE (IntelliJ IDEA, Eclipse, or VS Code)

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/easyshop.git
   cd easyshop
   ```

2. **Set up the database**
   ```bash
   mysql -u root -p < database/create_database_easyshop.sql
   ```

3. **Configure application properties**
   
   Create or update `src/main/resources/application.properties`:
   ```properties
   datasource.url=jdbc:mysql://localhost:3306/easyshop
   datasource.username=root
   datasource.password=yourpassword
   
   jwt.secret=your-secret-key-here
   jwt.token-timeout-seconds=86400
   ```

4. **Build the project**
   ```bash
   mvn clean install
   ```

5. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

The API will be available at `http://localhost:8080`

## API Endpoints

### Authentication

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| POST | `/register` | Register new user | Public |
| POST | `/login` | Login and receive JWT token | Public |

### Products

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| GET | `/products` | Get all products (with filters) | Public |
| GET | `/products/{id}` | Get product by ID | Public |
| POST | `/products` | Create new product | Admin |
| PUT | `/products/{id}` | Update product | Admin |
| DELETE | `/products/{id}` | Delete product | Admin |

**Query Parameters for GET /products:**
- `cat` - Filter by category ID
- `minPrice` - Minimum price filter
- `maxPrice` - Maximum price filter
- `subCategory` - Filter by subcategory

### Categories

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| GET | `/categories` | Get all categories | Public |
| GET | `/categories/{id}` | Get category by ID | Public |
| GET | `/categories/{id}/products` | Get products in category | Public |
| POST | `/categories` | Create category | Admin |
| PUT | `/categories/{id}` | Update category | Admin |
| DELETE | `/categories/{id}` | Delete category | Admin |

### Shopping Cart

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| GET | `/cart` | Get user's cart | Authenticated |
| POST | `/cart/products/{id}` | Add product to cart | Authenticated |
| PUT | `/cart/products/{id}` | Update quantity | Authenticated |
| DELETE | `/cart` | Clear cart | Authenticated |

### Profile

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| GET | `/profile` | Get user profile | Authenticated |
| PUT | `/profile` | Update user profile | Authenticated |

## Authentication

The API uses JWT (JSON Web Tokens) for authentication. Include the token in the Authorization header:

```
Authorization: Bearer <your-jwt-token>
```

### Example Login Request

```bash
curl -X POST http://localhost:8080/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "user",
    "password": "password"
  }'
```

### Example Response

```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "user": {
    "id": 1,
    "username": "user",
    "authorities": [
      {
        "name": "ROLE_USER"
      }
    ]
  }
}
```

## Default Users

The database comes with pre-configured test users:

| Username | Password | Role |
|----------|----------|------|
| user | password | ROLE_USER |
| admin | password | ROLE_ADMIN |
| george | password | ROLE_USER |

## Database Schemas

The project includes multiple database schemas for different store types:
- **EasyShop** - General e-commerce (electronics, fashion, home & kitchen)
- **ClothingStore** - Apparel and footwear
- **GroceryApp** - Fresh produce and groceries
- **RecordShop** - Vinyl records, CDs, and music accessories
- **VideoGameStore** - Games and gaming accessories

All schemas share the same structure but contain different product data.

## Database Schema Overview

### Main Tables

- **users** - User accounts and credentials
- **profiles** - User personal information
- **categories** - Product categories
- **products** - Product catalog
- **shopping_cart** - User shopping carts
- **orders** - Order history
- **order_line_items** - Order details

### Entity Relationships

```
users (1) ──────── (1) profiles
  │
  │ (1)
  │
  └────────────────── (N) shopping_cart (N) ──────── (1) products
  │                                                        │
  │ (1)                                                (N) │
  │                                                        │
  └────────────────── (N) orders                    categories (1)
                        │
                        │ (1)
                        │
                        └─────── (N) order_line_items (N) ──── products
```

## Testing

Run the test suite:

```bash
mvn test
```

The project includes unit tests for DAO implementations with an in-memory test database.

## Error Handling

The API returns standard HTTP status codes:

- `200 OK` - Successful GET/PUT requests
- `201 Created` - Successful POST requests
- `204 No Content` - Successful DELETE requests
- `400 Bad Request` - Invalid request data
- `401 Unauthorized` - Missing or invalid authentication
- `403 Forbidden` - Insufficient permissions
- `404 Not Found` - Resource not found
- `500 Internal Server Error` - Server error

## Security Features

- Password hashing using BCrypt
- JWT token-based authentication
- Role-based authorization
- SQL injection prevention through prepared statements
- CORS configuration for cross-origin requests
- Stateless session management

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## Known Issues

- Duplicate products exist in some database schemas (intentional for testing)
- Shopping cart items are cleared when user logs out

## Future Enhancements

- [ ] Order checkout functionality
- [ ] Payment processing integration
- [ ] Product reviews and ratings
- [ ] Wishlist feature
- [ ] Email notifications
- [ ] Product search with Elasticsearch
- [ ] Image upload functionality
- [ ] Inventory management alerts
- [ ] Admin dashboard

## Contact

Lucas Rappatta - lucas.rappatta@yahoo.com

Project Link: https://github.com/JackFlack72/capstone-3-videogame-shop-api

## Acknowledgments

- Spring Boot documentation
- JWT.io for JWT resources
- MySQL documentation
- Year Up United program
