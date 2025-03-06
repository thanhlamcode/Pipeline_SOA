import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/payment';

export const verifyPayment = async (orderRequest) => {
    try {
        const response = await axios.post(`${API_BASE_URL}/verify`, orderRequest);
        return response.data;
    } catch (error) {
        console.error("Error verifying payment:", error);
        return { success: false, message: "Lỗi xác minh thanh toán" };
    }
};
