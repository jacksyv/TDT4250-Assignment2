package assignment2.converter.api;

import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public interface Unit {
	
	String getUnit();
	String getSymbol();
	
}
