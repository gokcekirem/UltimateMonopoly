package kapitalMonopoly.kapitalBuildings;

import java.io.Serializable;

public class Skyscraper extends Property  implements PropertyInterface, Serializable {

    public Skyscraper(String icon, double price){
        super(icon,price);
    }


}
