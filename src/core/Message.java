package core;

import core.entities.IMessage;

public class Message implements IMessage {
    private Object payload;
    private boolean success = true;
    private String message = "";

    public Message(Object payload) {
        this.payload = payload;
    }

    @Override
    public Object getPayload() {
        return payload;
    }

    @Override
    public void setPayload(Object payload) {
        this.payload = payload;
    }

    @Override
    public boolean isSuccess() {
        return success;
    }

    @Override
    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }
}
