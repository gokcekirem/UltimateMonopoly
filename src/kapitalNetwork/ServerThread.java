package kapitalNetwork;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerThread extends Thread implements Serializable{

    public final String THREAD_TYPE = "ServerThread";
    private BufferedReader is;
    private PrintWriter os;
    private Socket socket;
    private ServerSocket serverSocket;
    private int connectionPort;
    private long lastAlive;

    public ServerThread(int connectionPort) {
        this.connectionPort = connectionPort;
    }

    public void run(){
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
            serverSocket = new ServerSocket(connectionPort);
            System.out.println("Opened connection on Port Number: " + connectionPort);
            socket = serverSocket.accept();
            System.out.println("Connection established on port: " + connectionPort + " with : " + socket.getRemoteSocketAddress());
            is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            os = new PrintWriter(socket.getOutputStream());
            Network.getInstance().addConnection(connectionPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean sendMessage(String message){
        String success = new String();
        //System.out.println("SERVERTHREAD IN sendMessage()");
        while(!success.equals(Network.OK_MESSAGE)){
            //System.out.println("Sending:" + message);
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
