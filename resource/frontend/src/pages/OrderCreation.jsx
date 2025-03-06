import React, { useState } from "react";
import { createOrder } from "../api/orderApi";

function OrderCreation() {
  const [response, setResponse] = useState(null);
  const [error, setError] = useState(null);

  const orderData = {
    order_info: {
      orders: [
        { orderId: 201, custId: 15, productId: 8, quantity: 2, description: "Điện thoại Samsung Galaxy S23 Ultra" },
        { orderId: 202, custId: 15, productId: 14, quantity: 1, description: "Tai nghe AirPods Pro 2" }
      ],
      delivery: {
        nodeId: 6,
        note: "Giao hàng hỏa tốc",
        deliveryAdd: "12 Nguyễn Du, Quận 1, TP.HCM",
        isDelivery: true
      },
      payments: {
        paymentId: 25,
        custId: 15,
        cardNumber: "2222-3333-4444-5555",
        cvv: "789"
      }
    }
  };

  const handleCreate = async () => {
    try {
      console.log("📡 Gửi dữ liệu đặt hàng:", orderData);
      const result = await createOrder(orderData);

      console.log("✅ Kết quả API:", result);
      setResponse(result.message || "Đơn hàng đã được tạo thành công!");
      setError(null);
    } catch (err) {
      console.error("❌ Lỗi khi tạo đơn hàng:", err);
      setError("❌ Đã xảy ra lỗi khi tạo đơn hàng. Vui lòng thử lại!");
      setResponse(null);
    }
  };

  return (
    <div className="min-h-screen flex flex-col items-center justify-center bg-gray-100 p-4">
      <h2 className="text-3xl font-bold mb-4">📝 Lập đơn đặt hàng</h2>

      <div className="bg-white p-6 rounded-lg shadow-md w-full max-w-xl">
        <h3 className="text-xl font-semibold mb-3">🛒 Danh sách sản phẩm</h3>
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
          className="w-full mt-4 px-6 py-2 bg-red-500 text-white rounded-lg hover:bg-red-600"
          onClick={handleCreate}
        >
          Tạo đơn hàng
        </button>

        {response && <p className="mt-4 text-green-600 font-semibold">✅ {response}</p>}
        {error && <p className="mt-4 text-red-500 font-semibold">{error}</p>}
      </div>
    </div>
  );
}

export default OrderCreation;
