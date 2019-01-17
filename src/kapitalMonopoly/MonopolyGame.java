package kapitalMonopoly;

import kapitalBot.BotFactory;
import kapitalBot.BotInterface;
import kapitalMonopoly.kapitalBoard.DeedSquare;
import kapitalMonopoly.kapitalCards.Card;
import kapitalMonopoly.kapitalCards.DeckFactory;
import kapitalMonopoly.kapitalCards.DeckInterface;
import kapitalMonopolyObservers.DeedListener;
import kapitalMonopolyObservers.PurchaseListener;
import kapitalMonopolyObservers.TurnListener;
import kapitalNetwork.Network;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


public class MonopolyGame {

	private static int playerID;
	private static int playerCount;

	public static final int ICON_SIZE_X = 20;
	public static final int ICON_SIZE_Y = 20;
	public static final int MAX_PLAYER_COUNT = 8;
	private static Pool pool = new Pool();
	private Cup cup = new Cup();
	private static ArrayList<String> botNames = new ArrayList<>();
	private Animation animation = new Animation();

	public enum CupInputs {
		RRS, RRR, RR, R
	} 

	private static final String[] PLAYER_ICONS = { "resources/BoardPic/blackish.png", "resources/BoardPic/blue.png",
			"resources/BoardPic/dark_blue.png", "resources/BoardPic/red.png",
			"resources/BoardPic/dark_yellow.png", "resources/BoardPic/gri.png", "resources/BoardPic/nevzat.png",
	"resources/BoardPic/dark_red.png"};

	public static DeckInterface chanceDeck = DeckFactory.getDeck("Chance Deck");
	public static DeckInterface communityChestDeck = DeckFactory.getDeck("Community Chest Deck");
	public static DeckInterface rollThreeDeck = DeckFactory.getDeck("Roll Three Deck");

	private HashMap<DeedSquare, Player> deedOwners = new HashMap<>();

	private static int turn = 0;
	private static Player currentPlayer;
	public static Player[] players;

	public static ArrayList<TurnListener> turnListeners = new ArrayList<>();

	private static MonopolyGame monopolyGame;

	protected Object readResolve() {
		return monopolyGame;
	}

	public static MonopolyGame getInstance(){
		if(monopolyGame == null) {
			monopolyGame = new MonopolyGame();
			players = new Player[playerCount];
			int botCount = botNames.size();
			for(int i=0; i<playerCount-botCount; i++){
				players[i] = new Player(PLAYER_ICONS[i], i+1);
			}
			if(botCount != 0){
				for(int i=playerCount-botCount;i<playerCount;i++){
					players[i] = ((Player) BotFactory.getBot(PLAYER_ICONS[i], i+1, botNames.get(i-playerCount+botCount)));
				}
			}
			if(players.length>0){
				currentPlayer = players[0];
			}
			for(int i=0; i<players.length;i++){
				System.out.println("PLAYER : " + players[i].getId() + " isbot: " + players[i].isBot());
			}
		}
		return MonopolyGame.monopolyGame;
	}


	public void addTurnListener(TurnListener lis) {
		turnListeners.add(lis);
	}

	public void publishTurnEvent(String name, Player player){
		for(int i=0; i<turnListeners.size();i++){
			turnListeners.get(i).onTurnEvent(this, name, player.getId());
		}
	}
	// Observer ends

	public void rollThree(int r1, int r2, int r3) {

		List<Integer> rolledValues = new ArrayList<>();
		rolledValues.add(r1);
		rolledValues.add(r2);
		rolledValues.add(r3);
		Collections.sort(rolledValues);

		System.out.println("Roll Three Roll outcome: " + rolledValues.get(0) + "," + rolledValues.get(1) + "," + rolledValues.get(2) + " !");

		for(int i=0;i<players.length;i++) {
			List<Integer>  cardValues = players[i].getRollThreeCard().getDiceValues();
			System.out.print("Player " + players[i].getId() + " has roll card of " + cardValues.get(0) + "," + cardValues.get(1) + "," + cardValues.get(2) + ",");
			cardValues.retainAll(rolledValues);

			int matchCount = cardValues.size();
			System.out.print("number of matches: " + matchCount);

			int earnedAmount = 0;
			if(matchCount == 1) {
				earnedAmount = 50;
			} else if(matchCount == 2) {
				earnedAmount = 200;
			} else if(matchCount == 3 && currentPlayer.getId() == players[i].getId()) {
				earnedAmount = 1500;
			} else if(matchCount == 3) {
				earnedAmount = 1000;
			}
			System.out.println(" earned: " + earnedAmount);
			players[i].getBalance().increaseAmount(earnedAmount);
		}
	}

	public void disconnect(int playerID){
		System.out.println("PLAYER DOWN : " +  playerID);
		players[playerID-1].disconnected();
		if(playerID == currentPlayer.getId()){
			System.out.println("CURRENT PLAYER DOWN");
			updateCurrentPlayerHelper();
		}
	}

