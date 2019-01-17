package kapitalMonopoly.kapitalBuildings;

import java.io.Serializable;

public class House extends Property implements PropertyInterface, Serializable{
    double price;
    String icon;

    public House(String icon, double price){
        super(icon,price);
    }
	
}
