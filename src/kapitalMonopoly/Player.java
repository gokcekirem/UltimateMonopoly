package kapitalMonopoly;

import kapitalMonopoly.kapitalBoard.Board;
import kapitalMonopoly.kapitalBoard.DeedSquare;
import kapitalMonopoly.kapitalBoard.Square;
import kapitalMonopoly.kapitalBuildings.Hotel;
import kapitalMonopoly.kapitalBuildings.House;
import kapitalMonopoly.kapitalBuildings.Property;
import kapitalMonopoly.kapitalBuildings.Skyscraper;
import kapitalMonopoly.kapitalCards.Card;
import kapitalMonopoly.kapitalCards.RollThreeCard;
import kapitalMonopolyObservers.DeedListener;
import kapitalMonopolyObservers.PurchaseListener;
import kapitalNetwork.Network;

import java.io.Serializable;
import java.util.ArrayList;

public class Player implements DeedListener, Serializable{

	private ArrayList<DeedSquare> deeds;
	private boolean isInJail = false;
	private int direction;
	private int ID;
	private Piece piece;
	private Balance balance;
	private ArrayList<Property> properties;
	private ArrayList<Card> cardsOwned;
	private boolean isConnected = true;
	private final int bailAmount = 50;  
	private boolean isBot;
	static ArrayList<PurchaseListener> purchaseListeners = new ArrayList<>();

	//public boolean[] rollDouble = new boolean[2];
	public int inJailTurn = 0;

	public Player(String icon, int ID) {
		this.ID = ID;
		isBot = false;
		deeds = new ArrayList<>();

		isInJail = false;
		direction = 1;
		balance = new Balance();
		piece = new Piece(icon, ID);


//		this.getPiece().setPosition(Board.getSquare("Bourbon Street"));
//		this.buyDeed();
//		this.getPiece().setPosition(Board.getSquare("Magazine Street"));
//		this.buyDeed();
//		this.getPiece().setPosition(Board.getSquare("Canal Street"));
//		this.buyDeed();
//		this.getPiece().setPosition(Board.getSquare("Esplanade Avenue"));
//		this.buyDeed();

//		for (int i=0;i<2;i++) {
//			rollDouble[i] = false;
//		}

		cardsOwned=new ArrayList<>();
		cardsOwned.add(MonopolyGame.getInstance().rollThreeDeck.fDraw());
		System.out.println("Player created. ");
	}

	public Player(String icon, int ID, boolean bot) {
		this.ID = ID;
		isBot = bot;
		deeds = new ArrayList<>();
		isInJail = false;
		direction = 1;
		balance = new Balance();
		piece = new Piece(icon, ID);

//		for (int i=0;i<2;i++) {
//			rollDouble[i] = false;
//		}

		cardsOwned=new ArrayList<>();
		cardsOwned.add(MonopolyGame.getInstance().rollThreeDeck.fDraw());
		System.out.println("Player created. ");
	}

	public boolean isBot(){
		return isBot;
	}

	public void addPurchaseListener(PurchaseListener lis){
		purchaseListeners.add(lis);
	}

	public void publishPurchaseEvent(String name, String value){
		for(int i=0;i<purchaseListeners.size();i++){
			purchaseListeners.get(i).onPurchaseEvent(this, name, value);
		}
	}

