package assignment2.converter.conversions;

import assignment2.converter.api.Unit;

public class Pound implements Unit{

	@Override
	public String getUnit() {
		return "Pound as lb";
	}

	@Override
	public String getSymbol() {
		return "lb";
	}
	
}
