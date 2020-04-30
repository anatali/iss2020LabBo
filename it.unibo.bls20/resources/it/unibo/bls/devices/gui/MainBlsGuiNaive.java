package it.unibo.bls.devices.gui;

import it.unibo.bls.appl.BlsApplicationLogic;
import it.unibo.bls.components.ButtonObserver;
import it.unibo.bls.interfaces.IAppLogic;
import it.unibo.bls.interfaces.IApplListener;
import it.unibo.bls.interfaces.IButtonObservable;
import it.unibo.bls.interfaces.ILed;
import it.unibo.bls.interfaces.IObserver;
 

public class MainBlsGuiNaive   {
 	public static MainBlsGuiNaive createTheSystem(){
 		return new MainBlsGuiNaive();	//calls the super constructor
 	}

 	public static void main(String[] args) {
		ILed led              		= LedListenerAsGui.createLed(  );
		IButtonObservable button	= ButtonAsGui.createButton("clickonoffnaive");
  		button.addObserver( (IObserver) led );	 
		led.turnOff();
 	}
}
