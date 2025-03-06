import React, { useState } from "react";
import { sendEmailNotification } from "../api/notificationAPi.js";

function Notification() {
  const [response, setResponse] = useState(null);
  const [error, setError] = useState(null);

  const emailData = {
    invoice: {
      invoiceId: 101,
      custId: 5,
      amount: 350.75,
      isPaid: false,
      orders: [
        { orderId: 1, custId: 5, productId: 10, quantity: 3, description: "Laptop Dell XPS 13" },
        { orderId: 2, custId: 5, productId: 15, quantity: 2, description: "Bàn phím cơ RGB" }
      ]
    },
    delivery: {
      nodeId: 3,
      note: "Giao hàng nhanh",
      deliveryAdd: "456 Đường Nguyễn Trãi, Quận 5, TP.HCM",
      isDelivery: true
    },
    payment: {
      paymentId: 10,
      custId: 5,
      cardNumber: "1234-5678-9012-3456",
      cvv: "123",
      transactionId: "TXN-987654321"
    }
  };

  const handleSendEmail = async () => {
    try {
      console.log("📡 Gửi dữ liệu email:", emailData);
      const result = await sendEmailNotification(emailData);

      console.log("✅ Kết quả API:", result);
      setResponse(result.message || "📩 Email thông báo đã được gửi!");
      setError(null);
    } catch (err) {
      console.error("❌ Lỗi khi gửi email:", err);
      setError("❌ Gửi email thất bại. Vui lòng thử lại!");
      setResponse(null);
    }
  };

  return (
    <div className="min-h-screen flex flex-col items-center justify-center bg-gray-100 p-4">
      <h2 className="text-3xl font-bold mb-4">📧 Gửi email thông báo</h2>

      <div className="bg-white p-6 rounded-lg shadow-md w-full max-w-xl">
        <h3 className="text-xl font-semibold mb-3">🧾 Hóa đơn</h3>
        <p><strong>ID Hóa đơn:</strong> {emailData.invoice.invoiceId}</p>
        <p><strong>Tổng tiền:</strong> ${emailData.invoice.amount}</p>
        <p><strong>Trạng thái thanh toán:</strong> {emailData.invoice.isPaid ? "✅ Đã thanh toán" : "❌ Chưa thanh toán"}</p>

        <h3 className="text-xl font-semibold mt-4 mb-2">🛒 Danh sách sản phẩm</h3>
        {emailData.invoice.orders.map((order, index) => (
          <div key={index} className="mb-2 p-2 border rounded">
            <p><strong>{order.description}</strong></p>
            <p>SL: {order.quantity} | Mã SP: {order.productId}</p>
          </div>
        ))}

        <h3 className="text-xl font-semibold mt-4 mb-2">📍 Địa chỉ giao hàng</h3>
        <p>{emailData.delivery.deliveryAdd}</p>
        <p><i>{emailData.delivery.note}</i></p>

        <h3 className="text-xl font-semibold mt-4 mb-2">💳 Thanh toán</h3>
        <p>Giao dịch: {emailData.payment.transactionId}</p>
        <p>Thẻ: ****-****-****-{emailData.payment.cardNumber.slice(-4)}</p>

        <button
          className="w-full mt-4 px-6 py-2 bg-yellow-500 text-white rounded-lg hover:bg-yellow-600"
          onClick={handleSendEmail}
        >
          Gửi Email
        </button>

        {response && <p className="mt-4 text-green-600 font-semibold">✅ {response}</p>}
        {error && <p className="mt-4 text-red-500 font-semibold">{error}</p>}
      </div>
    </div>
  );
}

export default Notification;
