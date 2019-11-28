package assignment2.converter.conversions;


import assignment2.converter.api.Unit;

public class Celcius implements Unit{
	

	@Override
	public String getUnit() {
		return "Celcius as C";
	}

	@Override
	public String getSymbol() {
		return "C";
	}

}
