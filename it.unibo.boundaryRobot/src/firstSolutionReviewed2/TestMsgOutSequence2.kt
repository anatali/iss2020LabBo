package firstSolutionReviewed2

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
 
class TestMsgOutSequence2 {
	
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	@Before
	fun systemSetUp() {
		kotlin.concurrent.thread(start = true) {	 
			main( arrayOf( "nouser" ) )  //Blocks
		}		
	}
	
	@After
	fun terminate() {
		println("TestMsgOutSequence2 terminates ")
	}

  	
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	@Test
	fun testWrongMsgSequence2( ) {
 		assertTrue( firstSolutionReviewed2.nStep == 0    )
		runBlocking {	//block the current thread until the code block is done
 				//Simulate a 'wrong' message: stop before start		
		 		Messages.forward( stopMsg.toString(), robotboundary2 )
		 		delay( 500 ) 
			Messages.forward( startMsg.toString(), robotboundary2 )
				//Simulate a 'wrong' message: resume after start		
				delay( 200 ) 
			 	Messages.forward( resumeMsg.toString(), robotboundary2 )
			for( i in 1..5){
		 	 	delay( 1300 )
				println("test FORWARD STOP ${i}  nStep=$nStep");
			 	Messages.forward( stopMsg.toString(), robotboundary2 )
			 		//Simulate a 'wrong' message: stop after a stop		
			 		delay( 200 ) 
			 		Messages.forward( stopMsg.toString(), robotboundary2 )
			 		//Simulate a 'wrong' message: stop after a stop		
			 		delay( 200 )		
					Messages.forward( stopMsg.toString(), robotboundary2 )
			 	delay( 1200 )		
				println("test FORWARD RESUME ${i}  nStep=$nStep");
			 	Messages.forward( resumeMsg.toString(), robotboundary2 )
			 		//Simulate a 'wrong' message: resume after a resume			
			 		delay( 200 )
			 		Messages.forward( resumeMsg.toString(), robotboundary2 )
			 }//for
			 (robotboundary2 as Job).join()	//waits for termination of robotboundary0
 		}//runBlocking	
 		assertTrue( firstSolutionReviewed2.nStep == 4    )
	}//testWrongMsgSequence2


}
