package assignment2.converter.util;

public class utils {

	public static boolean isFloat(String value) {
		try{
			float f = Float.parseFloat(value);
		}
		catch(Exception e) {
			return false;
		}
		return true;
	}
	
	public static Float toFloat(String value) {
		try{
			return Float.parseFloat(value);
		}
		catch(Exception e) {
			return 0.0f;
		}
	}

	
}
