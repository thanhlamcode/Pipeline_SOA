package process.read.note;

import core.Message;
import core.Note;
import core.entities.IMessage;

import java.util.concurrent.atomic.AtomicInteger;

public class NoteReader {
    private static final AtomicInteger noteIdGenerator = new AtomicInteger(1);

    public IMessage read(IMessage message) {
        if (!(message.getPayload() instanceof String)) {
            message.setSuccess(false);
            message.setMessage("Invalid payload type. Expected String.");
            return message;
        }

        String content = (String) message.getPayload();
        Note note = new Note(
                noteIdGenerator.getAndIncrement(),
                content,
                "ORDER_NOTE",
                "ACTIVE"
        );

        return new Message(note);
    }
}
