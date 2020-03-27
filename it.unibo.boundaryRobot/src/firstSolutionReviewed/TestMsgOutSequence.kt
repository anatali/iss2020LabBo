package firstSolutionReviewed

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
 
class TestMsgOutSequence {
	
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
		println("%%%  TestMsgOutSequence terminates ")
	}

  	
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	@Test
	fun testWrongMsgSequence( ) {
 		assertTrue( firstSolutionReviewed.nStep == 0    )
		runBlocking {	//block the current thread until the code block is done
     		Messages.forward( resumeMsg.toString(), robotboundary1 )
 			delay( 1000 )
			println("%%%  testWrongMsgSequence sends")
			Messages.forward( startMsg.toString(), robotboundary1 )
			(robotboundary1 as Job).join()	//waits for termination of robotboundary0
 		}	
//		println("%%%  testWork numOfSteps=${firstSolution.numOfSteps} ")	
		assertTrue( firstSolutionReviewed.nStep == 4    )
	}

}