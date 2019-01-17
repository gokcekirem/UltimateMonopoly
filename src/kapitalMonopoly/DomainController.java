package kapitalMonopoly;

import kapitalBot.BotInterface;
import kapitalMonopoly.kapitalBoard.Board;
import kapitalMonopolyObservers.*;
import kapitalMonopolyUI.PlayerPanel;
import kapitalNetwork.Action;
import kapitalNetwork.Network;

import java.util.ArrayList;

public class DomainController {

	/*
	 * Controller class methods : Facade Pattern
	 *  ---------------------------------------------------------
	 *  This facade object presents a single unified interface and is responsible for collaboration of the subsystem components.
	 *  For this we used a Singleton class called MonopolyGame with lazy initialization logic.
	 *  It is common to use synchronized to wrap the method with concurrency control in multi-threaded applications.
	 *  All the methods below return either String, int, double or an ArrayList of them. UI calling these methods have no idea about the domain logic.
	 *  UI doesn't know which data structures are used, how are things stored and managed. So even if we use another library or even different language,
	 *  the adjustments are very easy to do.
	 */
	public static void initializeMonopolyGame(int playerCount, int playerID, ArrayList<String> botNames) {
		int botCount = botNames.size();
		MonopolyGame.setPlayerCount(playerCount+botCount);
		MonopolyGame.setBotNames(botNames);
		MonopolyGame.setPlayerID(playerID);
	}

	public static ArrayList<String> getPlayerDeeds(){
		return MonopolyGame.getInstance().getOwnedDeeds();
	}

	public static double getPlayerBalance(){
		return MonopolyGame.getInstance().getPlayer(getCurrentPlayerID()).getBalance().getBalanceMoneyAmount(); //
	}

	public static ArrayList<String> getPlayerProperties(int playerID){
		return MonopolyGame.getInstance().getPlayer(playerID).getOwnedProperties();
	}

	public static ArrayList<String> getPlayerOwnedCards(){
		return MonopolyGame.getInstance().getOwnedCards();
	}

	public static boolean isPlayerInJail(int playerID){
		return MonopolyGame.getInstance().getPlayer(playerID).isInJail();
	}

	public static Player getCurrentPlayer() {
		return MonopolyGame.getInstance().getCurrentPlayer();
	}

	public int getPlayerDirection (int playerID) {
		return MonopolyGame.getInstance().getPlayer(playerID).getDirection();
	}

	public static ArrayList<int[]> getPiecePath(int playerID){
		System.out.println("get piece in   " + playerID );
		return getPlayerPiece(playerID).getMovePath();
	}

	public static void clearPiecePath(int playerID){
		getPlayerPiece(playerID).clearMovePath();
	}


	public static String buyDeed() {
		return MonopolyGame.getInstance().buyDeed();
	}

	public static String sellDeed(int playerID) {
		return MonopolyGame.getInstance().getPlayer(playerID).sellDeed();
	}

    public static String buyDeedHelper() {
        return MonopolyGame.getInstance().buyDeedHelper();
    }

    public static String sellDeedHelper(int playerID) {
        return MonopolyGame.getInstance().getPlayer(playerID).sellDeedHelper();
    }

	public static int[] rollDice() {
		return MonopolyGame.getInstance().rollDice();
	}
	public static int[] rollOneDice() {
		return MonopolyGame.getInstance().getCup().rollDice(MonopolyGame.CupInputs.R);
	}
	public static String getPlayerIcon(int playerID) {
		return MonopolyGame.getInstance().getPlayer(playerID).getPiece().getIcon(); //
	}

	public static int[] getPlayerCoordinates(int playerID) {
		return MonopolyGame.getInstance().getPlayer(playerID).getCoordinates();
	}

	public static int getCurrentPlayerID() {
		return MonopolyGame.getInstance().getCurrentPlayer().getId();
	}

	public static int[] getCurrentPlayerCoordinates() {
		return getPlayerCoordinates(MonopolyGame.getInstance().getCurrentPlayer().getId());
	}

	public static boolean isYourTurn(){
		return MonopolyGame.getInstance().isYourTurn();
	}

	public static boolean isConstructable(){
		return MonopolyGame.getInstance().isConstructable();
	}

	public static int getPlayerCount(){
	    return MonopolyGame.getPlayerCount();
    }

    public static void updateCurrentPlayer(){
	    MonopolyGame.getInstance().updateCurrentPlayer();
    }
    public static int[] getIconSize(){ //TODO
	    int[] iconsSize = {MonopolyGame.ICON_SIZE_X, MonopolyGame.ICON_SIZE_Y};
	    return iconsSize;
    }

