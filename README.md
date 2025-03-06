# 🚀 Pipeline SOA

## 📌 Introduction

Pipeline SOA is an order processing system designed using the pipeline architecture. The backend is built with Java Spring Boot, while the frontend utilizes React Vite with Tailwind CSS. The system continuously processes orders via a message queue, distributing data to respective processing modules.

## 👨‍💻 Team Members

- **Đoàn Thanh Lâm** - [GitHub](https://github.com/thanhlamcode)
- **Dương Lâm Gia Kiệt** - [GitHub](https://github.com/dlgkiet)
- **Đỗ Trọng Hiếu** - [GitHub](https://github.com/Hiu11)

## 🔥 Key Features

The system processes orders through the following steps:

1. **📦 Inventory Check**: Verify product availability.
2. **🚚 Delivery Service & Fee Calculation**.
3. **💳 Payment Capability Check**: Confirm the customer’s ability to pay.
4. **📝 Order Creation**.
5. **📧 Email Notification**.

Orders are continuously sent to the message queue, where processing modules execute corresponding tasks.

## 🛠️ Technologies Used

- **Backend**: Java Spring Boot
- **Frontend**: React + Vite + Tailwind CSS
- **Message Queue**: RabbitMQ or Kafka

## 📂 Project Structure

```
thanhlamcode-pipeline_soa/
├── SOA_03_03.iml
├── gradle.properties
├── gradlew
├── gradlew.bat
├── order_data.json
├── gradle/
│   ├── libs.versions.toml
│   └── wrapper/
│       └── gradle-wrapper.properties
├── resource/
│   └── frontend/
│       ├── README.md
│       ├── eslint.config.js
│       ├── index.html
│       ├── package-lock.json
│       ├── package.json
│       ├── vite.config.js
│       ├── .gitignore
│       ├── src/
│       │   ├── App.jsx
│       │   ├── index.css
│       │   ├── main.jsx
│       │   ├── api/
│       │   │   ├── deliveryApi.js
│       │   │   ├── inventoryApi.js
│       │   │   ├── notificationApi.js
│       │   │   ├── orderApi.js
│       │   │   └── paymentApi.js
│       │   ├── components/
│       │   │   ├── Footer.jsx
│       │   │   └── Navbar.jsx
│       │   └── pages/
│       │       ├── DeliveryCheck.jsx
│       │       ├── Home.jsx
│       │       ├── InventoryCheck.jsx
│       │       ├── Notification.jsx
│       │       ├── OrderCreation.jsx
│       │       └── PaymentVerification.jsx
│       └── .vite/
│           └── deps/
│               ├── _metadata.json
│               └── package.json
└── src/
    └── main/
        ├── java/
        │   ├── clients/
        │   ├── core/
        │   ├── filters/
        │   ├── pipes/
        │   ├── process/
        │   ├── queue/
        │   ├── services/
        │   └── web/
        └── resources/
            └── application.properties
```

## 🚀 Installation & Setup

### Backend (Runs on port 8080)

```bash
cd backend
./gradlew bootRun
```

### Frontend (Runs on port 5173)

```bash
cd resource/frontend
npm install
npm run dev
```

## 📡 API Endpoints

| Method | Endpoint       | Function            |
| ------ | -------------- | ------------------- |
| GET    | /api/orders    | Retrieve order list |
| POST   | /api/orders    | Create a new order  |
| GET    | /api/inventory | Check inventory     |
| GET    | /api/payment   | Verify payment      |

## 📜 Notes

- The system simulates a banking service and a delivery service.
- Orders are sent as JSON data with the following structure:

```json
{
  "order_info": {
    "orders": [
      {
        "orderId": 1,
        "custId": 1,
        "productId": 1,
        "quantity": 100,
        "description": "sample order 1"
      }
    ],
    "delivery": {
      "deliveryAdd": "12 NVB F4 GV, Ho Chi Minh, Viet Nam",
      "isDelivery": false
    },
    "payments": {
      "paymentId": 1,
      "custId": 1,
      "cardNumber": "7485-2222-3456-2435",
      "cvv": "111"
    }
  }
}
```

## 📬 Contact

For any inquiries, feel free to reach out to the team members via their respective GitHub profiles.

