package kapitalMonopoly;

import kapitalBot.BotInterface;
import kapitalMonopoly.kapitalBoard.ActionSquare;
import kapitalMonopoly.kapitalBoard.Board;
import kapitalMonopoly.kapitalBoard.Square;
import kapitalMonopolyObservers.DeedListener;
import kapitalMonopolyObservers.JailListener;
import kapitalMonopolyObservers.MoveListener;
import kapitalNetwork.Network;

import java.io.Serializable;
import java.util.ArrayList;

public class Piece implements MoveListener, Serializable{

	static ArrayList<DeedListener> deedListeners = new ArrayList<>();
	static ArrayList<JailListener> jailListeners = new ArrayList<>();

	private final int BUS_ROLL = 4;
	private final int MR_MONOPOLY_ROLL1 = 5;
	private final int MR_MONOPOLY_ROLL2 = 6;

	private String icon;
	private Square position;
	private int playerID;

	private ArrayList<int[]> movePath = new ArrayList<>();
	public void addToMovePath(int[] squarePosition) {
		movePath.add(squarePosition);
	}


	public void addDeedListener(DeedListener lis) {
		deedListeners.add(lis);
	}
	public void addJailListener(JailListener lis) { jailListeners.add(lis); }


	public void publishPropertyDeedEvent(String name, String value){
		for(int i=0; i<deedListeners.size();i++){
			deedListeners.get(i).onDeedEvent(this, name, value);

		}
	}

	public void publishJailEvent(){
		for (int i=0;i<jailListeners.size();i++){
			jailListeners.get(i).onJailEvent();
		}
	}

	@SuppressWarnings("static-access")
	public Piece(String icon, int playerID) {
		this.playerID = playerID;
		position = Board.getInstance().getSquare("Go");
		this.icon=icon;
		MonopolyGame.getInstance().getCup().addMoveListener(this);

	}

	public String getIcon() {
		return icon;
	}

	public Square getPosition() {
		return position;
	}
	
	public ArrayList<int[]> getMovePath() {
		return movePath;
	}

	public void clearMovePath() {
		movePath.clear();
	}

	public String getPositionName() {
		return position.getName();
	}

	public int getX() {
		return position.getCoordinates(playerID)[0];
	}

	public int getY() {
		return position.getCoordinates(playerID)[1];
	}

	public void setPosition(Square position) {
		this.position = position;
		String value;
		if(position.isDeedSquare()) {
			value = position.getName();
		} else {
			value = "";
		}
		publishPropertyDeedEvent("DeedShow", value);
	}

	private Square temp;
	public Square getTemp(){
		return temp;
	}
	public void setTemp(Square temp){
		this.temp = temp;
	}
	
	@SuppressWarnings("static-access")
	@Override
	public void onMoveEvent(Object source, String name, String value) {
		if(MonopolyGame.getCurrentPlayerID() != playerID){
			return;
		}
		if(Network.getInstance().getGameLoaded()){
			position = MonopolyGame.getInstance().getCurrentPlayer().getPosition();
		}
		System.out.print("CALLED:" + value);
		int playerID = MonopolyGame.getCurrentPlayerID();
		int r1 = Integer.parseInt(""+value.charAt(0));
		int r2 = Integer.parseInt(""+value.charAt(1));

		temp = MonopolyGame.getInstance().getCurrentPlayer().getPiece().getPosition();
		movePath.add(temp.getCoordinates(playerID));
		
		if(MonopolyGame.getInstance().getCurrentPlayer().getDirection() == 1) {
			for (int i = 0; i < r1 + r2; i++) {
				if (temp.getName().contains("Station") || temp.getName().contains("Railroad") || temp.getName().equals("Short Line")) {
					if ((r1 + r2) % 2 == 0) {
						temp = Board.getInstance().getNextSquare2(temp);
						movePath.add(temp.getCoordinates(playerID));
					}
				}
				temp = Board.getInstance().getNextSquare(temp);
				
				ActionSquare.doActionAsPassingFrom(temp);
				
				movePath.add(temp.getCoordinates(playerID));
			}

			if (Integer.parseInt("" + value.charAt(2)) < 4) {
				for (int i = 0; i < Integer.parseInt("" + value.charAt(2)); i++) {
					if (temp.getName().contains("Station") || temp.getName().contains("Railroad") || temp.getName().equals("Short Line")) {
						if ((r1 + r2) % 2 == 0) {
							temp = Board.getInstance().getNextSquare2(temp);
							movePath.add(temp.getCoordinates(playerID));
						}
					}
					temp = Board.getInstance().getNextSquare(temp);
					
					ActionSquare.doActionAsPassingFrom(temp);
					
					System.out.println("\n => "+temp.getName());
					movePath.add(temp.getCoordinates(playerID));
				}
			}

			if(Integer.parseInt(""+value.charAt(2)) > 3 && temp.isActionSquare()) {
				if (temp.getName().contains("Chance") || temp.getName().contains("Community")) {
					MonopolyGame.getInstance().getCurrentPlayer().getPiece().setPosition(temp);
					((ActionSquare) MonopolyGame.getInstance().getCurrentPlayer().getPiece().getPosition()).doAction();
				}
			}
			
			if(temp.isActionSquare()) {
				System.out.println("control1234");
				if (temp.getName().contains("Chance") || temp.getName().contains("Community")) {
					MonopolyGame.getInstance().getCurrentPlayer().getPiece().setPosition(temp);
					((ActionSquare) MonopolyGame.getInstance().getCurrentPlayer().getPiece().getPosition()).doAction();
				}
			}
			
			
			System.out.println("control");
			if (Integer.parseInt("" + value.charAt(2)) == MR_MONOPOLY_ROLL1 || Integer.parseInt("" + value.charAt(2)) == MR_MONOPOLY_ROLL2) {
				ArrayList<Square> monopolyMovePath = mrMonopolyMove(temp);
				temp = monopolyMovePath.get(monopolyMovePath.size() - 1);
				for (int i = 0; i < monopolyMovePath.size(); i++) {
					movePath.add(monopolyMovePath.get(i).getCoordinates(playerID));
				}
			} else if (Integer.parseInt("" + value.charAt(2)) == BUS_ROLL) {
				ArrayList<Square> busMovePath = busMove(temp);
				if (busMovePath.size() != 0) {
					temp = busMovePath.get(busMovePath.size() - 1);
					for (int i = 0; i < busMovePath.size(); i++) {
						movePath.add(busMovePath.get(i).getCoordinates(playerID));
					}
				}
			}
			this.setPosition(temp);
			ArrayList<int[]> movePath= DomainController.getPiecePath(DomainController.getCurrentPlayerID());
			int currentPlayerId = DomainController.getCurrentPlayerID();
			MonopolyGame.getInstance().getAnimation().animatePiece(movePath, r1==r2, currentPlayerId);
			if (MonopolyGame.getInstance().getCurrentPlayer().getPiece().getPosition().isActionSquare()) {
				((ActionSquare) MonopolyGame.getInstance().getCurrentPlayer().getPiece().getPosition()).doAction();
			}
			}
	}

