package kapitalNetwork;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.Socket;

public class ClientThread extends Thread implements Serializable{

    private final String THREAD_TYPE = "ClientThread";
    private BufferedReader is;
    private PrintWriter os;
    private Socket socket;
    private int connectionPort;
    private String serverAddress;
    private long lastAlive;

    public ClientThread(String serverAddress, int connectionPort){
        this.serverAddress = serverAddress;
        this.connectionPort = connectionPort;
    }

    public void run() {
        setUpConnection();
        work();
    }

    public void work(){
        while(true){

            String readMessage = new String();
            while(readMessage.isEmpty()){

                try {
                    if(is.ready()) {
                        readMessage = is.readLine();
                    }
                } catch (IOException e) {
                    Network.getInstance().blackHawkDown(connectionPort, THREAD_TYPE);
                    return;
                }
            }

//            if(readMessage.equalsIgnoreCase(Network.ALIVE_MESSAGE)){
//                lastAlive = System.currentTimeMillis();
//            } else {
//                long sinceLastAlive = lastAlive - System.currentTimeMillis();
//                lastAlive = System.currentTimeMillis();
//                if(sinceLastAlive > Network.ALIVE_PERIOD * Network.TOLERANCE_MESSAGE_DROP){
//                    Network.getInstance().blackHawkDown(connectionPort,THREAD_TYPE);
//                    return;
//                }
//            }

            if(readMessage.equalsIgnoreCase(Network.DISCONNECT_MESSAGE) || !internalSend(Network.OK_MESSAGE )) {
                Network.getInstance().blackHawkDown(connectionPort, THREAD_TYPE);
                return;
            }
            if(!readMessage.contains(Network.OK_MESSAGE) && !readMessage.equalsIgnoreCase(Network.ALIVE_MESSAGE)) {
                Network.getInstance().receivedMessage(readMessage);
            }

        }
    }

    private void setUpConnection(){
        try {
            System.out.println("connecting .... " + Integer.toString(connectionPort));
            socket = new Socket(serverAddress, connectionPort);
            is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            os = new PrintWriter(socket.getOutputStream());
            System.out.println("Connected");
            Network.getInstance().addConnection(connectionPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean sendMessage(String message){
        String success =  new String();
        //System.out.println("CLIEANTTHREAD IN SENDMESSAGE()");
        while(!success.equals(Network.OK_MESSAGE)){
            if(!internalSend(message)){
                return false;
            }
            try {
                success = is.readLine();
            } catch (IOException e) {
                return false;
            }
        }
        return true;
    }

    public void disconnect(){
        System.out.println("Sending disconnection");
        internalSend(Network.DISCONNECT_MESSAGE);
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        os.close();
    }

    private boolean internalSend(String message){
        if(os == null){
            return false;
        }
        os.println(message);
        os.flush();
        return true;
    }
}
