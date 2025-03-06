import React, { useState } from "react";
import { verifyPayment } from "../api/paymentApi";

function PaymentVerification() {
  const [response, setResponse] = useState(null);
  const [error, setError] = useState(null);

  const orderData = {
    order_info: {
      orders: [
        { orderId: 301, custId: 20, productId: 5, quantity: 1, description: "MacBook Air M2 2023" },
        { orderId: 302, custId: 20, productId: 9, quantity: 2, description: "iPad Pro 12.9-inch 2022" }
      ],
      delivery: {
        nodeId: 8,
        note: "Giao hàng tiêu chuẩn",
        deliveryAdd: "88 Đường Cách Mạng Tháng 8, Quận 10, TP.HCM",
        isDelivery: true
      },
      payments: {
        paymentId: 30,
        custId: 20,
        cardNumber: "1234-5678-9012-3456",
        cvv: "123"
      }
    }
  };

  const handleVerify = async () => {
    try {
      console.log("📡 Gửi dữ liệu thanh toán:", orderData);
      const result = await verifyPayment(orderData);

      console.log("✅ Kết quả API:", result);
      setResponse(result.message || "Thanh toán thành công!");
      setError(null);
    } catch (err) {
      console.error("❌ Lỗi xác minh:", err);
      setError("❌ Lỗi khi xác minh thanh toán. Vui lòng thử lại!");
      setResponse(null);
    }
  };

  return (
    <div className="min-h-screen flex flex-col items-center justify-center bg-gray-100 p-4">
      <h2 className="text-3xl font-bold mb-4">💳 Xác minh thanh toán</h2>

      <div className="bg-white p-6 rounded-lg shadow-md w-full max-w-xl">
        <h3 className="text-xl font-semibold mb-3">🛍️ Danh sách sản phẩm</h3>
        {orderData.order_info.orders.map((order, index) => (
          <div key={index} className="mb-2 p-2 border rounded">
            <p><strong>{order.description}</strong></p>
            <p>SL: {order.quantity} | Mã SP: {order.productId}</p>
          </div>
        ))}

        <h3 className="text-xl font-semibold mt-4 mb-2">📍 Địa chỉ giao hàng</h3>
        <p>{orderData.order_info.delivery.deliveryAdd}</p>
        <p><i>{orderData.order_info.delivery.note}</i></p>

        <h3 className="text-xl font-semibold mt-4 mb-2">💳 Thanh toán</h3>
        <p>Thẻ: ****-****-****-{orderData.order_info.payments.cardNumber.slice(-4)}</p>

        <button
          className="w-full mt-4 px-6 py-2 bg-purple-500 text-white rounded-lg hover:bg-purple-600"
          onClick={handleVerify}
        >
          Xác minh thanh toán
        </button>

        {response && <p className="mt-4 text-green-600 font-semibold">✅ {response}</p>}
        {error && <p className="mt-4 text-red-500 font-semibold">{error}</p>}
      </div>
    </div>
  );
}

export default PaymentVerification;
