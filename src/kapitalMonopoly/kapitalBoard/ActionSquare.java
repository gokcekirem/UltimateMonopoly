package kapitalMonopoly.kapitalBoard;

import kapitalMonopoly.MonopolyGame;

import java.io.Serializable;

public class ActionSquare extends Square implements Serializable{

	private final static String CARD_TYPE = "Action";
	
	
	public ActionSquare(String name, String nextSquare, String nextSquare2, String prevSquare, int[] borders) {
		super(name, nextSquare, nextSquare2, prevSquare, CARD_TYPE, borders);
	}
	
	public static String getCardType() {
		return CARD_TYPE;
	}


	@SuppressWarnings({ "static-access"})
	public void doAction() {
//		if(MonopolyGame.getCurrentPlayerID() != MonopolyGame.getPlayerID()) {
//			return;
//		}
		switch(MonopolyGame.getInstance().getCurrentPlayer().getPositionName()) {
			case "Chance 1":
				MonopolyGame.getInstance().chanceDeck.drawCard();
				break;
			case "Chance 2":
				MonopolyGame.getInstance().chanceDeck.drawCard();
				break;
			case "Chance 3":
				MonopolyGame.getInstance().chanceDeck.drawCard();
				break;
			case "Chance 4":
				MonopolyGame.getInstance().chanceDeck.drawCard();
				break;
			case "Chance 5":
				MonopolyGame.getInstance().chanceDeck.drawCard();
				break;
			case "Chance 6":
				MonopolyGame.getInstance().chanceDeck.drawCard();
				break;
			case "Chance 7":
				MonopolyGame.getInstance().chanceDeck.drawCard();
				break;
			case "Chance 8":
				MonopolyGame.getInstance().chanceDeck.drawCard();
				break;
			case "Community Chest 1":
				MonopolyGame.getInstance().communityChestDeck.drawCard();
				break;
			case "Community Chest 2":
				MonopolyGame.getInstance().communityChestDeck.drawCard();
				break;
			case "Community Chest 3":
				MonopolyGame.getInstance().communityChestDeck.drawCard();
				break;
			case "Community Chest 4":
				MonopolyGame.getInstance().communityChestDeck.drawCard();
				break;
			case "Community Chest 5":
				MonopolyGame.getInstance().communityChestDeck.drawCard();
				break;
			case "Community Chest 6":
				MonopolyGame.getInstance().communityChestDeck.drawCard();
				break;
			case "Community Chest 7":
				MonopolyGame.getInstance().communityChestDeck.drawCard();
				break;
			case "Community Chest 8":
				MonopolyGame.getInstance().communityChestDeck.drawCard();
				break;
			case "Roll Three":
				MonopolyGame.getInstance().rollThreeDeck.drawCard();
				//MonopolyGame.getInstance().rollThree();
				break;
			case "Income Tax":
				double rate = MonopolyGame.getInstance().getCurrentPlayer().getBalance().getBalanceMoneyAmount()%10;
				if(rate < 200) {
					MonopolyGame.getInstance().getCurrentPlayer().getBalance().decreaseAmount(200);
					break;
				}else{
					MonopolyGame.getInstance().getCurrentPlayer().getBalance().decreaseAmount(rate);
					break;
				}
			case "Luxury Tax":
				MonopolyGame.getInstance().getCurrentPlayer().getBalance().decreaseAmount(75.00);
				break;
			case "Squeeze Play":
				int[] total = MonopolyGame.getInstance().getCup().rollDice(MonopolyGame.CupInputs.RR);
				int r1 = total[0];
				int r2 = total[1];
				if(r1+r2 == 5 || r1+r2 ==  9){
					for(int i=0;i<MonopolyGame.getPlayerCount();i++){
						if(i != MonopolyGame.getCurrentPlayerID()){
							MonopolyGame.getInstance().getPlayer(i+1).getBalance().decreaseAmount(50);
						}else{
							MonopolyGame.getInstance().getCurrentPlayer().getBalance().increaseAmount(50*(MonopolyGame.getPlayerCount()-1));
						}
					}
				}else if(r1+r2 == 3 || r1+r2 == 4){
					for(int i=0;i<MonopolyGame.getPlayerCount();i++){
						if(i != MonopolyGame.getCurrentPlayerID()){
							MonopolyGame.getInstance().getPlayer(i+1).getBalance().decreaseAmount(100);
						}else{
							MonopolyGame.getInstance().getCurrentPlayer().getBalance().increaseAmount(100*(MonopolyGame.getPlayerCount()-1));
						}
					}
				}else if(r1+r2 == 10 ||r1+r2 == 11){
					for(int i=0;i<MonopolyGame.getPlayerCount();i++){
						if(i != MonopolyGame.getCurrentPlayerID()){
							MonopolyGame.getInstance().getPlayer(i+1).getBalance().decreaseAmount(100);
						}else{
							MonopolyGame.getInstance().getCurrentPlayer().getBalance().increaseAmount(100*(MonopolyGame.getPlayerCount()-1));
						}
					}
				} else if(r1+r2 == 2 || r1+r2 == 12){
					for(int i=0;i<MonopolyGame.getPlayerCount();i++){
						if(i != MonopolyGame.getCurrentPlayerID()){
							MonopolyGame.getInstance().getPlayer(i+1).getBalance().decreaseAmount(200);
						}else{
							MonopolyGame.getInstance().getCurrentPlayer().getBalance().increaseAmount(200*(MonopolyGame.getPlayerCount()-1));
						}
					}
				}
				break;
			case "Bonus":
				MonopolyGame.getInstance().getCurrentPlayer().increaseBalance(300);
				break;
			case "Tax Refund":
				double amount = MonopolyGame.getInstance().getPool().getMoney().getAmount()/2;
				MonopolyGame.getInstance().getPool().decreaseAmount(amount);
				MonopolyGame.getInstance().getCurrentPlayer().increaseBalance(amount);
				break;
			case "Holland Tunnel":
				MonopolyGame.getInstance().getCurrentPlayer().getPiece().setPosition(Board.getInstance().getSquare("Holland Tunnel 2"));
				break;
			case "Holland Tunnel 2":
				MonopolyGame.getInstance().getCurrentPlayer().getPiece().setPosition(Board.getInstance().getSquare("Holland Tunnel"));
				break;
			case "Reverse Direction":
				MonopolyGame.getInstance().getCurrentPlayer().getBalance().decreaseAmount(0);
				break;
			case "Subway":
				MonopolyGame.getInstance().getCurrentPlayer().getBalance().decreaseAmount(0);
				break;
			case "Bus Ticket":
				MonopolyGame.getInstance().getCurrentPlayer().getBalance().decreaseAmount(0);
				break;
			case "Auction":
				MonopolyGame.getInstance().getCurrentPlayer().getBalance().decreaseAmount(0);
				break;
			case "Pay Day":
				MonopolyGame.getInstance().getCurrentPlayer().getBalance().decreaseAmount(0);
				break;
			case "Go To Jail":
				//MonopolyGame.getInstance().getCurrentPlayer().sendToJail();
				break;
			case "Bus Ticket 2":
				MonopolyGame.getInstance().getCurrentPlayer().getBalance().decreaseAmount(0);
				break;
			case "Birtday Gift":
				MonopolyGame.getInstance().getCurrentPlayer().getBalance().increaseAmount(100);
				break;
			case "Go":
				MonopolyGame.getInstance().getCurrentPlayer().getBalance().increaseAmount(200);
				break;
		}
	}
	
	@SuppressWarnings({ "static-access"})
	public static void doActionAsPassingFrom(Square passingPosition) {
		switch(passingPosition.getName()) {
			case "Go":
				MonopolyGame.getInstance().getCurrentPlayer().getBalance().increaseAmount(200);
				break;
			case "Bonus":
				MonopolyGame.getInstance().getCurrentPlayer().getBalance().increaseAmount(250);
				break;
		}
	}
}
