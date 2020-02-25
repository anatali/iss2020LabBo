package it.unibo.iss.it.unibo.springbootIntro;

import org.springframework.stereotype.Component;

@Component
public class ApplModel {
	
	static String curState ="Initial State";
	
	
	public ApplModel() {};
	
	public void update(String value) {
		curState = value;
	}
	
	public String getRep() {
		return curState;
	}

}
