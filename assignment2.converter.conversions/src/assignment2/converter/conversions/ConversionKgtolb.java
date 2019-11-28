package assignment2.converter.conversions;

import org.osgi.service.component.annotations.Component;

import assignment2.converter.api.ConversionInterface;
import assignment2.converter.util.UnitConversion;


@Component(
		property = { UnitConversion.CONV_NAME_PROP + "=Kglb" } 
		)
public class ConversionKgtolb extends UnitConversion implements ConversionInterface{

	public ConversionKgtolb() {
		super(new Kilo(), new Pound(), "Kglb", "lb = 0.45359237 * Kg");
	}
}