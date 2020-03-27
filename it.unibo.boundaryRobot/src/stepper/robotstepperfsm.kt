package stepper
//robotstepperfsm
import kotlinx.coroutines.CoroutineScope
import utils.AppMsg
import utils.Messages
import kotlinx.coroutines.delay
import fsm.Fsm
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.Job
import utils.virtualRobotSupportApp

lateinit var robot : Fsm

val stepTime       = 350L	//a step whose space s is the robot length (s=v*stepTime)
val backTime       = 80L	
val pauseStepTime  = 500L	//delay between steps

 	val stepMsg        = AppMsg.create("step",    "main",  "robotstepper" )
	val stepDoneMsg    = AppMsg.create("stepdone","robotstepper",   "main")
	val stepFailMsg    = AppMsg.create("stepfail","robotstepper",   "main")

	val timeMsg        = AppMsg.create("timer", "robotstepper", "timeractor", "${stepTime}")
	val endtimeMsg     = AppMsg.create("endtime", "timeractor", "robotstepper", "expired")

	val endMsg         = AppMsg.buildDispatch("main","end","end","robotstepper")
 

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
class timeractor ( name: String, scope: CoroutineScope ) : Fsm( name, scope){
	
	override fun getInitialState() : String{
		return "init"
	}

	override fun getBody() : (Fsm.() -> Unit){
		return {
			state("init") {
				action {
					println("timeractor init ")
				}
				transition( edgeName="t0",targetState="waitcmd", cond=doswitch() )
			}
			
			state( "waitcmd" ){
				action {
//					println("timeractor waits ... ")
				}				
				transition( edgeName="t0",targetState="work",    cond=whenDispatch("timer") ) 
				transition( edgeName="t0",targetState="endwork", cond=whenDispatch("end") ) 
			}
			
			state("work") {
				action {
					 val delayTime = currentMsg.CONTENT.toLong()
//					 println("timeractor work $delayTime")
					 delay( delayTime )
					 Messages.forward( endtimeMsg, robot  )					
				}
				transition( edgeName="t0",targetState="waitcmd", cond=doswitch() ) 
			}				
			state("endwork") {
				action {
					println("			timer ENDS")
 					terminate()
  				}
 			}									
		}//return
	}//getBody
}//timeractor



@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
class robostepper ( name: String, scope: CoroutineScope  ) : Fsm( name, scope){
lateinit var timer : Fsm
var stepCounter = 0	
	override fun getInitialState() : String{
		return "init"
	}

	suspend fun doMove( move: String ){
	  	virtualRobotSupportApp.doApplMove( move )		//move in the application-language
	}
		
	override fun getBody() : (Fsm.() -> Unit){
		return {
			state("init") {
				action {  
					println("robostepper init ")
					virtualRobotSupportApp.initClientConn()
					timer = timeractor( "timer", scope)
				}
				transition( edgeName="t0",targetState="work", cond=doswitch() )			//empty move	
			}
			state("work")	{
				action {
					//println("robostepper waits ... ")
				}				
				transition( edgeName="t0",targetState="doStep",  cond=whenDispatch("step") )	
				transition( edgeName="t1",targetState="endwork", cond=whenDispatch("end")  )	
			}
			state("doStep") {
				action {
					doMove("w")
 					Messages.forward( timeMsg, timer  )
				}
				transition( edgeName="t2",targetState="stepKo", cond=whenDispatch("sensor")  )   	//(first)
				transition( edgeName="t1",targetState="stepOk", cond=whenDispatch("endtime") )
				transition( edgeName="t3",targetState="endwork", cond=whenDispatch("end") )
			}
			state("stepOk") {
				action {
					doMove("h")
 					stepCounter++
					println("			robostepper stepCounter=$stepCounter")
					//send stepDoneMsg to the caller
 				}
				transition( edgeName="t0",targetState="work", cond=doswitch() )				
 			}			
			state("stepKo") {
				action {
					println("robostepper stepKo")
					//send stepFailMsg to the caller
   				}
				transition( edgeName="t0",targetState="consumeEndTime", cond=whenDispatch("endtime") )
			}	//WARNING: the msg timer IS NOT LOST: it should be consumed
			
			state("consumeEndTime"){
				action {
					println("robostepper consume endtime")
   				}
				transition( edgeName="t0",targetState="endwork", cond=doswitch() )				
			}
									
			state("endwork") {
				action {
					println("			robostepper ENDS")
					Messages.forward( endMsg, timer  )
 					terminate()
  				}
 			}	 							
		}//return
	}//getBody	
}//robostepper


@kotlinx.coroutines.ExperimentalCoroutinesApi
@kotlinx.coroutines.ObsoleteCoroutinesApi
fun main()=runBlocking{
	println("main STARTS")
	
	robot = robostepper("robostepper", this )
  	virtualRobotSupportApp.setRobotTarget( robot   ) //Configure - Inject
	
	delay( 100 )
	
	for( i in 1..8 ){	
		delay(1000)
		Messages.forward( stepMsg, robot  )
	}
	
 	Messages.forward( endMsg, robot  )
	
	(robot.fsmactor as Job).join()	//waits for termination  
	
	println(			"main ENDS")
}