	public ArrayList<Square> mrMonopolyMove(Square temp){
		ArrayList<Square> monopolyMovePath = new ArrayList<Square>();
		//if all are bought, go anywhere you like must be implemented
//		System.out.println("\nBEFORE START OF MONOPOLYMOVE =>" + temp.getName());
		temp = Board.getInstance().getNextSquare(temp);
		monopolyMovePath.add(temp);
		while(temp.isActionSquare() || MonopolyGame.getInstance().getDeedOwners().containsKey(temp)) {
			temp = Board.getInstance().getNextSquare(temp);
			monopolyMovePath.add(temp);
		}

		return monopolyMovePath;
	}

	public ArrayList<Square> busMove(Square temp) {

		ArrayList<Square> busMovePath = new ArrayList<>();
//		System.out.println("\nBEFORE START OF BUSMOVE =>" + temp.getName());
		if(temp.getName().contains("Chance") || temp.getName().contains("Community")){
			temp = Board.getInstance().getNextSquare(temp);
			busMovePath.add(temp);
		}
		while(!temp.getName().contains("Chance") && !temp.getName().contains("Community")){
			temp = Board.getInstance().getNextSquare(temp);
			busMovePath.add(temp);
		}
		MonopolyGame.getInstance().getCurrentPlayer().getPiece().setPosition(temp);
//		System.out.println(", BUS AFTER=> "+position.getName());

		return busMovePath;
	}

	public ArrayList<Square> mrMonopolyMoveReverse(Square temp){
		ArrayList<Square> monopolyMovePath = new ArrayList<Square>();
		//if all are bought, go anywhere you like must be implemetned
//		System.out.println("\nBEFORE START OF MONOPOLYMOVE =>" + temp.getName());
		temp = Board.getInstance().getPrevSquare(temp);
		monopolyMovePath.add(temp);
		while(temp.isActionSquare() || MonopolyGame.getInstance().getDeedOwners().containsKey(temp)) {
			temp = Board.getInstance().getPrevSquare(temp);
			monopolyMovePath.add(temp);
		}

		return monopolyMovePath;
	}

	public ArrayList<Square> busMoveReverse(Square temp) {

		ArrayList<Square> busMovePath = new ArrayList<>();
//		System.out.println("\nBEFORE START OF BUSMOVE =>" + temp.getName());
		if(temp.getName().contains("Chance") || temp.getName().contains("Community")){
			temp = Board.getInstance().getPrevSquare(temp);
			busMovePath.add(temp);
		}
		while(!temp.getName().contains("Chance") && !temp.getName().contains("Community")){
			temp = Board.getInstance().getPrevSquare(temp);
			busMovePath.add(temp);
		}
		MonopolyGame.getInstance().getCurrentPlayer().getPiece().setPosition(temp);
//		System.out.println(", BUS AFTER=> "+position.getName());

		return busMovePath;
	}

	public void sendToJail(){
		MonopolyGame.getInstance().getCurrentPlayer().getPiece().movePath.add(Board.getInstance().getSquare("Jail").getCoordinates(playerID));
		setPosition(Board.getInstance().getSquare("Jail"));
		if(!MonopolyGame.getInstance().getCurrentPlayer().isBot()){
			publishJailEvent();
		} else{
			if(((BotInterface) MonopolyGame.getInstance().getCurrentPlayer()).isAdmin()){
				MonopolyGame.getInstance().updateCurrentPlayer();
			}
		}
	}

	public void updateMovePath(int[] coordinates) {
		MonopolyGame.getInstance().getCurrentPlayer().getPiece().movePath.add(coordinates);
	}

	public void setMovePath(ArrayList<int[]> path){
		this.movePath = path;
	}

	public int getPlayerID(){
		return playerID;
	}
}

