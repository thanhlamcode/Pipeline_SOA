package services;

import core.Payment;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class BankingService {
    // Simulated bank database
    private static final Map<String, BankAccount> bankAccounts = new HashMap<>();
    private static final Map<String, Double> exchangeRates = new HashMap<>();
    private static final Random random = new Random();

    static {
        // Initialize some test bank accounts
        bankAccounts.put("7485-2222-3456-2435", new BankAccount(10000.0, true));
        bankAccounts.put("1234-5678-9012-3456", new BankAccount(5000.0, true));
        bankAccounts.put("9876-5432-1098-7654", new BankAccount(2000.0, true));
        bankAccounts.put("1111-2222-3333-4444", new BankAccount(500.0, true));
        bankAccounts.put("5555-6666-7777-8888", new BankAccount(100.0, false));

        // Initialize exchange rates (against USD)
        exchangeRates.put("USD", 1.0);
        exchangeRates.put("EUR", 0.93);
        exchangeRates.put("GBP", 0.79);
        exchangeRates.put("JPY", 110.45);
        exchangeRates.put("VND", 23000.0);
    }

    // Check if a payment can be processed
    public static PaymentVerificationResult verifyPayment(Payment payment, double amount) {
        String cardNumber = payment.getCardNumber();
        BankAccount account = bankAccounts.get(cardNumber);

        if (account == null) {
            return new PaymentVerificationResult(false, "Card not found in the system");
        }

        if (!account.isActive()) {
            return new PaymentVerificationResult(false, "Card is inactive or blocked");
        }

        if (account.getBalance() < amount) {
            return new PaymentVerificationResult(false, "Insufficient funds");
        }

        // Simulate transaction verification (might fail for various reasons)
        double randomValue = random.nextDouble();
        if (randomValue < 0.05) { // 5% chance of failure
            return new PaymentVerificationResult(false, "Transaction declined by the bank");
        }

        return new PaymentVerificationResult(true, "Payment verified successfully");
    }

    // Process the payment
    public static PaymentProcessResult processPayment(Payment payment, double amount) {
        String cardNumber = payment.getCardNumber();
        BankAccount account = bankAccounts.get(cardNumber);

        if (account == null) {
            return new PaymentProcessResult(false, "Card not found");
        }

        // First verify the payment
        PaymentVerificationResult verificationResult = verifyPayment(payment, amount);
        if (!verificationResult.isVerified()) {
            return new PaymentProcessResult(false, verificationResult.getMessage());
        }

        // Process the payment
        account.setBalance(account.getBalance() - amount);

        // Generate transaction ID
        String transactionId = generateTransactionId();

        return new PaymentProcessResult(true, "Payment processed successfully", transactionId);
    }

    // Generate a random transaction ID
    private static String generateTransactionId() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            builder.append(random.nextInt(10));
        }
        return builder.toString();
    }

    public static synchronized void registerCard(String cardNumber, double balance, boolean active) {
        if (!bankAccounts.containsKey(cardNumber)) {
            bankAccounts.put(cardNumber, new BankAccount(balance, active));
        }
    }

    // Helper classes
    public static class BankAccount {
        private double balance;
        private boolean active;

        public BankAccount(double balance, boolean active) {
            this.balance = balance;
            this.active = active;
        }

        public double getBalance() {
            return balance;
        }

        public void setBalance(double balance) {
            this.balance = balance;
        }

        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }
    }

    public static class PaymentVerificationResult {
        private final boolean verified;
        private final String message;

        public PaymentVerificationResult(boolean verified, String message) {
            this.verified = verified;
            this.message = message;
        }

        public boolean isVerified() {
            return verified;
        }

        public String getMessage() {
            return message;
        }
    }

    public static class PaymentProcessResult {
        private final boolean success;
        private final String message;
        private final String transactionId;

        public PaymentProcessResult(boolean success, String message) {
            this(success, message, null);
        }

        public PaymentProcessResult(boolean success, String message, String transactionId) {
            this.success = success;
            this.message = message;
            this.transactionId = transactionId;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }

        public String getTransactionId() {
            return transactionId;
        }
    }
}

