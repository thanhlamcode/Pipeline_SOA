import React from 'react';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import InventoryCheck from './pages/InventoryCheck';
import DeliveryCheck from './pages/DeliveryCheck';
import PaymentVerification from './pages/PaymentVerification';
import OrderCreation from './pages/OrderCreation';
import Notification from './pages/Notification';

function App() {
  return (
    <>
      {/* Navbar */}
      <nav className="bg-blue-600 p-4 shadow-md">
        <ul className="flex justify-center space-x-6">
          <li>
            <Link to="/" className="text-white font-semibold hover:text-gray-300">
              Trang chủ
            </Link>
          </li>
          <li>
            <Link to="/inventory" className="text-white font-semibold hover:text-gray-300">
              Kiểm tra kho
            </Link>
          </li>
          <li>
            <Link to="/delivery" className="text-white font-semibold hover:text-gray-300">
              Dịch vụ giao hàng
            </Link>
          </li>
          <li>
            <Link to="/payment" className="text-white font-semibold hover:text-gray-300">
              Xác minh thanh toán
            </Link>
          </li>
          <li>
            <Link to="/order" className="text-white font-semibold hover:text-gray-300">
              Lập đơn hàng
            </Link>
          </li>
          <li>
            <Link to="/notification" className="text-white font-semibold hover:text-gray-300">
              Gửi email
            </Link>
          </li>
        </ul>
      </nav>

      {/* Main Content */}
      <div className="container mx-auto mt-6 p-6">
        <Routes>
          <Route path="/inventory" element={<InventoryCheck />} />
          <Route path="/delivery" element={<DeliveryCheck />} />
          <Route path="/payment" element={<PaymentVerification />} />
          <Route path="/order" element={<OrderCreation />} />
          <Route path="/notification" element={<Notification />} />
        </Routes>
      </div>
    </>
  );
}

export default App;
