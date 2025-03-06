import React, { useState } from "react";
import { checkDelivery } from "../api/deliveryApi";

function DeliveryCheck() {
  const [orderData, setOrderData] = useState({
    order_info: {
      orders: [
        {
          orderId: 1,
          custId: 5,
          productId: 10,
          quantity: 3,
          description: "Laptop Dell XPS 13",
        },
        {
          orderId: 2,
          custId: 5,
          productId: 15,
          quantity: 2,
          description: "Bàn phím cơ RGB",
        },
      ],
      delivery: {
        nodeId: 3,
        note: "Giao hàng trong ngày",
        deliveryAdd: "456 Đường Nguyễn Trãi, Quận 5, TP.HCM",
        isDelivery: true,
      },
      payments: {
        paymentId: 10,
        custId: 5,
        cardNumber: "1234-5678-9012-3456",
        cvv: "123",
      },
    },
  });

  const [response, setResponse] = useState(null);
  const [error, setError] = useState(null);

  const handleSubmit = async () => {
    try {
      console.log("📡 Gửi dữ liệu:", orderData);
      const result = await checkDelivery(orderData);

      console.log("✅ Kết quả API:", result);
      setResponse(result.message || "Giao hàng thành công!");
      setError(null);
    } catch (error) {
      console.error("❌ Lỗi:", error);
      setError("❌ Lỗi khi gửi đơn hàng. Vui lòng thử lại!");
      setResponse(null);
    }
  };

  return (
    <div className="min-h-screen flex flex-col items-center justify-center bg-gray-100 p-4">
      <h2 className="text-3xl font-bold mb-4">🚚 Kiểm tra dịch vụ giao hàng</h2>

      <div className="bg-white p-6 rounded-lg shadow-md w-full max-w-xl">
        <h3 className="text-xl font-semibold mb-3">🛍️ Danh sách sản phẩm</h3>
        {orderData.order_info.orders.map((order, index) => (
          <div key={index} className="mb-2 p-2 border rounded">
            <p>
              <strong>{order.description}</strong>
            </p>
            <p>
              SL: {order.quantity} | Mã SP: {order.productId}
            </p>
          </div>
        ))}

        <h3 className="text-xl font-semibold mt-4 mb-2">
          📍 Địa chỉ giao hàng
        </h3>
        <p>{orderData.order_info.delivery.deliveryAdd}</p>
        <p>
          <i>{orderData.order_info.delivery.note}</i>
        </p>

        <h3 className="text-xl font-semibold mt-4 mb-2">💳 Thanh toán</h3>
        <p>
          Thẻ: ****-****-****-
          {orderData.order_info.payments.cardNumber.slice(-4)}
        </p>

        <button
          className="w-full mt-4 px-6 py-2 bg-green-500 text-white rounded-lg hover:bg-green-600"
          onClick={handleSubmit}
        >
          Kiểm tra giao hàng
        </button>

        {response && (
          <p className="mt-4 text-green-600 font-semibold">✅ {response}</p>
        )}
        {error && <p className="mt-4 text-red-500 font-semibold">{error}</p>}
      </div>
    </div>
  );
}

export default DeliveryCheck;
