/* Generated by AN DISI Unibo */ 
package it.unibo.explorewavelike

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Explorewavelike ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi			
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		  var StepCounter        = 0
			var CurrentPlannedMove = ""
			var atHome             = true
			val maxNumSteps        = 4
			
			var obstacleFound      = false
		
		//REAL ROBOT
		//var StepTime   = 600	 
		 
		//VIRTUAL ROBOT
		 var StepTime   = 350
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						println("&&&  ctxexplorewawelike STARTED")
						itunibo.planner.plannerUtil.initAI(  )
						delay(500) 
						forward("cmd", "cmd(l)" ,"basicrobot" ) 
						delay(300) 
						forward("cmd", "cmd(r)" ,"basicrobot" ) 
						delay(300) 
						println("INITIAL MAP")
						itunibo.planner.plannerUtil.showMap(  )
						itunibo.planner.plannerUtil.startTimer(  )
					}
					 transition( edgeName="goto",targetState="exploreStep", cond=doswitch() )
				}	 
				state("exploreStep") { //this:State
					action { //it:State
						 StepCounter = StepCounter + 1 
								   //obstacleFound      = false			
					}
					 transition( edgeName="goto",targetState="doCurrentStep", cond=doswitch() )
				}	 
				state("doCurrentStep") { //this:State
					action { //it:State
						request("movetoCell", "movetoCell($StepCounter,$StepCounter)" ,"optimisticwalker" )  
					}
					 transition(edgeName="t00",targetState="exploreStep",cond=whenReply("atcell"))
					transition(edgeName="t01",targetState="backToHome",cond=whenReply("walkbreak"))
				}	 
				state("backToHome") { //this:State
					action { //it:State
						println("explorewawelike |  going backToHome")
						request("movetoCell", "movetoCell(0,0)" ,"optimisticwalker" )  
					}
					 transition(edgeName="t02",targetState="tuneAndContinue",cond=whenReply("atcell"))
					transition(edgeName="t03",targetState="obstacleWhileBackHome",cond=whenReply("walkbreak"))
				}	 
				state("testIfAtHome") { //this:State
					action { //it:State
					}
					 transition( edgeName="goto",targetState="backToHome", cond=doswitchGuarded({ ! itunibo.planner.plannerUtil.atHome()   
					}) )
					transition( edgeName="goto",targetState="atHomeAgain", cond=doswitchGuarded({! ( ! itunibo.planner.plannerUtil.atHome()   
					) }) )
				}	 
				state("atHomeAgain") { //this:State
					action { //it:State
						println("explorewawelike | atHomeAgain $StepCounter")
						itunibo.planner.plannerUtil.showCurrentRobotState(  )
					}
					 transition( edgeName="goto",targetState="tuneAndContinue", cond=doswitch() )
				}	 
				state("tuneAndContinue") { //this:State
					action { //it:State
						 val Direction = itunibo.planner.plannerUtil.getDirection() 
								   obstacleFound = true
						println("explorewawelike | tuneAndContinue $Direction")
						if(  obstacleFound && Direction == "upDir"   
						 ){forward("cmd", "cmd(l)" ,"basicrobot" ) 
						itunibo.planner.plannerUtil.updateMap( "l"  )
						delay(250) 
						forward("cmd", "cmd(l)" ,"basicrobot" ) 
						itunibo.planner.plannerUtil.updateMap( "l"  )
						}
						if(  obstacleFound &&  Direction == "leftDir"   
						 ){forward("cmd", "cmd(l)" ,"basicrobot" ) 
						itunibo.planner.plannerUtil.updateMap( "l"  )
						delay(250) 
						}
						itunibo.planner.plannerUtil.showCurrentRobotState(  )
						 readLine()  
					}
					 transition( edgeName="goto",targetState="replanforthesamegoal", cond=doswitchGuarded({ obstacleFound  
					}) )
					transition( edgeName="goto",targetState="continueToexplore", cond=doswitchGuarded({! ( obstacleFound  
					) }) )
				}	 
				state("continueToexplore") { //this:State
					action { //it:State
						println("explorewawelike | continueToexplore")
						 obstacleFound = false  
					}
					 transition( edgeName="goto",targetState="exploreStep", cond=doswitchGuarded({ StepCounter < maxNumSteps  
					}) )
					transition( edgeName="goto",targetState="endOfJob", cond=doswitchGuarded({! ( StepCounter < maxNumSteps  
					) }) )
				}	 
				state("replanforthesamegoal") { //this:State
					action { //it:State
						if(  itunibo.planner.model.RoomMap.getRoomMap().isObstacle(StepCounter,StepCounter)  
						 ){println("explorewawelike | OBSTACLE IN THE GOAL CELL ")
						 StepCounter = StepCounter + 1  
						}
						else
						 {println("explorewawelike | replanforthesamegoal ($StepCounter,$StepCounter) ")
						 }
						 obstacleFound = false   
						 		   //readLine() 
					}
					 transition( edgeName="goto",targetState="doCurrentStep", cond=doswitch() )
				}	 
				state("obstacleWhileBackHome") { //this:State
					action { //it:State
						println("explorewawelike | obstacleWhileBackHome SORRY - Fatal error ")
					}
				}	 
				state("endOfJob") { //this:State
					action { //it:State
						println("explorewawelike | endOfJob ")
						itunibo.planner.plannerUtil.getDuration(  )
					}
				}	 
			}
		}
}