package process.read.credit;

import core.CreditNote;
import core.Message;
import core.OrderRequest;
import core.entities.IMessage;

import java.util.HashMap;
import java.util.Map;

public class CreditNoteReader {
    // Simulated database of credit notes
    private static final Map<Integer, CreditNote> creditNotes = new HashMap<>();

    static {
        // Initialize with some test data
        creditNotes.put(1, new CreditNote(1, 1, 10000.0, true));
        creditNotes.put(2, new CreditNote(2, 2, 5000.0, true));
        // Add more as needed
    }

    public IMessage read(IMessage message) {
        if (!(message.getPayload() instanceof OrderRequest)) {
            message.setSuccess(false);
            message.setMessage("Invalid payload type. Expected OrderRequest.");
            return message;
        }

        OrderRequest orderRequest = (OrderRequest) message.getPayload();
        int custId = orderRequest.getOrder_info().getPayments().getCustId();

        // Check if customer has a credit note
        CreditNote creditNote = creditNotes.get(custId);
        if (creditNote == null) {
            // Create a new credit note with default values if not found
            creditNote = new CreditNote(custId, custId, 1000.0, true); // Default credit limit
            creditNotes.put(custId, creditNote);
        }

        return new Message(creditNote);
    }
}
