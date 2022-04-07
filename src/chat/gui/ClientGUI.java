package chat.gui;

import chat.client.Client;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class ClientGUI {
    private Client client;

    public ClientGUI(Client client) {
        this.client = client;

        JFrame frame = new JFrame();
        frame.setMinimumSize(new Dimension(600, 1080));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        String title = "Not Set";
        if(client.getUserName() != "")
            title = client.getUserName();
        frame.setTitle("Group Chat : ["+title+"]");

        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(40,40,40,40));

        JTextArea textArea = new JTextArea("Enter your Messages Here", 5,40);
        textArea.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JButton btnSend = new JButton("Send");
        JButton btnDc = new JButton("Disconnect");
        if(client.getUserName() == ""){
            btnDc.setText("Login");
        }

        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.PAGE_AXIS));
        btnPanel.add(btnSend);
        btnPanel.add(Box.createRigidArea(new Dimension(0,10)));
        btnPanel.add(btnDc);

        panel.add(textArea);
        panel.add(btnPanel);

        frame.add(panel, BorderLayout.PAGE_END);
        frame.pack();
        frame.setLayout(null);
        frame.setVisible(true);
    }
}
