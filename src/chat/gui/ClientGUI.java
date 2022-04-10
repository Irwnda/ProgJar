package chat.gui;

import chat.client.Client;
import utils.Dbg;
import utils.Palette;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

import static java.awt.Image.SCALE_SMOOTH;

public class ClientGUI {
    // Data
    private ArrayList<String> clientList;

    // Component
    public JPanel mainPanel;
    private JPanel leftPanel;
    private JPanel rightPanel;
    private Client client;
    private JPanel topPanel, bottomPanel, midPanel;

    private JTextArea textArea;
    private JButton btnSend;
    private JButton btnDc;
    private JPanel btnPanel;
    private JPanel chatPanel;
    private JPanel statusBar;
    private JLabel status;

    private JFrame frame;

    public ClientGUI(Client client){
        this.client = client;
        this.clientList = client.getClients();

        frame = new JFrame();
        frame.setMinimumSize(new Dimension(300, 720));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBackground(Color.BLACK);

        frame.setTitle("ngeChat | Socket Chat App");

        createUIComponents();
        frame.setContentPane(mainPanel);

        frame.pack();
        frame.setLayout(null);
        frame.setVisible(true);
        frame.setResizable(false);

    }

    private void createUIComponents() {
       status.setText("Disconnected");
        status.setForeground(Color.RED);

        createHomePanel();
        createLeftPanel();
        createBottomPanel();

    }


    private void createHomePanel() {
        chatPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));

        JLabel welcomeLabel = new JLabel("Welcome to ngeChat");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 30));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel continueLabel = new JLabel("Enter Username to Continue !");
        continueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField textField = new JTextField();
        textField.setPreferredSize( new Dimension( 200, 24 ) );
        JButton loginBtn = new JButton("Login!");
        loginBtn.addActionListener(e -> {
            String userName = textField.getText();
            doLogin(userName);
        });

        JPanel loginPanel = new JPanel();
        loginPanel.setBackground(Palette.BIRUMUDA);
        loginPanel.setLayout(new FlowLayout());
        loginPanel.add(textField);
        loginPanel.add(loginBtn);

        chatPanel.add(Box.createRigidArea(new Dimension(1, 300)));
        chatPanel.add(welcomeLabel);
        chatPanel.add(Box.createRigidArea(new Dimension(1, 10)));
        chatPanel.add(continueLabel);
        chatPanel.add(loginPanel);
    }

    private void createLeftPanel() {
        updateClientList();
    }

    private void createBottomPanel() {
        setBottomEnable(false);

        textArea.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        btnSend.addActionListener(e -> client.sendMessage(textArea.getText()));
        btnDc.addActionListener(e -> doDisconnect());
    }

    public void doLogin(String userName) {
        client.setUserName(userName);
        client.registerClient(userName);

        status.setText("Connected : " + client.getUserName());
        status.setForeground(Color.BLUE);

        setBottomEnable(true);

        chatPanel.removeAll();
        chatPanel.revalidate();
        chatPanel.repaint();
    }

    public void doDisconnect() {
        client.disconnectClient(client.getUserName());
        client.setUserName("");

        status.setText("Disconnected");
        status.setForeground(Color.RED);

        setBottomEnable(false);

        createHomePanel();
        chatPanel.revalidate();
        chatPanel.repaint();
    }

    public void setBottomEnable(Boolean isEnable) {
        textArea.setEnabled(isEnable);
        btnSend.setEnabled(isEnable);
        btnDc.setEnabled(isEnable);
    }

    public void updateClientList() {
        leftPanel.removeAll();

        clientList = client.getClients();
        Dbg.debugKu("Updating client with : " + clientList);

        try {
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.anchor = GridBagConstraints.FIRST_LINE_START;
            gbc.insets = new Insets(5,5,5,5);
            gbc.gridx = 0;
            gbc.gridy = 0;

            JLabel nameLabel = new JLabel("Global");
            Image globalIcon = ImageIO.read(new URL("https://cdn-icons-png.flaticon.com/512/1383/1383676.png"));
            nameLabel.setIcon(new ImageIcon(globalIcon.getScaledInstance(20, 20, SCALE_SMOOTH)));
            leftPanel.add(nameLabel, gbc);

            for (int i = 0; i < clientList.size(); i++) {
                String uname = clientList.get(i);
                URL url = new URL("https://avatars.dicebear.com/api/initials/"+uname+".png");
                Image myPicture = ImageIO.read(url);

                JLabel onlineClient = new JLabel(uname);
                onlineClient.setAlignmentX(Component.LEFT_ALIGNMENT);
                onlineClient.setIcon(new ImageIcon(myPicture.getScaledInstance(20, 20, SCALE_SMOOTH)));

                gbc.gridx = 0;
                gbc.gridy = i+1;
                leftPanel.add(onlineClient, gbc);
                Dbg.debugKu("Client Image added !!!");
            }

            leftPanel.revalidate();
            leftPanel.repaint();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
