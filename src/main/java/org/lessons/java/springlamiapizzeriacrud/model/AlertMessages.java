package org.lessons.java.springlamiapizzeriacrud.model;

public class AlertMessages {

    public enum typeAlert {
        SUCCESS,
        ERROR;
    }

    typeAlert typeAlertMessage;

    private String textMessage;

    public AlertMessages(typeAlert typeAlertMessage, String textMessage) {
        this.typeAlertMessage = typeAlertMessage;
        this.textMessage = textMessage;
    }

    public AlertMessages() {

    }

    public typeAlert getTypeAlertMessage() {
        return typeAlertMessage;
    }

    public void setTypeAlertMessage(typeAlert typeAlertMessage) {
        this.typeAlertMessage = typeAlertMessage;
    }

    public String getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }
}