	// Helper for buy/sell house
	private boolean houseDowngrade(ArrayList<DeedSquare> squares, DeedSquare unwanted) {
		ArrayList<DeedSquare> otherSquares = new ArrayList<DeedSquare>();
		for (DeedSquare sq: squares) {
			if (!sq.getName().equals(unwanted.getName()))
				otherSquares.add(sq);
		}	

		if (squares.size() == 4 && (squares.get(0).getBuildingCount()[1]>0 || 
				squares.get(1).getBuildingCount()[1]>0 ||
				squares.get(2).getBuildingCount()[1]>0)) {
			return false;
		}else if (squares.size() == 3 && (squares.get(0).getBuildingCount()[1]>0 || 
				squares.get(1).getBuildingCount()[1]>0)) {
			return false;
		}

		if (unwanted.getBuildingCount()[0]>=maxBuildingCount(otherSquares)) {
			return true;
		}


		return false;
	}
	private boolean houseApprovement(ArrayList<DeedSquare> squares, DeedSquare wanted){
		if (wanted.getBuildingCount()[0] == 4) {
			return false;
		}
		ArrayList<DeedSquare> otherSquares = new ArrayList<DeedSquare>();
		for (DeedSquare sq: squares) {
			if (!sq.getName().equals(wanted.getName()))
				otherSquares.add(sq);
		}		
		if(Board.getColorGroupPropertyNumber(squares.get(0).getColor()) == 4) {
			if (squares.size() == 4) {
				if (
						(
								(maxBuildingCount(otherSquares)== minBuildingCount(otherSquares)) 
								&& (wanted.getBuildingCount()[0] <= minBuildingCount(otherSquares))
								) 
						|| 
						(
								(maxBuildingCount(otherSquares)!= minBuildingCount(otherSquares))
								&&  (wanted.getBuildingCount()[0] >= minBuildingCount(otherSquares))
								&&  (wanted.getBuildingCount()[0] < maxBuildingCount(otherSquares))
								) 
						) {
					return true;
				}
			}else if (squares.size() == 3) {
				if ((otherSquares.get(0).getBuildingCount()[0] == otherSquares.get(1).getBuildingCount()[0]) 
						&& (wanted.getBuildingCount()[0] <= otherSquares.get(0).getBuildingCount()[0])
						|| (((otherSquares.get(0).getBuildingCount()[0] > otherSquares.get(1).getBuildingCount()[0])
								&& ( otherSquares.get(1).getBuildingCount()[0] == wanted.getBuildingCount()[0]))
								|| ((otherSquares.get(1).getBuildingCount()[0] > otherSquares.get(0).getBuildingCount()[0])
										&& ( otherSquares.get(0).getBuildingCount()[0] == wanted.getBuildingCount()[0])))){
					return true;
				}
			}
		}else if (Board.getColorGroupPropertyNumber(squares.get(0).getColor()) == 3){
			if (squares.size() == 2) {
				if((wanted.getBuildingCount()[0] <= otherSquares.get(0).getBuildingCount()[0])) {
					return true;
				}
			}else if (squares.size() == 3) {
				if (((otherSquares.get(0).getBuildingCount()[0] == otherSquares.get(1).getBuildingCount()[0]) 
						&& (wanted.getBuildingCount()[0] <= otherSquares.get(0).getBuildingCount()[0]))
						||
						((otherSquares.get(0).getBuildingCount()[0] > otherSquares.get(1).getBuildingCount()[0])
								&& (otherSquares.get(1).getBuildingCount()[0] == wanted.getBuildingCount()[0]))
						|| ((otherSquares.get(1).getBuildingCount()[0] > otherSquares.get(0).getBuildingCount()[0])
								&& (otherSquares.get(0).getBuildingCount()[0] == wanted.getBuildingCount()[0]))
						) {
					return true;
				}
			}
		}else if (Board.getColorGroupPropertyNumber(squares.get(0).getColor()) == 2){
			//if there are only 2 deeds of the same color, house or hotel cannot be bought
			return false;
		}

		return false;
	}


	// Helper for buy/sell house
	public ArrayList<DeedSquare> getColoredSquares(String color) {
		ArrayList<DeedSquare> coloredSquares = new ArrayList<>();
		for(int i=0;i<deeds.size();i++){
			if(deeds.get(i).getColor().equals(color)){
				coloredSquares.add(deeds.get(i));
			}
		}
		return coloredSquares;
	}

	// Helper for buy/sell house
	private ArrayList<DeedSquare> getOtherSquares(DeedSquare wanted, String color) {
		ArrayList<DeedSquare> otherSquares = new ArrayList<>();
		for(int i=0;i<deeds.size();i++){
			if(deeds.get(i).getColor().equals(color) && !deeds.get(i).equals(wanted)){
				otherSquares.add(deeds.get(i));
			}
		}
		return otherSquares;
	}

	public String buyHouse(String squareName){
		DeedSquare wanted = ((DeedSquare) Board.getInstance().getSquare(squareName));
		String color = wanted.getColor();
		ArrayList<DeedSquare> coloredSquares = getColoredSquares(color);
		int colorCount = coloredSquares.size();

		if(balance.getBalanceMoneyAmount() > wanted.getPrice()){
			if(houseApprovement(coloredSquares, wanted)){
				wanted.increaseBuildingCount(new House("House",100));
				wanted.updateRent();
				System.out.println("Color: "+color + ", square name: "+squareName+", house num: "+wanted.getBuildingCount()[0]);
	
				return "House successfully bought";
			}
			System.out.println("Color: "+color + ", square name: "+squareName+", house num: "+wanted.getBuildingCount()[0]);
			return "Violation of house construction rules";
		}
		System.out.println("Color: "+color + ", square name: "+squareName+", house num: "+wanted.getBuildingCount()[0]);
		return "Cannot buy house";
	}


