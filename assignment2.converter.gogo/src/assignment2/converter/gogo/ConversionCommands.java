package assignment2.converter.gogo;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;

import org.apache.felix.service.command.Descriptor;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;

import assignment2.converter.api.ConversionInterface;
import assignment2.converter.api.Converter;
import assignment2.converter.api.ConverterResult;
import assignment2.converter.api.Unit;
import assignment2.converter.util.UnitConversion;

@Component(
		service = ConversionCommands.class,
		property = {
				"osgi.command.scope=conversion",
				"osgi.command.function=list",
				"osgi.command.function=convert",
				"osgi.command.function=add",
		})
public class ConversionCommands {

	private Configuration getConfig(String convName) {
		try {
			Configuration[] configs = configuration_admin.listConfigurations("(&(" + UnitConversion.CONV_NAME_PROP + "=" + convName + ")(service.factoryPid=" + UnitConversion.FACTORY_PID + "))");
			if (configs != null && configs.length >= 1) {
				return configs[0];
			}
		} catch (IOException | InvalidSyntaxException e) {
		}
		return null;
	}
	
	@Descriptor("list available conversions")
	public void list() {
		System.out.println("Adding new conversion: ");
		System.out.println("Enter add Kg lb \"lb = 0.45359237 * Kg\" or other conversions you find intresting");
		System.out.println("Conversions: ");
		BundleContext bundle_context = FrameworkUtil.getBundle(this.getClass()).getBundleContext();
		try {
			System.out.println("Enter conversions as \"convert Unit Unit Value\", e.g. convert Kg lb 50");
			System.out.println("Conversions are case-sensitive");
			for (ServiceReference<ConversionInterface> serviceReference : bundle_context.getServiceReferences(ConversionInterface.class, null)) {
				ConversionInterface conversion = bundle_context.getService(serviceReference);
				try {
					if (conversion != null) {
						String startUnitName = conversion.getStartUnit().getUnit();
						String endUnitName = conversion.getEndUnit().getUnit();
						String conversionName = startUnitName + endUnitName;
						System.out.print("From " + startUnitName + " to " + endUnitName);
						if (getConfig(conversionName) != null) {
							System.out.print("*");						
						}
					}
				} finally {
					bundle_context.ungetService(serviceReference);
				}
				System.out.println();
			}
		} catch (InvalidSyntaxException e) {
		}
		System.out.println();
	}
	
	@Descriptor("convert a value from an unit to another")
	public void convert(
			@Descriptor("unit to convert from")
			String startUnitSymbol,
			@Descriptor("unit to convert to")
			String endUnitSymbol,
			@Descriptor("value")
			float value
			) {
		BundleContext bundle_context = FrameworkUtil.getBundle(this.getClass()).getBundleContext();
		
		ConverterResult converterResult = null;
		try {
			for (ServiceReference<ConversionInterface> serviceReference : bundle_context.getServiceReferences(ConversionInterface.class, null)) {
				ConversionInterface conversion = bundle_context.getService(serviceReference);
				if (conversion != null) {
					try {
						if( startUnitSymbol.equals(conversion.getStartUnit().getSymbol())
								&& endUnitSymbol.equals(conversion.getEndUnit().getSymbol())) {
							converterResult = conversion.convert(startUnitSymbol, endUnitSymbol, value);
							
						}
						
					} finally {
						bundle_context.ungetService(serviceReference);
					}
				} 
			}
		} catch (InvalidSyntaxException e) {
		}
		if(converterResult == null) {
			boolean success = false;
			converterResult = new ConverterResult(success, value, "", "", "");
		}
		System.out.println(converterResult.getMessage());
	}
	
	@Reference(cardinality = ReferenceCardinality.MANDATORY)
	private ConfigurationAdmin configuration_admin;

	@Descriptor("add a conversion")
	public void add(
			@Descriptor("the name of start unit")
			String startUnitSymbol,
			@Descriptor("the name of the end unit")
			String endUnitSymbol,
			@Descriptor("The conversion expression. Format: [endUnit] = a * [startUnit] + b")
			String expression
			) throws Exception {
		
		BundleContext bc = FrameworkUtil.getBundle(this.getClass()).getBundleContext();
		Unit startUnit = Converter.getUnit(startUnitSymbol);
		Unit endUnit = Converter.getUnit(endUnitSymbol);
		
		if( startUnit == null || endUnit == null) {
			throw new Exception("Unit name does not exists");
			
		}
		
		
		String conversionName = startUnitSymbol + endUnitSymbol;
		Configuration config = getConfig(conversionName);
		if (config == null) {
			
			config = configuration_admin.createFactoryConfiguration(UnitConversion.FACTORY_PID, "?");
		}
		
		Dictionary<String, String> props = new Hashtable<>();
		props.put(UnitConversion.CONV_NAME_PROP, conversionName);
		if (expression != null) {
			props.put(UnitConversion.EXPRESSION_PROP, expression);
		}
		ConversionInterface conversion = new UnitConversion(startUnit, endUnit, conversionName, expression) {
		};
		Converter.addConversion(conversion);
		
		config.update(props);
		bc.registerService(ConversionInterface.class, conversion, props);
		System.out.println("Conversion from " + startUnitSymbol + " to " + endUnitSymbol + " has been added.");
	}
	
}