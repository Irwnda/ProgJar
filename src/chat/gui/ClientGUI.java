package chat.gui;

import chat.client.Client;
import chat.object.Person;
import utils.Dbg;
import utils.Palette;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

import static java.awt.Image.SCALE_SMOOTH;

public class ClientGUI {
    // Data
    String[] avatarsType = { "initials", "male", "female", "human", "identicon", "bottts", "avataaars", "jdenticon", "gridy", "micah"};
    private ArrayList<Person> clientList;

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
    private JPanel headerPanel;

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

    public void updateHeaderPanel() {
        headerPanel.removeAll();

        JLabel targetLabel = new JLabel(client.getTargetSend());
        URL url = null;
        try {
            if(client.getTargetSend().equals("global"))
                url = new URL("https://cdn-icons-png.flaticon.com/512/1383/1383676.png");
            else
                url = new URL("https://avatars.dicebear.com/api/"+client.getTargetProfile()+"/"+client.getTargetSend()+".png");

            Image myPicture = ImageIO.read(url);
            targetLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            targetLabel.setIcon(new ImageIcon(myPicture.getScaledInstance(40, 40, SCALE_SMOOTH)));
            headerPanel.add(targetLabel, BorderLayout.BEFORE_LINE_BEGINS);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        headerPanel.revalidate();
        headerPanel.repaint();
    }

    private void createChatPanel() {
        chatPanel.removeAll();

        headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        headerPanel.setMaximumSize(new Dimension(chatPanel.getWidth(), 50));
        headerPanel.setBackground(Palette.ABU);

        updateHeaderPanel();

        JScrollPane scroller = new JScrollPane();

        chatPanel.add(headerPanel);
        chatPanel.add(scroller);

        chatPanel.revalidate();
        chatPanel.repaint();
    }

    private void createHomePanel() {
        chatPanel.removeAll();
        chatPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));

        JLabel welcomeLabel = new JLabel("Welcome to ngeChat");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 30));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel continueLabel = new JLabel("Enter Username to Continue !");
        continueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel avatarPanel = new JPanel(new FlowLayout());
        avatarPanel.setBackground(Palette.BIRUMUDA);
        JLabel avatarLabel = new JLabel("Avatar:");
        JRadioButton[] avatarButton = new JRadioButton[10];
        ButtonGroup avatarBtnGroup = new ButtonGroup();
        avatarPanel.add(avatarLabel);
        for(int i=0; i<avatarsType.length; i++){
            avatarButton[i] = new JRadioButton();
            avatarButton[i].setText(avatarsType[i]);
            avatarButton[i].setBackground(Palette.BIRUMUDA);
            avatarBtnGroup.add(avatarButton[i]);
            avatarPanel.add(avatarButton[i]);
        }

        JTextField textField = new JTextField();
        textField.setPreferredSize( new Dimension( 200, 24 ) );
        JButton loginBtn = new JButton("Login!");
        loginBtn.addActionListener(e -> {
            int selected = 0;
            for(int i=0;i<avatarsType.length; i++){
                if(avatarButton[i].isSelected()){
                    selected = i;
                }
            }
            String userName = textField.getText();
            doLogin(userName, avatarsType[selected]);
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
        chatPanel.add(avatarPanel);
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

    public void doLogin(String userName, String profileType) {
        client.setUserName(userName);
        client.registerClient(userName, profileType);

        status.setText("Connected : " + client.getUserName());
        status.setForeground(Color.BLUE);

        setBottomEnable(true);

        createChatPanel();
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
        Dbg.debugKu("Updating client with : " + client.getClientsList());

        try {
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.anchor = GridBagConstraints.FIRST_LINE_START;
            gbc.insets = new Insets(5,5,5,5);
            gbc.gridx = 0;
            gbc.gridy = 0;

            JLabel globalLabel = new JLabel("Global");
            Image globalIcon = ImageIO.read(new URL("https://cdn-icons-png.flaticon.com/512/1383/1383676.png"));
            globalLabel.setIcon(new ImageIcon(globalIcon.getScaledInstance(25, 25, SCALE_SMOOTH)));
            globalLabel.setFont(new Font("Arial", Font.PLAIN, 25));
            globalLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    client.setTargetSend("global");
                    updateHeaderPanel();
                }
            });
            leftPanel.add(globalLabel, gbc);

            for (int i = 0; i < clientList.size(); i++) {
                String uname = clientList.get(i).getUserName();
                String profileType = clientList.get(i).getProfileType();
                URL url = new URL("https://avatars.dicebear.com/api/"+profileType+"/"+uname+".png");
                Image myPicture = ImageIO.read(url);

                JLabel clientLabel = new JLabel(uname);
                clientLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                clientLabel.setIcon(new ImageIcon(myPicture.getScaledInstance(25, 25, SCALE_SMOOTH)));
                clientLabel.setFont(new Font("Arial", Font.PLAIN, 25));
                clientLabel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        client.setTargetSend(uname);
                        client.setTargetProfile(profileType);
                        updateHeaderPanel();
                    }
                });

                gbc.gridx = 0;
                gbc.gridy = i+1;
                leftPanel.add(clientLabel, gbc);
            }

            leftPanel.revalidate();
            leftPanel.repaint();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
