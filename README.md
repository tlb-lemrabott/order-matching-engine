# 📈 Stock Exchange Platform – Project Overview

A real-time, intelligent stock trading platform using a **microservice architecture**. It allows buyers and sellers to place orders, automatically matches them, executes payments, and provides analytics-based price recommendations via integrated services.

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

## 📦 Project Structure (StockExchangeService)
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

## 🚀 Future Enhancements

- 🔐 JWT-based user authentication
- 💼 User wallet and portfolio management
- 🧪 Market simulation and replay


