package kapitalNetwork;

import kapitalBot.BotInterface;
import kapitalMonopoly.MonopolyGame;
import kapitalMonopolyObservers.NetworkListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Network implements Serializable{

    private static final int PORT_NUMBER_MIN = 45000;
    private static final int PORT_CHANGE = 250;
    private static int[][] portNumbers;

    public static final String OK_MESSAGE = "OK";
    public static final String DISCONNECT_MESSAGE = "BlackHawkDown";
    public static final String ALIVE_MESSAGE = "I_AM_ALIVE_!";
    public static final int ALIVE_PERIOD = 3000;
    public static final int TOLERANCE_MESSAGE_DROP = 3;
    private boolean gameLoaded = false;


    private static final String[] IP_ADDRESSES = {"172.20.168.222", "172.20.168.244" , "172.20.168.251",  "172.20.80.239", "172.20.88.153" ,
            "localhost", "localhost", "localhost", "localhost"}; // will be filled with each player's IP address

    private static ClientThread[] clientThreads;
    private static ServerThread[] serverThreads;
    //private AliveChecker aliveChecker;

    private static int clientThreadCount;
    private static int serverThreadCount;
    private static int playerRow;
    private static Set<Integer> disconnectedPlayers;
    private static Set<Integer> connectedPorts;
    private static int botCount = 0;

    private static Network networkInstance = new Network(MonopolyGame.getPlayerID(), MonopolyGame.getPlayerCount()); // this is for example, it will be set up in the initialization

    private ArrayList<NetworkListener> networkListeners = new ArrayList<>();

    public void addNetworkListener(NetworkListener lis) {
        networkListeners.add(lis);
    }

    public void publishPropertyEvent(String name, String value){
        for(int i=0; i<networkListeners.size();i++){
            networkListeners.get(i).onNetworkEvent(this, name, value);
        }
    }
    public static Network getInstance() {
        return networkInstance;
    }

    private Network(int playerID, int playerCount){

        playerRow = playerID-1;
        disconnectedPlayers = new HashSet<>();
        connectedPorts = new HashSet<>();
        portNumbers = getPortNumbers(playerCount);

        clientThreadCount = playerID-1;
        serverThreadCount = playerCount-playerID;

        clientThreads = new ClientThread[clientThreadCount];
        serverThreads = new ServerThread[serverThreadCount];

        for(int i=0; i<clientThreadCount; i++){ // Serve as a client to the players with a lower ID
            if(!MonopolyGame.players[i].isBot()){
                clientThreads[i] = new ClientThread(IP_ADDRESSES[i], portNumbers[playerRow][i]);
                clientThreads[i].start();
            } else {
                clientThreads[i] = null;
                connectedPorts.add(i);
                botCount++;
            }
        }

        for(int i=0; i<serverThreadCount;i++){ // Serve as a server to the players with a higher ID
            if(!MonopolyGame.players[i+playerID].isBot()){
                serverThreads[i] = new ServerThread(portNumbers[playerRow][i + playerID]);
                serverThreads[i].start();
            }else{
                serverThreads[i] = null;
                connectedPorts.add(i + playerID);
                botCount++;
            }
        }
    }

    public void setGameLoaded(){
        gameLoaded = true;
    }

    private int[][] getPortNumbers(int size){
        portNumbers = new int[size][size];
        for(int i=0; i<size; i++){
            for(int j=0; j<size; j++){
                if(i==j) {
                    portNumbers[i][j] = 0;
                } else {
                    portNumbers[i][j] = PORT_NUMBER_MIN + (i+j)* PORT_CHANGE;
                }
            }
        }
        return portNumbers;
    }

    public boolean sendMessage(String message){
        boolean success = true;
        for(int i=0; i<clientThreadCount; i++){
            if(clientThreads[i] != null && !clientThreads[i].sendMessage(message)){
                removeThread(i, "ClientThread");
            }
        }
        for(int i=0; i<serverThreadCount; i++){
//        	System.out.println("SERVER THREAD COUNT: " + serverThreadCount);
            if(serverThreads[i] != null && !serverThreads[i].sendMessage(message)){
                removeThread(i,"ServerThread");
            }
        }
        return success;
    }

    public void receivedMessage(String message){
        System.out.println("RECEIVED:" + message);
        if(message.contains("ROLLED")){
            String value = message.substring("ROLLED".length()).trim();
            int r1 = Integer.parseInt(value.charAt(0)+"");
            int r2 = Integer.parseInt(value.charAt(1)+"");
            int r3 = Integer.parseInt(value.charAt(2)+"");
            MonopolyGame.getInstance().getCup().publishPropertyEventRoll("RollDice", value);
            MonopolyGame.getInstance().getCup().publishPropertyEventMove("MovePlayer", value);
            MonopolyGame.getInstance().getAnimation().animateRollDice(r1,r2,r3,r1==r2);
//            System.out.println("ROLLED: " + message.substring("ROLLED".length()));
            if(MonopolyGame.getInstance().getCurrentPlayer().isBot()){
                int[] rolled = new int[3];
                rolled[0] = r1;
                rolled[1] = r2;
                rolled[2] = r3;
                ((BotInterface) MonopolyGame.getInstance().getCurrentPlayer()).networkAct(rolled);
            }
        } else if(message.contains("ENDTURN")){
            MonopolyGame.getInstance().updateCurrentPlayerHelper();
        }else if(message.contains("BUYDEED")){
            MonopolyGame.getInstance().getCurrentPlayer().buyDeedHelper();
        } else if(message.contains("SELLDEED")){
            MonopolyGame.getInstance().getCurrentPlayer().sellDeedHelper();
        } else if(message.contains("BUYPROPERTY")){
            String type = message.substring("BUYPROPERTY".length()).trim();
            MonopolyGame.getInstance().getCurrentPlayer().buyProperty(MonopolyGame.getInstance().getCurrentPlayer().getPositionName(),type);
        } else if(message.contains("SELLPROPERTY")){
            String type = message.substring("SELLPROPERTY".length()).trim();
            MonopolyGame.getInstance().getCurrentPlayer().sellPropertyHelper(MonopolyGame.getInstance().getCurrentPlayer().getPositionName(),type);
        } else if(message.contains("ROLLTHREE")){
            String value = message.substring("ROLLTHREE".length());
            int r1 = Integer.parseInt(""+value.charAt(0));
            int r2 = Integer.parseInt(""+value.charAt(1));
            int r3 = Integer.parseInt(""+value.charAt(2));
            MonopolyGame.getInstance().rollThree(r1,r2,r3);
        } else if(message.contains("PAUSE")){
            publishPropertyEvent("PAUSE","");
        } else if(message.contains("RESUME")){
            publishPropertyEvent("RESUME","");
        } else if(message.contains("SAVEGAME")){
            Action.saveGameHelper();
        } else if(message.contains("CHAT")){
            publishPropertyEvent(message, message.substring("CHAT".length()+1));
        } else if(message.contains("PAYJAILBAIL")){
            MonopolyGame.getInstance().getCurrentPlayer().getBalance().decreaseAmount(Integer.parseInt(message.substring("PAYJAILBAIL".length())));
            MonopolyGame.getInstance().getCurrentPlayer().setFreeFromJail();
            publishPropertyEvent("PAYJAILBAIL",null);
        } else if(message.contains("CHANCE") && gameLoaded){
            MonopolyGame.getInstance().chanceDeck.publishDrawEvent("DrawCard", message.substring("CHANCE".length()));
        } else if(message.contains("COMMUNITY_CHEST") && gameLoaded){
            MonopolyGame.getInstance().communityChestDeck.publishDrawEvent("DrawCard", message.substring("COMMUNITY_CHEST".length()));
        }
    }

    public void blackHawkDown(int portNumber, String type){
        int threadIndex = 0;

        for(int i=0; i< portNumbers[playerRow].length;i++){
            if(portNumbers[playerRow][i] == portNumber){
                threadIndex = i;
                break;
            }
        }

        if(type.equalsIgnoreCase("ClientThread")){
            removeThread(threadIndex, type);
        } else if(type.equalsIgnoreCase("ServerThread")){
            threadIndex -= playerRow + 1;
            removeThread(threadIndex, type);
        }
    }

    public void disconnect(){
        if(connectedPorts.size() <= botCount){
            return;
        }
        for(int i=0; i<clientThreadCount; i++){
            if(clientThreads[i] != null){
                clientThreads[i].disconnect();
            }
        }
        for(int i=0; i<serverThreadCount; i++){
            if(serverThreads[i] != null) {
                serverThreads[i].disconnect();
            }
        }
    }

    private void removeThread(int index, String type){
        if(connectedPorts.size() <= botCount){
            return;
        }
        int playerID = playerRow + 1;
        int disconnectedPlayer;

//        System.out.println("Index : " + index + " , serverThreadCount : " + serverThreadCount + " , clientThreadCount : " + clientThreadCount);

        if(type.equalsIgnoreCase("ClientThread")){

            disconnectedPlayer = index + 1;
            if(disconnectedPlayers.contains(disconnectedPlayer)){
                return;
            }
            if(clientThreadCount!=1 && index!=clientThreadCount-1){
                clientThreads[index] = clientThreads[clientThreadCount-1];
            }
            clientThreadCount--;
        } else{
            disconnectedPlayer = playerID + 1 + index;
            if(disconnectedPlayers.contains(disconnectedPlayer)){
                return;
            }
            if(serverThreadCount!=1 && index!=serverThreadCount-1){
                serverThreads[index] = serverThreads[serverThreadCount-1];
            }
            serverThreadCount--;
        }
        disconnectedPlayers.add(disconnectedPlayer);
        MonopolyGame.getInstance().disconnect(disconnectedPlayer);
        publishPropertyEvent("ENDTURN", null);
    }

    public boolean getGameLoaded(){
        return gameLoaded;
    }

    public void addConnection(int portNumber){
        connectedPorts.add(portNumber);
        if(everyoneConnected()){
            if(gameLoaded){
              MonopolyGame.updateCurrentPlayerHelper();
              MonopolyGame.getInstance().publishTurnEvent("EndTurn", MonopolyGame.getInstance().getCurrentPlayer());
            }
//            aliveChecker = new AliveChecker(ALIVE_MESSAGE, ALIVE_PERIOD);
//            aliveChecker.start();
        }
    }

    public boolean everyoneConnected(){
        return connectedPorts.size() == (MonopolyGame.getPlayerCount() - 1 - botCount);
    }

    public boolean everyoneDisconnected(){
        return disconnectedPlayers.size() == (MonopolyGame.getPlayerCount() - 1 - botCount);
    }

    private class AliveChecker extends Thread {
        private String message;
        private int period;

        public AliveChecker(String message, int period){
            this.message = message;
            this.period = period;
        }

        public void run() {
            while(true){
                Network network = Network.getInstance();
                try {
                    Thread.sleep(period);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                network.sendMessage(message);
                if(network.everyoneDisconnected()){
                    return;
                }
            }
        }

    }

}





