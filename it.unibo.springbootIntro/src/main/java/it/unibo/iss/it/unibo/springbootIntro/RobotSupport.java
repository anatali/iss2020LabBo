package it.unibo.iss.it.unibo.springbootIntro;

import org.springframework.stereotype.Component;

import it.unibo.robot.robotAdapter;
import itunibo.robotMock.mockrobotSupport;

@Component
public class RobotSupport implements robotAdapter{
/*
 * TODO: look at a configuration file and use the proper adapter
 */
	@Override
	public void create() {
		mockrobotSupport.INSTANCE.create();
		
	}

	@Override
	public void move(String cmd) {
		mockrobotSupport.INSTANCE.move(cmd);		
	}
	
}