    public static String buyProperty(String squareName, String type){
        return MonopolyGame.getInstance().getCurrentPlayer().buyProperty(squareName, type);
    }

    public static String buyPropertyHelper(String squareName, String type){
        return MonopolyGame.getInstance().getCurrentPlayer().buyPropertyHelper(squareName, type);
    }

    public static String sellProperty(String squareName, String type){
        return MonopolyGame.getInstance().getCurrentPlayer().sellProperty(squareName, type);
    }

    public static String sellPropertyHelper(String squareName, String type){
        return MonopolyGame.getInstance().getCurrentPlayer().sellPropertyHelper(squareName, type);
    }

    private static Piece getPlayerPiece(int playerID) {
        return MonopolyGame.getInstance().getPlayer(playerID).getPiece();
    }
	
	public static void getOutOfJailWithBail(int playerID) {
		MonopolyGame.getInstance().getOutOfJailWithBail(playerID-1);
	}

	public static String useSavedCard(String cardName, int playerID) {
		return MonopolyGame.getInstance().useCard(cardName, playerID);
		
	}

	public static boolean getIsPlayerInJail() {
		return MonopolyGame.getInstance().getIsPlayerInJail();
	}
	
	public static void saveGame() {
		Action.saveGame();
	}
	
	public static void loadGame() {
		Action.loadGame();
	}
	
	public static void pauseGame() {
		Action.pauseGame();
	}
	
	public static void resumeGame() {
		Action.resumeGame();
	}
	
    public static void disconnect(){
		Network.getInstance().disconnect();
	}

	public static void addMoveListener(MoveListener moveListener){
		MonopolyGame.getInstance().getCup().addMoveListener(moveListener);
	}

	public static void addRollListener(RollListener rollListener){
		MonopolyGame.getInstance().getCup().addRollListener(rollListener);
	}

	public static void addAnimationRollListener(AnimationRollListener animationRollListener){
		MonopolyGame.getInstance().getAnimation().addAnimationRollListener(animationRollListener);
	}
	public static void addJailListener(PlayerPanel jailListener) {
		MonopolyGame.getInstance().getCurrentPlayer().getPiece().addJailListener(jailListener);
	}

	public static void addDrawListener(DrawListener drawListener){
		MonopolyGame.getInstance().chanceDeck.addDrawListener(drawListener);
		MonopolyGame.getInstance().communityChestDeck.addDrawListener(drawListener);
		MonopolyGame.getInstance().rollThreeDeck.addDrawListener(drawListener);
	}

	public static void addTurnListener(TurnListener turnListener){
		MonopolyGame.getInstance().addTurnListener(turnListener);
	}

	public static void addNetworkListener(NetworkListener networkListener){
		Network.getInstance().addNetworkListener(networkListener);
	}

	public static void addDeedListener( DeedListener deedListener){
		MonopolyGame.getInstance().addDeedListener(deedListener);
	}

	public static void addConstructionListener(ConstructionListener constructionListener){
		Board.getInstance().addConstructionListener(constructionListener);
	}

	public static int[] getPropertyLocation(String squareName){ //TODO
		return Board.getInstance().getSquareMap().get(squareName).getPropertyLocations();
	}

	public static void sendMessage(String message){
		Network.getInstance().sendMessage(message);
	}

	public static int getPlayerID(){
		return MonopolyGame.getPlayerID();
	}

	public static boolean isAdmin(){
		if (MonopolyGame.getInstance().getCurrentPlayer().isBot()) {
			return ((BotInterface) MonopolyGame.getInstance().getCurrentPlayer()).isAdmin();
		}
		return false;
	}
	
	public static void botAct() {
		MonopolyGame.getInstance().botAct();
	}

	public static boolean hasDeeds(){
		return !MonopolyGame.getInstance().getCurrentPlayer().getDeeds().isEmpty();
	}

	public static void addAnimationMoveListener(AnimationMoveListener animationMoveListener){
		MonopolyGame.getInstance().getAnimation().addAnimationMoveListener(animationMoveListener);
	}

	public static void addPurchaseListener(PurchaseListener purchaseListener){
		MonopolyGame.getInstance().addPurchaseListener(purchaseListener);
	}

	public static boolean isBot(){
		return MonopolyGame.getInstance().getCurrentPlayer().isBot();
	}
}
