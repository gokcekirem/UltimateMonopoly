package kapitalMonopoly.kapitalBuildings;

import java.io.Serializable;

public class Property implements Serializable{
	private double price;
	private String icon;
	
	public Property(String icon, double price) {
		this.icon = icon;
		this.price = price;
	}
	
	public double getPrice(){
		return price;
	}
	
	public String getIcon(){
		return icon;
	}
}
