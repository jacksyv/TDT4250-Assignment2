package assignment2.converter.conversions;

import org.osgi.service.component.annotations.Component;

import assignment2.converter.api.ConversionInterface;
import assignment2.converter.util.UnitConversion;


@Component(
		property = { UnitConversion.CONV_NAME_PROP + "=CF" } 
		)
public class ConversionCtoF extends UnitConversion implements ConversionInterface{

	public ConversionCtoF() {
		super(new Celcius(), new Fahrenheit(), "CF", "F = 1.8 * C + 32");
	}
}
