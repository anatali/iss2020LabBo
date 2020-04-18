package it.unibo.bls.components;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class Utils {

	public static void showSystemInfo(){
		System.out.println(
			"COMPUTER | memory="+ Runtime.getRuntime().totalMemory() +
					" num of processors=" +  Runtime.getRuntime().availableProcessors());
		System.out.println(
			"AT START | num of threads="+ Thread.activeCount() +" currentThread=" + Thread.currentThread() );
	}

 
	public static void delay(int n) {
		try {
			Thread.sleep(n);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
