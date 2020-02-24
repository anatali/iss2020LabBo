package it.unibo.robotMock;

public class mockRobot {
	
	private static void simulateMove( String move ) {
		System.out.println("		%%% mockRobot | doing " + move );
	}
	
	public static void move( String cmd ) {
		simulateMove( cmd );
 	}

}
