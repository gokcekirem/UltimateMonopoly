package kapitalMonopoly.kapitalBuildings;

import java.io.Serializable;

public class PropertyFactory implements Serializable{
	public static PropertyInterface getProperty(String property)
	{
		if (property.equals("House"))
			return new House("House",100);
		else if (property.equals("Hotel"))
			return new Hotel("Hotel",100);
		else if (property.equals("Skyscraper"))
			return new Skyscraper("Skyscraper",100);
		return null;
	}
}
