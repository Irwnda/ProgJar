package chat.object;

import java.io.Serializable;

public class Message implements Serializable {
    private String sender;
    private String text;
    private int action;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getAction(){
        return action;
    }

    public void setAction(int action){
        this.action = action;
    }
}