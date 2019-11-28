package assignment2.converter.api;


import org.osgi.annotation.versioning.ConsumerType;

@ConsumerType
public class ConverterResult {

	private final boolean success;
	private final String message;
	private static final String DEFAULT_MESSAGE = "Input is invalid, make sure the command exists";
	
	public ConverterResult(boolean success, float value, String startUnit, String endUnit, String startValue) {
		super();
		this.success = success;
		
		if(success) {
			this.message = startValue + " " + startUnit + " = " + String.format("%.2f", value) + " " + endUnit;
		}
		else {
			this.message = DEFAULT_MESSAGE;
		}	
	}
	
	public boolean isSuccess() {
		return success;
	}
	
	public String getMessage() {
		return message;
	}
	

}
