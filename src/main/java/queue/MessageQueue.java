package queue;

import core.entities.IMessage;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.UUID;
import java.util.function.Consumer;

public class MessageQueue {
    private final BlockingQueue<QueueMessage> queue = new LinkedBlockingQueue<>();
    private final ConcurrentMap<String, Consumer<IMessage>> subscribers = new ConcurrentHashMap<>();
    private boolean isRunning = true;
    private final Thread processingThread;

    public MessageQueue() {
        processingThread = new Thread(this::processMessages);
        processingThread.setDaemon(true);
        processingThread.start();
    }

    // Lớp bao bọc tin nhắn với thông tin bổ sung
    private static class QueueMessage {
        private final IMessage message;
        private final String topic;
        private final long timestamp;
        private final String messageId;

        public QueueMessage(IMessage message, String topic) {
            this.message = message;
            this.topic = topic;
            this.timestamp = System.currentTimeMillis();
            this.messageId = UUID.randomUUID().toString();
        }

        public IMessage getMessage() {
            return message;
        }

        public String getTopic() {
            return topic;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public String getMessageId() {
            return messageId;
        }
    }

    // Gửi tin nhắn vào hàng đợi với chủ đề cụ thể
    public void sendMessage(IMessage message, String topic) {
        try {
            queue.put(new QueueMessage(message, topic));
            System.out.println("Message sent to queue with topic: " + topic);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Interrupted while sending message: " + e.getMessage());
        }
    }

    // Phương thức đơn giản hóa gửi tin nhắn với chủ đề mặc định
    public void sendMessage(IMessage message) {
        sendMessage(message, "default");
    }

    // Đăng ký một người tiêu dùng cho một chủ đề cụ thể
    public String subscribe(String topic, Consumer<IMessage> consumer) {
        String subscriptionId = UUID.randomUUID().toString();
        subscribers.put(subscriptionId, consumer);
        System.out.println("Subscribed to topic: " + topic + " with ID: " + subscriptionId);
        return subscriptionId;
    }

    // Hủy đăng ký một người tiêu dùng
    public void unsubscribe(String subscriptionId) {
        subscribers.remove(subscriptionId);
        System.out.println("Unsubscribed ID: " + subscriptionId);
    }

    // Phương thức để dừng hàng đợi
    public void stop() {
        isRunning = false;
        processingThread.interrupt();
    }

    // Xử lý các tin nhắn trong hàng đợi
    private void processMessages() {
        while (isRunning) {
            try {
                QueueMessage queueMessage = queue.take();
                System.out.println("Processing message with ID: " + queueMessage.getMessageId()
                        + " from topic: " + queueMessage.getTopic());

                // Thông báo cho tất cả người đăng ký
                subscribers.values().forEach(consumer -> {
                    try {
                        consumer.accept(queueMessage.getMessage());
                    } catch (Exception e) {
                        System.err.println("Error notifying subscriber: " + e.getMessage());
                    }
                });
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Message queue processing interrupted: " + e.getMessage());
                if (!isRunning) {
                    break;
                }
            }
        }
    }
}
