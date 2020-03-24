package firstSolution

import org.junit.Before
import org.junit.After
import org.junit.Test
import org.junit.Assert.assertTrue
 
class TestRobotboundary0 {
	
	@Before
	fun systemSetUp() {
	}
	
	@After
	fun terminate() {
		println("%%%  TestRobotboundary0 terminates ")
	}
	
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	@Test
	fun testWork() {
		assertTrue( firstSolutionReviewed.nStep == 0    )
		firstSolution.main()
//		println("%%%  testWork numOfSteps=${firstSolution.numOfSteps} ")	
		assertTrue( firstSolution.nStep == 4  )
	}

}