	public static void updateCurrentPlayerHelper() {
//		System.out.println("TURN BEFORE : " + turn);
		turn++;
		turn = turn % players.length;
		currentPlayer = players[turn];
//		System.out.println("TURN AFTER : " + turn);

		while(!currentPlayer.isConnected()){
			turn++;
			turn = turn % players.length;
			currentPlayer = players[turn];
		}

		MonopolyGame.getInstance().publishTurnEvent("EndTurn", currentPlayer);
//		System.out.println("Player updated!" + currentPlayer.getId());
//		System.out.println("Below must be edited");
		if(MonopolyGame.getInstance().getCurrentPlayer().isBot()){
			botAct();
		}
	}
	
	public static void botAct() {
		if(MonopolyGame.getInstance().getCurrentPlayer().isBot()){
			((BotInterface) MonopolyGame.getInstance().getCurrentPlayer()).act();
		}
	}

	public static void updateCurrentPlayer(){
		Network.getInstance().sendMessage("ENDTURN");
		updateCurrentPlayerHelper();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}	

	public HashMap<DeedSquare, Player> getDeedOwners(){
		return deedOwners;
	}

	public void setDeedOwners(DeedSquare ds){
		deedOwners.remove(ds);
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public int getTurn() {
		return turn;
	}

	public Pool getPool(){
		return pool;
	}

	public String buyDeed(int playerID) {
		return players[playerID-1].buyDeed();
	}

	public String sellDeed(int playerID) {
		return players[playerID-1].sellDeed();
	}

	public int[] rollDice() {
//		if (currentPlayer.isInJail()) {
//			return cup.rollDice(MonopolyGame.CupInputs.RR);
//		}
		return cup.rollDice(MonopolyGame.CupInputs.RRS);
	}

	public Cup getCup() {
		return cup;
	}
	
	public Animation getAnimation(){
		return animation;
	}

	public Player getPlayer(int playerID) {
		return players[playerID-1];
	}

	public void getOutOfJailWithBail(int playerID) {
		players[playerID].payJailBail();
	}

	public void saveCard(Card newCard) {
		getCurrentPlayer().saveCard(newCard);
	}

	public String useCard(String cardName, int playerID) {
		if ((cardName.contains("Get_Out_of_Jail_Free") && MonopolyGame.getInstance().getIsPlayerInJail())) {
			getPlayer(playerID).useCard(cardName);
			kapitalMonopoly.kapitalCards.ChanceDeck.putBack(cardName);
			return "used a saved card";
		}
		return "you cant use it, you are not in jail";
	}

	public boolean getIsPlayerInJail() {
		return false;
//		return getCurrentPlayer().isInJail();
	}

	public boolean isYourTurn(){
		if(currentPlayer == null){
			return false;
		}
		boolean yourTurn = currentPlayer.getId() == playerID;
//		System.out.println("yourTurn:" + yourTurn);
		return yourTurn;
	}

	public boolean isConstructable(){
		boolean yourTurn = currentPlayer.getId() == playerID;
		boolean hasDeed = !currentPlayer.getDeeds().isEmpty();
		return hasDeed && yourTurn;
	}

	public static int getPlayerCount(){
		return  playerCount;
	}

	public static int getCurrentPlayerID(){
		return currentPlayer.getId();
	}

	public static int getPlayerID(){
		return playerID;
	}

	public void addDeedListener(DeedListener deedListener){
		for(int i=0; i<playerCount;i++){
			players[i].getPiece().addDeedListener(deedListener);
		}
	}

	public void addPurchaseListener(PurchaseListener purchaseListener){
		for(int i=0; i<playerCount;i++){
			players[i].addPurchaseListener(purchaseListener);
		}
	}

	public static void setPlayerID(int playerID) {
		MonopolyGame.playerID = playerID;
	}

	public static void setPlayerCount(int playerCount) {
		MonopolyGame.playerCount = playerCount;
	}

	public static void setBotNames(ArrayList<String> botNames){
		MonopolyGame.botNames = botNames;
	}

	public void setCup(Cup cup){
		this.cup = cup;
	}

	public void setDeedOwners(HashMap<DeedSquare, Player> deedOwners){
		this.deedOwners = deedOwners;
	}

	public void setTurn(int turn){
		this.turn = turn;
	}

	public void setPool(Pool givenPool){
		 pool = givenPool;
	}

	public ArrayList<String> getOwnedCards(){
		return currentPlayer.getOwnedCards();
	}

	public ArrayList<String> getOwnedDeeds(){
		return currentPlayer.getOwnedDeeds();
	}

	public String buyDeed() {
		return  currentPlayer.buyDeed();
	}

	public String buyDeedHelper() {
		return  currentPlayer.buyDeedHelper();
	}

	public boolean isAdmin(){
		return MonopolyGame.getInstance().getCurrentPlayer().isBot() && ((BotInterface) MonopolyGame.getInstance().getCurrentPlayer()).isAdmin();
	}
}