	public String sellHouse(String squareName){
		DeedSquare wanted = ((DeedSquare)Board.getInstance().getSquare(squareName));
		String color = wanted.getColor();
		double price = wanted.getPrice();
		ArrayList<DeedSquare> coloredSquares = getColoredSquares(color);
		ArrayList<DeedSquare> otherSquares = getOtherSquares(wanted, color);
		int colorCount = coloredSquares.size();

		if(wanted.getBuildingCount()[0] > 0){
			if(houseDowngrade(coloredSquares, wanted)){
				wanted.decreaseBuildingCount();
				wanted.updateRent();
		
				return "House successfully sold";
			}
			return "Violation of house construction rules";
		}
		else {
			
			return "Cannot sell house";
		}
	}

	public String buyHotel(String squareName){
		DeedSquare wanted = ((DeedSquare)Board.getInstance().getSquare(squareName));
		String color = wanted.getColor();
		double price = wanted.getPrice();
		ArrayList<DeedSquare> coloredSquares = getColoredSquares(color);
		ArrayList<DeedSquare> otherSquares = getOtherSquares(wanted, color);
		int colorCount = coloredSquares.size();

		if(balance.getBalanceMoneyAmount() > wanted.getPrice()){
			if((colorCount == 4 && 
					(wanted.getBuildingCount()[0] ==4 && wanted.getBuildingCount()[1] ==0  )&& 
					(otherSquares.get(0).getBuildingCount()[0] == 4 || otherSquares.get(0).getBuildingCount()[1] == 1) && 
					(otherSquares.get(1).getBuildingCount()[0] == 4 || otherSquares.get(0).getBuildingCount()[1] == 1) && 
					(otherSquares.get(2).getBuildingCount()[0] == 4 || otherSquares.get(0).getBuildingCount()[1] == 1) )
					|| 
					(colorCount == 3 && 
					(wanted.getBuildingCount()[0] ==4 && wanted.getBuildingCount()[1]==0)&& 
					(otherSquares.get(0).getBuildingCount()[0] == 4 || otherSquares.get(0).getBuildingCount()[1] == 1) && 
					(otherSquares.get(1).getBuildingCount()[0] == 4 || otherSquares.get(1).getBuildingCount()[1] == 1))
					)
			{
				wanted.increaseBuildingCount(new Hotel("Hotel",200));
				wanted.updateRent();
				
				return "Hotel successfully bought";
			}
			return "Need 4 houses on each same color square";
		}
		return "Cannot buy hotel";
	}

	public String sellHotel(String squareName) {
		DeedSquare wanted = ((DeedSquare)Board.getInstance().getSquare(squareName));
		String color = wanted.getColor();
		double price = wanted.getPrice();
		ArrayList<DeedSquare> coloredSquares = getColoredSquares(color);
		ArrayList<DeedSquare> otherSquares = getOtherSquares(wanted, color);
		int colorCount = coloredSquares.size();

		if(colorCount == 3 || colorCount == 4){
			if(wanted.getBuildingCount()[1] == 1){
				wanted.decreaseBuildingCount();
				wanted.updateRent();
		
				return "Hotel successfully sold";
			}
			return "Need a hotel to sell";
		}
		return "Cannot sell hotel";
	}

	public String buySkyscraper(String squareName){
		DeedSquare wanted = ((DeedSquare)Board.getInstance().getSquare(squareName));
		String color = wanted.getColor();
		double price = wanted.getPrice();
		ArrayList<DeedSquare> coloredSquares = getColoredSquares(color);
		ArrayList<DeedSquare> otherSquares = getOtherSquares(wanted, color);
		int colorCount = coloredSquares.size();

		if(balance.getBalanceMoneyAmount() > wanted.getPrice()){
			if((colorCount == 3 && 
					Board.getColorGroupPropertyNumber(wanted.getColor()) == 3 && 
					wanted.getBuildingCount()[1] == 1 && 
					(otherSquares.get(0).getBuildingCount()[1] == 1 || otherSquares.get(0).getBuildingCount()[2] == 1 ) && 
					(otherSquares.get(1).getBuildingCount()[1] == 1 || otherSquares.get(1).getBuildingCount()[2] == 1))
					|| (colorCount == 4 &&
					Board.getColorGroupPropertyNumber(wanted.getColor()) == 4 && 
					wanted.getBuildingCount()[1] == 1 && 
					(otherSquares.get(0).getBuildingCount()[1] == 1 || otherSquares.get(0).getBuildingCount()[2] == 1) && 
					(otherSquares.get(1).getBuildingCount()[1] == 1 || otherSquares.get(1).getBuildingCount()[2] == 1)&&
					(otherSquares.get(2).getBuildingCount()[1] == 1 || otherSquares.get(2).getBuildingCount()[2] == 1))){
				wanted.increaseBuildingCount(new Skyscraper("Skyscraper",300));
				wanted.updateRent();
				
				return "Skyscraper successfully bought";
			}
			return "Need 1 hotel on each same color square";
		}
		return "Cannot buy skyscraper";
	}

