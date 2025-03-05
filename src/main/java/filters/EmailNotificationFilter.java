package filters;

import core.InvoiceInfo;
import core.entities.IFilter;
import core.entities.IMessage;

public class EmailNotificationFilter implements IFilter {
    @Override
    public boolean isMatch(IMessage message) {
        return message.getPayload() instanceof InvoiceInfo;
    }

    @Override
    public IMessage execute(IMessage message) {
        InvoiceInfo invoiceInfo = (InvoiceInfo) message.getPayload();

        // In a real system, we would send an actual email
        // For this example, we'll just simulate the email sending

        System.out.println("Email sent to customer ID: " + invoiceInfo.getInvoice().getCustId());
        System.out.println("Invoice ID: " + invoiceInfo.getInvoice().getInvoiceId());
        System.out.println("Total Amount: $" + invoiceInfo.getInvoice().getAmount());
        System.out.println("Delivery Address: " + invoiceInfo.getDelivery().getDeliveryAdd());

        return message;
    }
}
