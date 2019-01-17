package kapitalMonopoly.kapitalBuildings;

import java.io.Serializable;

public class Hotel extends Property implements PropertyInterface, Serializable{

    public Hotel(String icon, double price){
        super(icon,price);
    }


}