	public String sellSkyscraper(String squareName){
		DeedSquare wanted = ((DeedSquare)Board.getInstance().getSquare(squareName));
		String color = wanted.getColor();
		double price = wanted.getPrice();
		ArrayList<DeedSquare> coloredSquares = getColoredSquares(color);
		ArrayList<DeedSquare> otherSquares = getOtherSquares(wanted, color);
		int colorCount = coloredSquares.size();

		if(colorCount == 3 || colorCount == 4){
			if(wanted.getBuildingCount()[2] == 1){
				wanted.decreaseBuildingCount();
				wanted.updateRent();

				return "Skyscraper successfully sold";
			}
			return "Need a skyscraper to sell";
		}
		return "Cannot sell skyscraper";
	}


	public String buyProperty(String squareName, String type) { // must return string
		Network.getInstance().sendMessage("BUYPROPERTY" + type);
		return buyPropertyHelper(squareName, type);
	}

	public String buyPropertyHelper(String squareName, String type) { // must return string
		if(type.equals("House"))
			return buyHouse(squareName);
		else if(type.equals("Hotel"))
			return buyHotel(squareName);
		else
			return buySkyscraper(squareName);
	}

	public String sellProperty(String squareName, String type) { //must return string
		Network.getInstance().sendMessage("SELLPROPERTY" + type);
		return sellPropertyHelper(squareName, type);
	}

	public String sellPropertyHelper(String squareName, String type) { //must return string
		if(type.equals("House"))
			return sellHouse(squareName);
		else if(type.equals("Hotel"))
			return sellHotel(squareName);
		else
			return sellSkyscraper(squareName);
	}


	public String buyDeed() {  //must return string
		Network.getInstance().sendMessage("BUYDEED");
		return buyDeedHelper();
	}

	public String buyDeedHelper(){
		System.out.println("Applied for purchase of:" + piece.getPosition().type + ", is it a deed:" + piece.getPosition());
		if(piece.getPosition().isDeedSquare()) {
			DeedSquare deedSquare = (DeedSquare) piece.getPosition();
			if(!MonopolyGame.getInstance().getDeedOwners().containsKey(deedSquare) && balance.getBalanceMoneyAmount() >= deedSquare.getPrice()) {
				balance.decreaseAmount(deedSquare.getPrice());
				MonopolyGame.getInstance().getDeedOwners().put(deedSquare, this);
				deeds.add(deedSquare);
				String value = "Deed Successfully bought";
				publishPurchaseEvent("Bought", value);
				return value;
			}else {
				return "This square cannot be bought";
			}
		}
		return  "Cannot be bought, not even a Deed Square";
	}

	public String sellDeed(){
		Network.getInstance().sendMessage("SELLDEED");
		return sellDeedHelper();
	}

	public String sellDeedHelper() {  //must return string
		if(piece.getPosition().isDeedSquare()) {
			DeedSquare deedSquare = (DeedSquare) piece.getPosition();
			if(deeds.contains(deedSquare)) {
				deeds.remove(deedSquare);
				balance.increaseAmount(deedSquare.getPrice());
				MonopolyGame.getInstance().setDeedOwners(deedSquare);
				String value = "Successfully sold";
				publishPurchaseEvent("Sold", value);
				return value;
			}else
				return "Deed could not be sold";
		}
		return "Cannot be sold, not even a Deed Square";
	}

	public void mortgageDeed(DeedSquare square) { //must return string but will be implemented later.
		if(deeds.contains(square) && isInJail==false) {
			balance.increaseAmount(square.getPrice()/2);
		}else
			System.out.println("You can't mortgage that");
	}

