import axios from "axios";

const API_BASE_URL = "http://localhost:8080/inventory";

export const checkInventory = async (orderRequest) => {
  try {
    const response = await axios.post(`${API_BASE_URL}/check`, orderRequest);
    return response.data;
  } catch (error) {
    console.error("Error checking inventory:", error);
    return { success: false, message: "Lỗi kiểm tra kho hàng" };
  }
};

export const getInventoryStatus = async (productId) => {
  try {
    const response = await axios.get(`${API_BASE_URL}/status/${productId}`);
    return response.data;
  } catch (error) {
    console.error("Error getting inventory status:", error);
    return { success: false, message: "Không lấy được thông tin kho" };
  }
};
