import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/order';

export const createOrder = async (orderRequest) => {
    try {
        const response = await axios.post(`${API_BASE_URL}/create`, orderRequest);
        return response.data;
    } catch (error) {
        console.error("Error creating order:", error);
        return { success: false, message: "Lỗi tạo đơn hàng" };
    }
};
