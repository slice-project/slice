package org.etri.slice.commons.eshop;

public interface AirConditionerProvider {
	
	void turnOn();
	
	void turnOff();
	
	void setTemperature(double temperature);
	
	double getTemperature();
}
