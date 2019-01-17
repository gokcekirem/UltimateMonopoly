package kapitalBot;

import kapitalMonopoly.MonopolyGame;
import kapitalMonopoly.Player;
import kapitalMonopoly.kapitalBoard.DeedSquare;

public abstract class Bot extends Player{

	public boolean[] rollDouble = new boolean[2];
	public int inJailTurn = 0;
	public int adminID = 1;
	private String botType;

	public Bot(String icon, int ID, String botType){
		super(icon, ID, true);
		this.botType = botType;
	}
	
	public boolean isOnBuyableDeed() {
		boolean buyable = false;
		System.out.println("Control");
		System.out.println(getPiece().getPosition());
		if(getPiece().getPosition().isDeedSquare()) {
			DeedSquare deedSquare = (DeedSquare) getPiece().getPosition();
			buyable = !MonopolyGame.getInstance().getDeedOwners().containsKey(deedSquare) && getBalance().getBalanceMoneyAmount() >= deedSquare.getPrice();
		}
		return buyable;
	}

	public String getBotType(){
		return botType;
	}

	public void setAdminID(int ID){
		adminID = ID;
	}

	public boolean isAdmin(){
		return adminID == MonopolyGame.getPlayerID();
	}

}

//	public Square onDeed() {
//		Square deed = null;
//		if(isOnBuyableDeed()) {
//			deed = piece.getPosition();
//		}
//		return deed;
//	}