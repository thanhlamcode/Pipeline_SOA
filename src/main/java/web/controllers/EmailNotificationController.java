package web.controllers;

import core.InvoiceInfo;
import core.entities.IMessage;
import core.Message;
import filters.EmailNotificationFilter;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notification")
public class EmailNotificationController {

    private final EmailNotificationFilter emailNotificationFilter;

    public EmailNotificationController() {
        this.emailNotificationFilter = new EmailNotificationFilter();
    }

    @PostMapping("/send-email")
    public IMessage sendEmailNotification(@RequestBody InvoiceInfo invoiceInfo) {
        IMessage message = new Message(invoiceInfo);
        return emailNotificationFilter.execute(message);
    }
}