	public Piece getPiece() {
		return piece;
	}

	public int getDirection() {
		return direction;
	}

	public Balance getBalance() {
		return balance;
	}

	public boolean isInJail() {
		return isInJail;
	}

	public void setFreeFromJail() {
		isInJail = false;
	}

	public void payJailBail() {
		if (balance.getBalanceMoney().getAmount() >= bailAmount) {
			balance.decreaseAmount(bailAmount);
			setFreeFromJail();
			Network.getInstance().sendMessage("PAYJAILBAIL" + bailAmount);
		}else {
			System.out.println("You dont have enough money");
			// check the rules
		}
	}

	public String getPositionName() {
		return piece.getPositionName();
	}

	public Square getPosition() {
		return piece.getPosition();
	}

	public int getId() {
		return ID;
	}

	public ArrayList<Property> getProperties() {
		return properties;
	}

	public ArrayList<DeedSquare> getDeeds() {
		return deeds;
	}

	public ArrayList<Card> getCards(){
		return cardsOwned;
	}

	public void setProperties(Property newProperty) {
		properties.add(newProperty);
	}

	public void setDeeds(DeedSquare newDeed) {
		deeds.add(newDeed);
	}


	public void increaseBalance(double amount){
		balance.increaseAmount(amount);
	}


	public RollThreeCard getRollThreeCard() {
		for(int i=cardsOwned.size(); i>0; i--) {
			if(cardsOwned.get(i-1).isRollThreeCard()) {
				return (RollThreeCard) cardsOwned.get(i-1);
			}
		}
		return null;
	}

	public int[] getCoordinates() {
		int[] coordinates = new int[2];
		coordinates[0] = piece.getX();
		coordinates[1] = piece.getY();
		return coordinates;
	}

	public ArrayList<String> getOwnedCards(){
		ArrayList<String> cardNames = new ArrayList<>();
		for(int i=0; i<cardsOwned.size(); i++) {
			cardNames.add(cardsOwned.get(i).toString());
		}
		return cardNames;
	}

	public ArrayList<String> getOwnedProperties(){
		ArrayList<String> p = new ArrayList<>();
		for(int i=0; i<properties.size(); i++) {
			p.add(properties.get(i).toString());
		}
		return p;
	}
	
	public ArrayList<String> getCardsOwned(){
		ArrayList<String> c = new ArrayList<>();
		for(int i=0; i<cardsOwned.size(); i++) {
			c.add(cardsOwned.get(i).toString());
		}
		return c;
	}


	public ArrayList<String> getOwnedDeeds(){
		ArrayList<String> deedNames = new ArrayList<>();
		for(int i=0; i<deeds.size(); i++) {
			deedNames.add(deeds.get(i).toString());
		}
		return deedNames;
	}

	public void disconnected(){
		isConnected = false;
	}

	public boolean isConnected(){
		return isConnected;
	}
	@Override
	public void onDeedEvent(Object source, String name, String value) {
		// TODO Auto-generated method stub

	}

	public void saveCard(Card newCard){
		cardsOwned.add(newCard);
	}

	public void useCard(String newCard){
		if(newCard.contains("Get_Out_of_Jail")) {
			setFreeFromJail();
		}
		Card remove = null;
		for (Card c : cardsOwned) {
			if(c.toString().contains("Get_Out_of_Jail")){
				remove = c;
			}
		}
		System.out.println(cardsOwned.size()+" elements, one removed: "+remove.toString());
		cardsOwned.remove(remove);
		System.out.println(cardsOwned.size()+" elements");

	}

	public void sendToJail() {
		isInJail = true;
		piece.sendToJail();
		// TODO Auto-generated method stub

	}

	public void setPiece(Piece piece){
		this.piece = piece;
	}
	public void setDirection(int i) {
		direction = 0;
	}

	public int maxBuildingCount(ArrayList<DeedSquare> squares) {
		int max = 0;
		for (DeedSquare sq: squares) {
			if (max < sq.getBuildingCount()[0])
				max = sq.getBuildingCount()[0];
		}
		return max;
	}

	public int minBuildingCount(ArrayList<DeedSquare> squares) {
		int min = 10;
		for (DeedSquare sq: squares) {
			if (min >= sq.getBuildingCount()[0])
				min = sq.getBuildingCount()[0];
		}
		return min;
	}

	public void setIsBot(){
		this.isBot = false;
	}
}
