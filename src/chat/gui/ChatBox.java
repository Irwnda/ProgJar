package chat.gui;

import chat.object.Chat;

import javax.swing.*;
import java.awt.*;

public class ChatBox extends JPanel {
    ChatBox(Chat chat){
//        setLayout(new FlowLayout());
        if(chat.isSender)
            setBackground(Color.cyan);
        else
            setBackground(Color.ORANGE);

        JLabel sender = new JLabel(chat.sender + " : ");
        JLabel text = new JLabel(chat.text);

        add(sender);
        add(text);
    }
}
