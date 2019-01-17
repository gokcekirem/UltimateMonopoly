package kapitalMonopoly.kapitalCards;

import kapitalMonopoly.MonopolyGame;
import kapitalMonopoly.Piece;
import kapitalMonopoly.Player;
import kapitalMonopoly.kapitalBoard.Board;
import kapitalMonopoly.kapitalBoard.DeedSquare;
import kapitalMonopoly.kapitalBoard.Square;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class ChanceCard extends Card implements Serializable{
	
	private static final String CARD_FILES_PATH  = "resources/ChanceCards/";
	private static final String CARD_FILE_EXTENSION  = ".jpg";
	
	private int cardNumber;
	
	public int getCardNumber() {
		return cardNumber;
	}
	public ChanceCard(int cardNumber, String cardName) {
		super(CARD_FILES_PATH + cardName + CARD_FILE_EXTENSION);
		this.cardNumber = cardNumber;
	}
	
	public static String getChanceCardPath() {
		return CARD_FILES_PATH;
	}

	@SuppressWarnings("static-access")
	@Override
	public void doAction() {
		switch(cardNumber) {
		case 0: // Holiday Bonus
			MonopolyGame.getInstance().getCurrentPlayer().getBalance().increaseAmount(100);
//			System.out.println(MonopolyGame.getInstance().getCurrentPlayer().getBalance().toString());
			MonopolyGame.getInstance().getCurrentPlayer().publishPurchaseEvent("","Holiday Bonus: earned 100");
			break;
		case 1: //Forward Thinker
			//String value = "210";
			Piece piece = MonopolyGame.getInstance().getCurrentPlayer().getPiece();
			for(int i=0;i<3;i++){
				piece.addToMovePath(piece.getTemp().getCoordinates(piece.getPlayerID()));
				piece.setTemp(Board.getInstance().getNextSquare(MonopolyGame.getInstance().getCurrentPlayer().getPiece().getTemp()));
			}
			Square tempSquare = piece.getPosition();
			for(int i=0;i<3;i++){
				tempSquare = Board.getInstance().getNextSquare(tempSquare);
			}
			MonopolyGame.getInstance().getCurrentPlayer().getPiece().setPosition(tempSquare);
			
		case 2: // Hurricane makes land fall
			Random rand = new Random();
			int playerId = rand.nextInt(MonopolyGame.getPlayerCount())+1;
			Player selectedPlayer = MonopolyGame.getInstance().getPlayer(playerId);

			DeedSquare selectedDeed;
			if (selectedPlayer.getDeeds().size()!=0) {
				int deedId = rand.nextInt(selectedPlayer.getDeeds().size());
				selectedDeed = selectedPlayer.getDeeds().get(deedId);
				ArrayList<DeedSquare> coloredSquares = selectedPlayer.getColoredSquares(selectedDeed.getColor());
				for (DeedSquare sq:coloredSquares) {
					sq.decreaseBuildingCount();
					System.out.println("\nPlayer "+playerId+" got hurricaned at "+sq.getName());
				}
			}else{
				System.out.println("\nPlayer "+playerId+" got lucky, no deeds found.");
			}

			break;
		case 3: // Get out of Jail Free
//			System.out.println("get out of jail");
			break;
		case 4: // Go to Jail
//			System.out.println("go to jail");
//			MonopolyGame.getInstance().getCurrentPlayer().sendToJail();
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
		case 11:
			break;	
		case 12:
			break;	
		case 13:
			break;
		case 14:
			break;	
		case 15:
			break;	
		case 16:
			break;
		case 17:
			break;	
		case 18:
			break;	
		case 19:
			break;	
		case 20:
			break;	
		case 21:
			break;	
		case 22:
			break;	
		default:
			break;
		}
	}


}
