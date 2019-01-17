package kapitalNetwork;

import kapitalBot.Bot;
import kapitalBot.BotFactory;
import kapitalMonopoly.MonopolyGame;
import kapitalMonopoly.Piece;
import kapitalMonopoly.Player;
import kapitalMonopoly.Pool;
import kapitalMonopoly.kapitalBoard.Board;
import kapitalMonopoly.kapitalBoard.DeedSquare;
import kapitalMonopoly.kapitalBoard.Square;
import kapitalMonopoly.kapitalCards.DeckInterface;

import java.io.*;
import java.util.HashMap;

public class Action implements Serializable{

	private static String filename = "resources/savedGame.ser";

	public static void saveGame(){
		Network.getInstance().sendMessage("SAVEGAME");
		saveGameHelper();
	}

	public static void saveGameHelper(){
		Savable monopoly = new Savable();

//		System.out.println("Saving game with count: " + monopoly.game.playerCount + ", id: " + monopoly.game.playerID);

		try {
			FileOutputStream file = new FileOutputStream(filename);
			ObjectOutputStream out = new ObjectOutputStream(file);

			out.writeObject(monopoly);

			out.close();
			file.close();

			System.out.println("Game has been serialized");
		}
		catch(IOException e) {
			System.out.println(e.toString());
		}
	}

	public static void loadGame() {

		Savable monopoly;

		try
		{
			FileInputStream file = new FileInputStream(filename);
			ObjectInputStream in = new ObjectInputStream(file);

			monopoly = (Savable) in.readObject();

			in.close();
			file.close();

			System.out.println("Game has been deserialized ");
//			System.out.println("Loading game with count: " + monopoly.game.playerCount + ", id: " + monopoly.game.playerID);
			monopoly.setGame();
//			System.out.println("LOADED COUNT: " + MonopolyGame.getPlayerCount() + ", ID: " + MonopolyGame.getPlayerID());

		}

		catch(IOException ex)
		{
			System.out.println("IOException is caught");
		}

		catch(ClassNotFoundException ex) {
			System.out.println("ClassNotFoundException is caught");
		}
	}

	public static void pauseGame() {
		Network.getInstance().sendMessage("PAUSE");
	}

	public static void resumeGame() {
		Network.getInstance().sendMessage("RESUME");
	}


	private static class Savable implements Serializable{
		private SavableGame game;
		private SavableBoard board;

		private Savable(){
			this.game = new SavableGame();
			this.board = new SavableBoard();
		}

		private void setGame(){
			board.setBoard();
			game.setMonopolyGame();
		}
	}

	private static class SavableGame implements Serializable{

		private int playerID;
		private int playerCount;

		public DeckInterface chanceDeck;
		public DeckInterface communityChestDeck;
		public DeckInterface rollThreeDeck;
		public Pool pool;
		private HashMap<DeedSquare, Player> deedOwners;
		private Player[] players;
		private boolean[] isBot;
		private String[] botTypes;
		private SavablePiece[] pieces;
		private int turn;

		public SavableGame(){
			this.playerID = MonopolyGame.getPlayerID();
			this.playerCount = MonopolyGame.getPlayerCount();
			this.pieces = new SavablePiece[playerCount];
			this.chanceDeck = MonopolyGame.getInstance().chanceDeck;
			this.communityChestDeck = MonopolyGame.getInstance().communityChestDeck;
			this.rollThreeDeck = MonopolyGame.getInstance().rollThreeDeck;
			this.pool = MonopolyGame.getInstance().getPool();
			this.deedOwners = MonopolyGame.getInstance().getDeedOwners();
			this.players = MonopolyGame.players;
			isBot = new boolean[players.length];
			botTypes = new String[players.length];
			for(int i=0; i<players.length;i++){
				Piece temp = players[i].getPiece();
				pieces[i] = new SavablePiece(temp);
				isBot[i] = players[i].isBot();
				if(isBot[i]){
					botTypes[i] = ((Bot) players[i]).getBotType();
				}
			}
			this.turn = MonopolyGame.getCurrentPlayerID()-1;
		}

		public void setMonopolyGame(){
			MonopolyGame.setPlayerID(playerID);
			MonopolyGame.setPlayerCount(playerCount);
			MonopolyGame.getInstance().chanceDeck = chanceDeck;
			MonopolyGame.getInstance().communityChestDeck = communityChestDeck;
			MonopolyGame.getInstance().rollThreeDeck = rollThreeDeck;
			MonopolyGame.getInstance().setPool(pool);
			MonopolyGame.getInstance().setDeedOwners(deedOwners);
			for(int i=0; i<players.length;i++){
				SavablePiece tempPiece = pieces[i];
				Piece createdPiece = new Piece(tempPiece.icon, tempPiece.playerID);
				createdPiece.setPosition(tempPiece.position);
				if(isBot[i]){
					players[i] = ((Player) BotFactory.getBot(players[i].getPiece().getIcon(), i+1, botTypes[i]));
				}
				players[i].setPiece(createdPiece);
			}
			MonopolyGame.players = players;
			MonopolyGame.getInstance().setTurn(turn);
			Network.getInstance().setGameLoaded();
		}
	}

	private static class SavableBoard implements Serializable{

		private HashMap<String,SavableDeedSquare> savedSquareMap = new HashMap<>();

		public SavableBoard(){
			HashMap<String,Square> squareMap = Board.getInstance().getSquareMap();
			for(String key : squareMap.keySet()){
				Square temp = squareMap.get(key);
				if(temp.isDeedSquare()){
					SavableDeedSquare temp2 = new SavableDeedSquare((DeedSquare) temp);
					savedSquareMap.put(key, temp2);
				}
			}
		}

		public void setBoard(){
			for(String key : savedSquareMap.keySet()){
				SavableDeedSquare temp = savedSquareMap.get(key);
				((DeedSquare) Board.getInstance().getSquareMap().get(key)).setRent(temp.rent);
				int[] buildings = temp.buildingCount;
				String property = "Bought";
				if(buildings[2] == 1){
					property += "Skyscraper";
				} else if(buildings[1] == 1){
					property += "Hotel";
				} else{
					property += "House" + buildings[0];
				}
				((DeedSquare) Board.getInstance().getSquareMap().get(key)).publishConstructionEvent(property,key);
				((DeedSquare) Board.getInstance().getSquareMap().get(key)).setBuildingCount(temp.buildingCount);
				((DeedSquare) Board.getInstance().getSquareMap().get(key)).changePurchasable(temp.isPurchasable);
			}
		}
	}

	private static class SavablePiece implements Serializable{

		private String icon;
		private Square position;
		private int playerID;

		public SavablePiece(Piece piece){
			this.icon = piece.getIcon();
			this.position = piece.getPosition();
			this.playerID = piece.getPlayerID();
		}

	}

	private static class SavableDeedSquare implements Serializable{

		Double rent;
		Boolean isPurchasable;
		int[] buildingCount;

		public SavableDeedSquare(DeedSquare square){
			this.rent = square.getRent();
			this.isPurchasable = square.getPurchasable();
			this.buildingCount = square.getBuildingCount();
		}
	}

}
