# 📈 Stock Exchange Platform – Project Overview

A real-time, intelligent stock trading platform using a **Spring Boot microservice architecture**. It allows buyers and sellers to place orders, automatically matches them, executes payments, and provides analytics-based price recommendations via integrated services.

---

## 🧩 Architecture Overview

The platform is composed of **three independent microservices**:

1. **StockExchangeService** – Core matching engine and order book management
2. **PaymentService** – Manages trade-related monetary transactions
3. **AssistantService** – Recommends optimal buy/sell prices based on market data and analytics

All services communicate via **REST APIs**.

---

🔐 Security
- 🔗 JWT-based Authentication for securing APIs
- 🔑 bcrypt for secure password hashing
- 👥 Role-based Access Control (RBAC) with roles:
  - ADMIN
  - BUYER
  - SELLER

---

## 🧱 Microservices Breakdown

### 1. StockExchangeService

Handles the order lifecycle: submission, matching, history, and trade triggering.

#### Responsibilities:
- Maintain buy/sell order books using priority queues
- Execute price-time or pro-rata matching algorithm
- Persist trade history
- Trigger payments via PaymentService
- Request pricing advice from AssistantService
- Provide real-time updates via WebSocket

#### REST Endpoints:
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST   | `/orders` | Submit new order |
| GET    | `/orders/book` | Get current order book |
| GET    | `/trades/history` | View trade history |
| POST   | `/orders/match` | Trigger match manually |
| GET    | `/recommendation/price` | Delegate price suggestion to AssistantService |
| POST   | `/payment/execute` | Trigger payment execution in PaymentService |

#### Features:
- ✅ Price-Time and Pro-rata Matching
- 📡 WebSocket notifications for trades and order book
- 📊 Prometheus + Grafana for monitoring

---

### 2. PaymentService

Executes and logs payment transactions between users.

#### Responsibilities:
- Validate user balances
- Transfer funds on trade confirmation
- Track transaction history
- Simulate external payment gateways

#### REST Endpoints:
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST   | `/payment/execute` | Execute a payment |
| GET    | `/payment/status/{transactionId}` | Get payment status |
| GET    | `/wallet/{userId}` | View user wallet or balance (optional) |

#### Features:
- 🔐 Token-based inter-service authentication
- 💳 Integration with Stripe for payment service

---

### 3. AssistantService

Provides smart price suggestions based on market analytics and trends.

#### Responsibilities:
- Calculate price recommendations using:
  - SMA, EMA
  - Order book depth and volume trends
  - AI models
- Summarize market stats for frontend display

#### REST Endpoints:
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET    | `/recommendation/price` | Recommend buy/sell price |
| GET    | `/market/summary` | Market analysis summary |

#### Features:
- 📊 Analytical tools (Apache Commons Math)
- 🤖 AI/ML integration using Spring AI

---

## 📡 Inter-Service Communication

All microservices use **REST APIs** for communication.

#### Example Flow:
1. Buyer submits order via frontend → `StockExchangeService`
2. Matching engine finds match → Creates trade
3. `StockExchangeService`:
   - Calls `AssistantService` → To get price advice
   - Calls `PaymentService` → To process trade payment
4. Trade result is pushed to clients via WebSocket

---

## 🗄️ Data Storage Design

Each microservice has its own **PostgreSQL** database:

- `StockExchangeService` → Orders, Trades
- `PaymentService` → Transactions, Wallets
- `AssistantService` → Market Snapshots, Analytics

Use independent schema per service or isolated database instances.

---

## 📦 Backend Structure (StockExchangeService)
stock-exchange-platform/
├── src/
│ ├── main/java/com/example/engine/
│ │ ├── config/ # PrometheusConfig.java, WebSocketConfig.java
│ │ ├── dto/ # OrderRequest.java, TradeResponse.java
│ │ ├── controller/ # OrderController.java
│ │ ├── metrics/ # TradeMetricsService.java
│ │ ├── model/ # Order.java, OrderType.java, Trade.java
│ │ ├── service/ # OrderBookService.java, TradeService.java
│ │ ├── engine/ # MatchingEngine.java, PriceTimeMatchingEngine.java
│ │ ├── repository/ # OrderRepository.java, TradeRepository.java
│ │ ├── websocket/ # TradeWebSocketController.java
│ │ └── OrderMatchingEngineApplication.java
│ └── resources/
│ ├── application.properties
│ ├── application-dev.yml
│ └── application-prod.yml
└── pom.xml

---

## 🧪 Observability & Testing

- ✅ **Unit Tests** with JUnit
- ✅ **Integration Tests** for service interaction
- 📈 **Monitoring** with Prometheus and Grafana
- 📑 **Logging** with SLF4J / Logback

---

## Fontend side
### Frontend Structure (stock-exchange-ui)
stock-exchange-ui/
├── src/
│   ├── app/
│   │   ├── core/                      # Core modules and services (singleton)
│   │   │   ├── auth/                 # JWT auth, login, registration
│   │   │   │   ├── auth.service.ts
│   │   │   │   ├── auth.guard.ts
│   │   │   │   ├── jwt.interceptor.ts
│   │   │   │   ├── role.guard.ts
│   │   │   │   └── token-storage.service.ts
│   │   │   ├── services/             # Shared singleton services
│   │   │   │   ├── websocket.service.ts
│   │   │   │   └── notification.service.ts
│   │   │   └── core.module.ts
│   │
│   │   ├── shared/                   # Shared components, pipes, and directives
│   │   │   ├── components/
│   │   │   ├── pipes/
│   │   │   └── shared.module.ts
│   │
│   │   ├── features/                 # Feature modules (organized by business domain)
│   │   │   ├── auth/                # Login/Register pages
│   │   │   │   ├── login/
│   │   │   │   ├── register/
│   │   │   │   └── auth.module.ts
│   │   │   ├── dashboard/          # Home and user dashboards
│   │   │   │   ├── buyer-dashboard/
│   │   │   │   ├── seller-dashboard/
│   │   │   │   └── dashboard.module.ts
│   │   │   ├── order-book/         # Order book view and submission
│   │   │   │   ├── place-order/
│   │   │   │   ├── live-order-book/
│   │   │   │   └── order-book.module.ts
│   │   │   ├── trade-history/      # View completed trades
│   │   │   │   ├── trade-history.component.ts
│   │   │   │   └── trade-history.module.ts
│   │   │   ├── assistant/          # Assistant recommendations
│   │   │   │   ├── assistant.component.ts
│   │   │   │   └── assistant.module.ts
│   │   │   ├── admin/              # Admin pages (optional)
│   │   │   │   └── user-management/
│   │   │   │       ├── user-list.component.ts
│   │   │   │       └── role-editor.component.ts
│   │   │   └── ...
│   │
│   │   ├── app-routing.module.ts
│   │   ├── app.component.ts
│   │   └── app.module.ts
│   │
│   ├── assets/                     # Static assets like logos and images
│   ├── environments/               # `environment.ts` and `environment.prod.ts`
│   └── index.html
│
├── angular.json
├── package.json
└── tsconfig.json

### 🧠 Feature Module Descriptions
- **auth:** Handles user authentication (login/register), role guard, JWT storage
- **dashboard:** User dashboards (BUYER, SELLER, ADMIN views)
- **order-book:** Submit/view live order book (with real-time updates)
- **trade-history:** Displays a list of completed trades
- **User Assistant** Shows price recommendations and market analysis
- **admin:** Admin interface for managing users and roles

### 🔌 WebSocket & Real-Time Integration
- WebSocketService in core/services to connect to Spring WebSocket endpoint
- Emit and listen for:
  - Trade updates
  - Order book changes
