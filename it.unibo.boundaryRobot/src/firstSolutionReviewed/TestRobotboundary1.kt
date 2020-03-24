package firstSolutionReviewed

import org.junit.Before
import org.junit.After
import org.junit.Test
import org.junit.Assert.assertTrue
 
class TestRobotboundary1 {
	
	@Before
	fun systemSetUp() {
	}
	
	@After
	fun terminate() {
		println("%%%  TestRobotboundary1 terminates ")
	}

  	
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	@Test
	fun testWork() {
		assertTrue( firstSolutionReviewed.nStep == 0    )
		firstSolutionReviewed.main()
//		println("%%%  testWork numOfSteps=${firstSolution.numOfSteps} ")	
		assertTrue( firstSolutionReviewed.nStep == 4    )
	}

}