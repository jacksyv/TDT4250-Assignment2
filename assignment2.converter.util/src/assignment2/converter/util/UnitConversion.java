package assignment2.converter.util;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;

import assignment2.converter.api.ConversionInterface;
import assignment2.converter.api.ConverterResult;
import assignment2.converter.api.Unit;
import assignment2.converter.util.utils;

@Component(
		configurationPid = UnitConversion.FACTORY_PID,
		configurationPolicy = ConfigurationPolicy.REQUIRE)
public abstract class UnitConversion implements ConversionInterface{
	
	public static final String FACTORY_PID = "assignment2.converter.util.UnitConversion";
	public static final String CONV_NAME_PROP = "conversionName";
	public static final String EXPRESSION_PROP = "expression";
	
	public UnitConversion(Unit startUnit, Unit endUnit, String conversionName, String expression) {
		this.startUnit = startUnit;
		this.endUnit = endUnit;
		this.conversionName = conversionName;
		this.setExpression(expression);
	}
	

	public @interface UnitConversionConfig {
		String conversionName();
		String expression();
	}
	

	private Unit startUnit;
	private Unit endUnit;
	private String conversionName;
	
	// Expression
	private float a;
	private float b;
	private boolean isAddition;
	

	@Override
	public String getConversionName() {
		return conversionName;
	}

	@Override
	public Unit getStartUnit() {
		return startUnit;
	}

	@Override
	public Unit getEndUnit() {
		return endUnit;
	}

	@Override
	public ConverterResult convert(String startUnitSymbol, String endUnitSymbol, float value) {
		boolean success = true;
		float result = 0.0f;
		if(!startUnit.getSymbol().equals(startUnitSymbol)) {
			success = false;
		}
		else if(!endUnit.getSymbol().equals(endUnitSymbol)) {
			success = false;
		}
		if(success) {
			result = this.calculate(value);
		}
		
		return new ConverterResult(success, result, startUnitSymbol, endUnitSymbol, String.format("%.2f", value));
	}
	

	private float calculate(float value) {
		float result = 0.0f;
		result = a * value;
		if(isAddition) {
			result += b;
		}
		else {
			result -= b;
		}
		return result;
	}

	protected void setConversionName(String convName) {
		this.conversionName = convName;
	}
	
	protected boolean setExpression(String expression) {
		// expression format: [endUnit] = a * [startUnit] + b
		boolean success = true;
		String[] expParts = expression.split(" ");
		
		
		if (expParts.length != 5 && expParts.length != 7) {
			success = false;
		}
		
		if (!expParts[0].equals(endUnit.getSymbol())) {
			success = false;
		}
		
		if (!expParts[1].equals("=") ) {
			success = false;
		}
		
		if(utils.isFloat(expParts[2])) {
			this.a = utils.toFloat(expParts[2]);
		}
		else {
			success = false;
		}
		
		if (!expParts[4].equals(startUnit.getSymbol())) {
			success = false;
		}
		
		if (expParts.length == 7) {
			if (expParts[5].equals("+")) {
				this.isAddition = true;
			}
			else if (expParts[5].equals("-") ) {
				this.isAddition = false;
			}
			else {
				success = false;
			}
			
			if(utils.isFloat(expParts[6])) {
				this.b = utils.toFloat(expParts[6]);
			}
			else {
				success = false;
			}
		}
		return success;
	}

	@Override
	public void setUnits(Unit startUnit, Unit endUnit) {
		this.startUnit = startUnit;
		this.endUnit = endUnit;
	}

}
