package kapitalMonopolyUI;

import kapitalMonopoly.DomainController;
import kapitalMonopolyObservers.NetworkListener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChatPanel implements NetworkListener {

    protected static final int PANEL_WIDTH = 630;
    protected static final int PANEL_HEIGHT = 230;
    public static final int[] CHATBOX = {188, 30, 404, 150};
    public static final int[] TEXTBOX = {187,189,333,20};
    public static final String CHATBOX_INFO = "...\n...";
    public static final int[] SEND_BUTTON = {350,185,65,20};
    private static final String CHAT_LOGO_FILE = "resources/MonopolyChat.png";
    protected JTextArea chatBox;
    protected JTextField textBox;
    protected JButton sendButton;
    public JPanel chatJPanel;

    private final static int CHAT_COLUMNS = 9;

    public ChatPanel(){
        initialize();
    }

    private void initialize(){

        DomainController.addNetworkListener(this);
        chatJPanel = new JPanel();
        chatJPanel.setBounds(BoardPanel.BOARD_WIDTH + MonopolyGameFrame.BREAK_AMOUNT*2,
                PlayerPanel.PANEL_HEIGHT + CardPanel.PANEL_HEIGHT + DiePanel.PANEL_HEIGHT
                        + MonopolyGameFrame.BREAK_AMOUNT *2,
                PANEL_WIDTH, PANEL_HEIGHT);

        chatJPanel.setLayout(null);

        chatBox = new JTextArea();
        chatBox.setBounds(CHATBOX[0], CHATBOX[1], CHATBOX[2], CHATBOX[3]);
        chatBox.setEditable(false);
        chatBox.setColumns(CHAT_COLUMNS);
        chatBox.setText(CHATBOX_INFO);
        textBox = new JTextField();
        textBox.setBounds(TEXTBOX[0],TEXTBOX[1],TEXTBOX[2],TEXTBOX[3]);
        textBox.setText("");
        sendButton = new JButton("send");
        sendButton.setBounds(527,189,SEND_BUTTON[2],SEND_BUTTON[3]);



        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = textBox.getText();
                addToChat(">>You: " + message);
                DomainController.sendMessage(DomainController.getPlayerID() + "CHAT" + message);
                textBox.setText("");
            }
        });


        JLabel monopolyChat = new JLabel("");
        monopolyChat.setBounds(73,30,118,170);
        chatJPanel.add(monopolyChat);
        monopolyChat.setIcon(new ImageIcon(CHAT_LOGO_FILE));

        chatJPanel.add(chatBox);
        chatJPanel.add(textBox);
        chatJPanel.add(sendButton);
    }

    @Override
    public void onNetworkEvent(Object source, String name, String value) {
        if(name.contains("CHAT")){
            String sender = "->Player " + name.charAt(0) + ": ";
            String chat = sender + value;
            addToChat(chat);
        }
    }

    private void addToChat(String message){
        String[] myArray = chatBox.getText().split("\n");
        if(myArray.length == CHAT_COLUMNS){
            String chat = new String();
            for(int i=0; i<CHAT_COLUMNS-1;i++){
                chat += myArray[i+1] + "\n";
            }
            chat += message;
            chatBox.setText(chat);
        } else{
            chatBox.append("\n" + message);
        }
    }
}
