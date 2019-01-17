package kapitalMonopoly.kapitalBoard;

import kapitalMonopoly.kapitalBuildings.Property;
import kapitalMonopolyObservers.ConstructionListener;

import java.io.Serializable;
import java.util.ArrayList;

public class DeedSquare extends Square implements Serializable{

	private final static String CARD_TYPE = "Deed";
	
	String color; 
	Double price;
	Double rent = 0.0;
	Boolean isPurchasable;
	int[] buildingCount; // ex: 0 for hotel, buildingCount[0] means number of hotels


	static ArrayList<ConstructionListener> constructionListeners = new ArrayList<ConstructionListener>();

	public void addConstructionListener(ConstructionListener lis){ constructionListeners.add(lis); }


	public void publishConstructionEvent(String name, String value){
		for(int i=0;i<constructionListeners.size();i++){
			constructionListeners.get(i).onConstructionEvent(this, name, value);
		}
	}
	
	public DeedSquare(String name, String color, Double price, String nextSquare, String nextSquare2, String prevSquare, int[] borders) {
		super(name, nextSquare, nextSquare2, prevSquare, CARD_TYPE, borders);
		this.price = price;
		this.isPurchasable = true;
		this.color = color;
		
		buildingCount = new int[3];
		buildingCount[0]= 0;
		buildingCount[1]= 0;
		buildingCount[2]= 0;
		
	}

	public void payRent() {
		
	}

	public void updateRent(){
		double houseRent = buildingCount[0]*100;
		double hotelRent = buildingCount[1]*250;
		double skyscraperRent = buildingCount[2]*500;
		double constructionRent = houseRent + hotelRent + skyscraperRent;

		rent = price + constructionRent;
	}

	public Double getPrice(){
		return price;
	}
	
	
	public Boolean getPurchasable(){
		return isPurchasable;
	}

	public static String getCardType() {
		return CARD_TYPE;
	}

	public void changePurchasable(boolean purchase) {
		this.isPurchasable = purchase;
	}
	
	public void increaseBuildingCount(Property property) {
		if(property.getIcon().equals("House")) {
			buildingCount[0]++;
			publishConstructionEvent("Bought House" + getBuildingCount()[0], getName());
		}
		else if(property.getIcon().equals("Hotel")) {
			buildingCount[0] = 0;
			buildingCount[1]++;
			publishConstructionEvent("Bought Hotel", getName());
		}
		else if(property.getIcon().equals("Skyscraper")) {
			buildingCount[1] = 0;
			buildingCount[2]++;
			publishConstructionEvent("Bought Skyscraper", getName());
		}
	}

	public void decreaseBuildingCount() {
		
		if(buildingCount[2] > 0) {
			buildingCount[2]--;
			buildingCount[1]++;
			publishConstructionEvent("Sold Skyscraper", getName());
			return;
		}
		else if(buildingCount[1] > 0) {
			buildingCount[1]--;
			buildingCount[0] = buildingCount[0] + 4;
			publishConstructionEvent("Sold Hotel", getName());
			return;
		}
		else if(buildingCount[0] > 0) {
			buildingCount[0]--;
			publishConstructionEvent("Sold House"+ getBuildingCount()[0], getName());
			return;
		}
	}

	public String getColor(){
		return color;
	}

	public int[] getBuildingCount() { return buildingCount; }

	public void setBuildingCount(int[] buildings) {
		this.buildingCount = buildings;
	}

	public double getRent(){
		return rent;
	}

	public void setRent(double rent){
		this.rent = rent;
	}



}
