package kapitalMonopoly.kapitalCards;

import kapitalMonopoly.MonopolyGame;
import kapitalMonopoly.kapitalBoard.Board;

import java.io.Serializable;

public class CommunityChestCard extends Card implements Serializable{

	private static final String CARD_FILES_PATH  = "resources/CommunityChestCards/";
	private static final String CARD_FILE_EXTENSION  = ".png";
	
	private int cardNumber;
	
	public CommunityChestCard(int cardNumber, String cardPath) {
		super(CARD_FILES_PATH + cardPath +  CARD_FILE_EXTENSION);
		this.cardNumber = cardNumber;
	}
	
	public static String getCommunityChestPath() {
		return CARD_FILES_PATH;
	}

	@SuppressWarnings("static-access")
	@Override
	public void doAction() {
		// TODO Auto-generated method stub
		switch(cardNumber) {
		case 0:
			MonopolyGame.getInstance().getCurrentPlayer().getBalance().decreaseAmount(100);
			MonopolyGame.getInstance().getPool().increaseAmount(100);
			MonopolyGame.getInstance().getCurrentPlayer().publishPurchaseEvent("","Pay Hospital Bills: paid 100 to pool");
//			System.out.println(MonopolyGame.getInstance().getCurrentPlayer().getBalance().toString());
			break;
		case 1:
//			System.out.print("Before BALANCE : " + MonopolyGame.getInstance().getCurrentPlayer().getBalance().getBalanceMoney().getAmount());
			String trackType = Board.getInstance().getTrackType(MonopolyGame.getInstance().getCurrentPlayer().getPosition());
			if(trackType.equalsIgnoreCase("InnerTrack")){
				MonopolyGame.getInstance().getCurrentPlayer().getBalance().increaseAmount(250);
				MonopolyGame.getInstance().getCurrentPlayer().publishPurchaseEvent("","Insider's Edge: InnerTrack, earned 250");
			} else if(trackType.equalsIgnoreCase("OuterTrack")){
				MonopolyGame.getInstance().getCurrentPlayer().getBalance().decreaseAmount(50);
				MonopolyGame.getInstance().getPool().increaseAmount(50);
				MonopolyGame.getInstance().getCurrentPlayer().publishPurchaseEvent("","Insider's Edge: OuterTrack, paid 50 to Pool");
			} else{
				MonopolyGame.getInstance().getCurrentPlayer().publishPurchaseEvent("","Insider's Edge: CenterTrack, nothing...");
			}
//			System.out.println("After BALANCE : " + MonopolyGame.getInstance().getCurrentPlayer().getBalance().getBalanceMoney().getAmount());
			break;	
		case 2:
			break;
		case 3:
			break;	
		case 4:
			break;
		case 5:
			break;	
		case 6:
			break;
		case 7:
			break;	
		case 8:
			break;
		case 9:
			break;	
		case 10:
			break;
		default:
			break;
		}
	}
}
