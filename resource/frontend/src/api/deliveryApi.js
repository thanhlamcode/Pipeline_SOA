import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/delivery';

export const checkDelivery = async (orderRequest) => {
    try {
        const response = await axios.post(`${API_BASE_URL}/check`, orderRequest);
        return response.data;
    } catch (error) {
        console.error("Error checking delivery:", error);
        return { success: false, message: "Lỗi kiểm tra giao hàng" };
    }
};
