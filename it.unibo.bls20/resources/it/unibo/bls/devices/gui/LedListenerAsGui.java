package it.unibo.bls.devices.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Panel;
import java.util.Observable;
import it.unibo.bls.devices.mock.LedMock;
import it.unibo.bls.interfaces.ILed;
import it.unibo.bls.interfaces.IObserver;

public class LedListenerAsGui extends LedMock implements IObserver{ //
private Panel p ; 
private Frame frame;
private final Dimension sizeOn  = new Dimension(100,100);
private final Dimension sizeOff = new Dimension(30,30);

	public static ILed createLed(  ){
		LedListenerAsGui led = new LedListenerAsGui(FrameUtils.initFrame(300,300));
		led.turnOff();
		return led;
	}
	public static ILed createLed( Frame frame){
		LedListenerAsGui led = new LedListenerAsGui(frame);
		led.turnOff();
		return led;
	}
	//Constructor
	public LedListenerAsGui( Frame frame ) {
		super();
		this.frame = frame;
 		configure( );
  	}	
	protected void configure( ){
		p = new Panel();
		p.setSize( sizeOff );
		p.validate();
		p.setBackground(Color.red);
		p.validate();
		frame.add(BorderLayout.CENTER,p);
  	}
	@Override //IObserver
	public void update(Observable source, Object val) {
		if( state ) turnOff();
		else turnOn();
	}
	
	@Override //LedMock
	public void turnOn(){
		state = true;
		p.setSize( sizeOn );
		p.validate();
	}
	@Override //LedMock
	public void turnOff() {
		state = false;
		p.setSize( sizeOff );
		p.validate();
	}
}
