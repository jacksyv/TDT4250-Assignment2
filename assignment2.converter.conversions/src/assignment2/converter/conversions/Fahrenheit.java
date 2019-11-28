package assignment2.converter.conversions;

import assignment2.converter.api.Unit;

public class Fahrenheit implements Unit{

	@Override
	public String getUnit() {
		return "Fahrenheit as F";
	}

	@Override
	public String getSymbol() {
		return "F";
	}

}
