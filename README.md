Collecting workspace informationI'll help you create comprehensive documentation for the entire microservices system, including the RabbitMQ configuration. Here's a complete project documentation that covers all the services:

# 📚 Online Home-Made Food Microservices System

This documentation covers the entire microservices-based food ordering application, which consists of multiple interconnected services using RabbitMQ for message-based communication.

## 🔄 System Architecture

The application is built as a distributed system with the following microservices:

- **[User Service](#user-service)**: Manages customer accounts, authentication, and order history
- **[Order Service](#order-service)**: Processes food orders, handles payments, and manages shipping
- **[Restaurant Service](#restaurant-service)**: Manages restaurant profiles, menus, and dish inventory
- **[Admin Service](#admin-service)**: Provides system monitoring, company account creation, and reporting

![System Architecture Diagram]

## 🚀 Prerequisites

Before setting up the project, ensure you have the following installed:

* **Java Development Kit (JDK)**: Version 21
* **Apache Maven**: For building Java services
* **WildFly Application Server**: Version 36
* **Spring Boot**: For Restaurant, User, and Admin services
* **Docker**: For running RabbitMQ
* **Node.js and npm**: For frontend applications
* **Visual Studio Code**: With Java and JavaScript extensions installed

## 🐰 RabbitMQ Setup

The system uses RabbitMQ for asynchronous communication between services. To run RabbitMQ:

```bash
docker run -it --rm --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:4.0-management
```

This command starts RabbitMQ with the management plugin enabled:
- AMQP port: 5672
- Management UI: http://localhost:15672 (username: guest, password: guest)

## 🌐 Service Configuration

### User Service

Located in the user directory, this Spring Boot application manages customer accounts and preferences.

#### Key Features:
- Customer registration and authentication
- User balance management
- Order status updates via RabbitMQ
- Restaurant and dish listing

#### API Endpoints:
- `/accounts/getUserBalance?id={userId}` - Get user balance
- `/accounts/updateUserBalance?id={userId}&balance={balance}` - Update user balance
- `/accounts/all` - List all users

#### Building and Running:
```bash
cd user
mvn clean package
java -jar target/user-0.0.1-SNAPSHOT.jar
```

Server runs on port 8083.

### Order Service

Located in the system-order directory, this Jakarta EE application processes food orders.

#### Key Features:
- Order processing pipeline
- Payment processing
- Stock availability verification
- RabbitMQ event publishing

#### API Endpoints:
- `/api/order/placeOrder` - Process new orders
- `/api/order/getAllOrdersByUserID` - Get orders by user
- `/api/order/getOrderDishesByOrderID` - Get details of an order

#### Building and Running:
```bash
cd system-order
mvn clean package
mvn wildfly:deploy
```

Server runs on port 8080 with context path `/system-order-1.0-SNAPSHOT`.

### Restaurant Service

Located in the system-restaurant directory, this Spring Boot application manages restaurant information and menus.

#### Key Features:
- Restaurant account management
- Dish inventory and menu management
- Stock availability checks
- Order fulfillment

#### API Endpoints:
- `/dishes/create` - Create new dish
- `/dishes/getbyrestaurantid` - Get restaurant menu items
- `/dishes/update` - Update dish information
- `/createAccount` - Create restaurant account

#### Building and Running:
```bash
cd system-restaurant
mvn clean package
java -jar target/system_restaurant-0.0.1-SNAPSHOT.jar
```

Server runs on port 8082.

### Admin Service

Located in the system-admin directory, this Spring Boot application provides administrative capabilities.

#### Key Features:
- System monitoring and logging
- Payment failure tracking
- Service health monitoring
- Company account creation

#### API Endpoints:
- `/admin/payment-failures` - Get payment failure records
- `/admin/service-logs` - Get service logs
- `/admin/create-company-accounts` - Create restaurant accounts
- `/admin/list-users` - List all users

#### Building and Running:
```bash
cd system-admin
mvn clean package
java -jar target/system-admin-0.0.1-SNAPSHOT.jar
```

Server runs on port 8088.

## 📱 Frontend Applications

### Admin Dashboard

Located in the admin_dashboard directory, this React application provides admin tools and monitoring.

#### Key Features:
- User management
- Restaurant account creation
- System logs and monitoring
- Payment failure reporting

#### Building and Running:
```bash
cd frontend/admin_dashboard
npm install
npm run dev
```

### Customer Orders Frontend

Located in the orders_frontend directory, this React application allows customers to order food.

#### Key Features:
- Restaurant browsing
- Menu viewing
- Order placement
- Order history

#### Building and Running:
```bash
cd frontend/orders_frontend
npm install
npm run dev
```

## 📨 Message Queues & Event Flow

The system uses the following RabbitMQ exchanges and queues:

### Exchanges:
- `order_exchange` (direct) - Order success/failure events
- `payments_exchange` (direct) - Payment processing events
- `log_exchange` (topic) - System logs
- `stock_exchange` (topic) - Stock availability checks

### Primary Queues:
- `order_status_user` - Order status updates for user service
- `payment_failure_queue` - Failed payment events
- `log_queue` - System logs
- `request_stock` - Stock availability requests
- `response_stock` - Stock availability responses
- `order_success_queue` - Successful order events

### Event Flow Examples:

1. **Order Placement**:
   - Customer submits order → Order Service
   - Order Service checks stock via `request_stock` queue
   - Restaurant Service responds via `response_stock` queue
   - Order Service processes payment
   - On success: Order Service sends to `order_success_queue`
   - Restaurant Service updates inventory

2. **Payment Failure**:
   - Order Service detects payment issue
   - Publishes to `payment_failure_queue`
   - Admin Service logs the failure
   - User Service updates user status

## 🔍 System Monitoring

The Admin Service collects logs and error events from all services via the `log_queue`. The events are stored in the database and can be viewed through the Admin Dashboard.

## 🛠️ Development Workflow

1. Start RabbitMQ with Docker
2. Start each service in separate terminals
3. Start frontend applications
4. Make changes and restart individual services as needed

## 📝 Data Models

### User/Customer
- Account information
- Balance
- Order history

### Order
- OrderID
- UserID
- RestaurantID
- List of OrderDishes
- TotalPrice
- Destination
- ShippingCompany
- Status

### Restaurant
- Restaurant profile
- Dish inventory
- Menu items

### Dish
- Name
- Description
- Price
- RestaurantID

## 🧪 Testing

Each service includes its own testing suite. Run tests with:

```bash
mvn test
```

## 🏗️ Future Enhancements

- Kubernetes deployment configuration
- Service discovery integration
- API gateway implementation
- Enhanced security with OAuth2
- Performance monitoring with Prometheus

---

[1]: https://docs.wildfly.org/
[2]: https://docs.wildfly.org/26/Getting_Started_Guide.html
[3]: https://forum.camunda.org/t/deploying-process-application-to-wildfly-as-27/38731
[4]: https://developer.jboss.org/docs/DOC-55154