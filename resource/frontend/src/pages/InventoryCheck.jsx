import React, { useState } from "react";
import { checkInventory } from "../api/inventoryApi";

function OrderForm() {
  const [orderData, setOrderData] = useState({
    order_info: {
      orders: [
        { orderId: 1, custId: 7, productId: 5, quantity: 10, description: "MacBook Pro 14-inch" },
        { orderId: 2, custId: 7, productId: 12, quantity: 5, description: "Chuột không dây Logitech MX Master 3" }
      ],
      delivery: {
        nodeId: 2,
        note: "Giao hàng tiêu chuẩn",
        deliveryAdd: "123 Đường Lý Thường Kiệt, Quận 10, TP.HCM",
        isDelivery: true
      },
      payments: {
        paymentId: 15,
        custId: 7,
        cardNumber: "9876-5432-1098-7654",
        cvv: "321"
      }
    }
  });

  const [response, setResponse] = useState(null);

  const handleSubmit = async () => {
    try {
      const result = await checkInventory(orderData);
      setResponse(result);
    } catch (error) {
      setResponse({ message: "Lỗi khi gửi đơn hàng" });
    }
  };

  return (
    <div className="min-h-screen flex flex-col items-center justify-center bg-gray-100 p-4">
      <h2 className="text-3xl font-bold mb-4">📦 Tạo đơn hàng</h2>

      <div className="bg-white p-6 rounded-lg shadow-md w-full max-w-xl">
        <h3 className="text-xl font-semibold mb-3">🛍️ Sản phẩm</h3>
        {orderData.order_info.orders.map((order, index) => (
          <div key={index} className="mb-2 p-2 border rounded">
            <p><strong>{order.description}</strong></p>
            <p>SL: {order.quantity} | Mã SP: {order.productId}</p>
          </div>
        ))}

        <h3 className="text-xl font-semibold mt-4 mb-2">🚚 Giao hàng</h3>
        <p>{orderData.order_info.delivery.deliveryAdd}</p>
        <p><i>{orderData.order_info.delivery.note}</i></p>

        <h3 className="text-xl font-semibold mt-4 mb-2">💳 Thanh toán</h3>
        <p>Thẻ: ****-****-****-{orderData.order_info.payments.cardNumber.slice(-4)}</p>

        <button
          className="w-full mt-4 px-6 py-2 bg-blue-500 text-white rounded-lg hover:bg-blue-600"
          onClick={handleSubmit}
        >
          Gửi đơn hàng
        </button>

        {response && <p className="mt-4 text-lg">{response.message}</p>}
      </div>
    </div>
  );
}

export default OrderForm;
