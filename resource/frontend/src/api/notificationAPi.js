import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/notification';

export const sendEmailNotification = async (invoiceInfo) => {
    try {
        const response = await axios.post(`${API_BASE_URL}/send-email`, invoiceInfo);
        return response.data;
    } catch (error) {
        console.error("Error sending email notification:", error);
        return { success: false, message: "Lỗi gửi email thông báo" };
    }
};
