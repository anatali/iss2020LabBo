package fsm

import org.junit.Before
import org.junit.After
import org.junit.Test
import org.junit.Assert.assertTrue
import utils.Messages
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.Job
import utils.AppMsg
import utils.virtualRobotSupportApp

class TestRobotboundaryFsm {
	
lateinit var robot : Fsm
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	@Before
	fun systemSetUp() {
		kotlin.concurrent.thread(start = true) {	 
			 robot = robotboundaryfsm ( "robot",  GlobalScope )
			 virtualRobotSupportApp.setRobotTarget( robot   ) //Configure - Inject
		}		
	}
	
	@After
	fun terminate() {
		println("TestRobotboundaryFsm terminates ")
	}
	
	
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	suspend fun testMsgOutOfsequence(){
 				//Simulate a 'wrong' message: stop before start		
		 		delay( 500 ) 
		 		Messages.forward( stopMsg, robot )
		 		delay( 500 ) 
			Messages.forward( startMsg, robot )
				//Simulate a 'wrong' message: resume after start		
				delay( 200 ) 
			 	Messages.forward( resumeMsg, robot )
			for( i in 1..5){
		 	 	delay( 1300 )
				println("test FORWARD STOP ${i}  nStep=$nStep");
			 	Messages.forward( stopMsg, robot )
			 		//Simulate a 'wrong' message: stop after a stop		
			 		delay( 200 ) 
			 		Messages.forward( stopMsg, robot )
			 		//Simulate a 'wrong' message: stop after a stop		
			 		delay( 200 )		
					Messages.forward( stopMsg, robot )
			 	delay( 1200 )		
				println("test FORWARD RESUME ${i}  nStep=$nStep");
			 	Messages.forward( resumeMsg, robot )
			 		//Simulate a 'wrong' message: resume after a resume			
			 		delay( 200 )
			 		Messages.forward( resumeMsg, robot )
			 }//for
		
	}	
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	suspend fun testStart(){
			Messages.forward( startMsg, robot  )
	}
	
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	@Test
	fun testFsm( ) {
		assertTrue(  nStep == 0    )
		runBlocking{
			 
			testMsgOutOfsequence()
			
			(robot.fsmactor as Job).join()	
		}
		assertTrue(  nStep == 4    )
	}
	
}