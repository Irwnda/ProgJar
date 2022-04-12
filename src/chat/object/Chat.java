package chat.object;

public class Chat {
    public String sender;
    public String text;
    public boolean isSender;

    public Chat(String sender, String text, boolean isSender){
        this.sender = sender;
        this.text =  text;
        this.isSender = isSender;
    }
}
