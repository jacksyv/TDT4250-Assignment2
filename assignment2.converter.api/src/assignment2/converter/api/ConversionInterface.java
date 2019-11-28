package assignment2.converter.api;

import org.osgi.annotation.versioning.ProviderType;

import assignment2.converter.api.ConverterResult;
import assignment2.converter.api.Unit;

@ProviderType
public interface ConversionInterface {
	String getConversionName();
	Unit getStartUnit();
	Unit getEndUnit();
	void setUnits(Unit startUnit, Unit endUnit) throws Exception;
	ConverterResult convert(String startUnitSymbol, String endUnitSymbol, float value);
	
}
