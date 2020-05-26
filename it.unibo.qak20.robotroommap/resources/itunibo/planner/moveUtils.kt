package itunibo.planner

import aima.core.agent.Action
import it.unibo.kactor.ActorBasic
import kotlinx.coroutines.delay
import itunibo.planner.model.RobotState.Direction
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.ObjectInputStream
import java.io.FileInputStream
import itunibo.planner.model.RoomMap
import java.io.PrintWriter
import java.io.FileWriter
import java.io.ObjectOutputStream
import java.io.FileOutputStream

object moveUtils{
    private val actions : List<Action>? = null
    private var existPlan = false

	private var mapDims   : Pair<Int,Int> = Pair(0,0)
//	private var curPos    : Pair<Int,Int> = Pair(0,0)
//	private var curGoal   : Pair<Int,Int> = Pair(0,0)
//	private var direction = "downDir"
	private val PauseTime = 250
	
	private var MaxX        = 0
	private var MaxY        = 0

/*
 ABOUT THE ROOM MAP
*/	
 	fun getMapDimX( ) 	: Int{ return mapDims.first }
	fun getMapDimY( ) 	: Int{ return mapDims.second }
// 	fun getPosX(actor : ActorBasic)    	  : Int{ setPosition(actor); return curPos.first  } 
//	fun getPosY(actor : ActorBasic)    	  : Int{ setPosition(actor); return curPos.second }
//	fun getDirection(actor : ActorBasic)  : String{ setPosition(actor);return direction.toString() }
	fun mapIsEmpty() : Boolean{return (getMapDimX( )==0 &&  getMapDimY( )==0 ) }

	fun showMap() {
        println(RoomMap.getRoomMap().toString())
    }
	fun getMap() : String{
		return RoomMap.getRoomMap().toString() 
	}
	fun getMapOneLine() : String{ 
		return  "'"+RoomMap.getRoomMap().toString().replace("\n","@").replace("|","").replace(",","") +"'" 
	}

	fun getMapDims() : Pair<Int,Int> {
		if( RoomMap.getRoomMap() == null ){
			return Pair(0,0)
		}
	    val dimMapx = RoomMap.getRoomMap().getDimX()
	    val dimMapy = RoomMap.getRoomMap().getDimY()
	    //println("getMapDims dimMapx = $dimMapx, dimMapy=$dimMapy")
		return Pair(dimMapx,dimMapy)	
	}
 	
	fun loadRoomMap( fname: String  )   {
 		try{
 			val inps = ObjectInputStream(FileInputStream("${fname}.bin"))
			val map  = inps.readObject() as RoomMap;
 			println("loadRoomMap = $fname DONE")
			RoomMap.setRoomMap( map )
		}catch(e:Exception){			
			println("loadRoomMap = $fname FAILURE")
		}
		mapDims = getMapDims()//Pair(dimMapx,dimMapy)
	}
 
	fun saveRoomMap(  fname : String)   {		
        println("saveMap in $fname")
		val pw = PrintWriter( FileWriter(fname+".txt") )
		pw.print( RoomMap.getRoomMap().toString() )
		pw.close()
		
		val os = ObjectOutputStream( FileOutputStream(fname+".bin") )
		os.writeObject(RoomMap.getRoomMap())
		os.flush()
		os.close()
		mapDims = getMapDims()
    }
	
/*
 ABOUT THE ROBOT POSITION
*/		
	
//	fun atHome() : Boolean{
//		return curPos.first == 0 && curPos.second == 0
//	}
//	
//	fun showCurrentRobotState(){
//		println("===================================================")
//		showMap()
//		direction = plannerUtil.getDirection()
//		println("RobotPos=(${curPos.first}, ${curPos.second})  direction=$direction") //in map($MaxX,$MaxY)
//		println("===================================================")
//	}

	
	
// 	fun setObstacleOnCurrentDirection( actor : ActorBasic ){
//		doPlannedMove(actor, direction )
//	}
	
//	fun setDuration( actor : ActorBasic ){
//		val time = plannerUtil.getDuration()
// 	}
	
//	fun setDirection(  )  {
//		direction = plannerUtil.getDirection()
// 	}
	
/*
 ABOUT THE GOALS
		
	fun setGoal(  x: String, y: String) {
		val xv = Integer.parseInt(x)
		val yv = Integer.parseInt(y)
		plannerUtil.setGoal( xv,yv )
		curGoal=Pair(xv,yv)
	}	

	
	
	fun existPlan() : Boolean{ return existPlan }

	fun doPlannedMove(actor : ActorBasic, move: String){
		plannerUtil.doMove( move )
		setPosition(actor)
		//setDirection( actor )
	}
	
	fun setPosition( ){
		direction     = plannerUtil.getDirection()
		val posx      = plannerUtil.getPosX()
		val posy      = plannerUtil.getPosY()
		curPos        = Pair( posx,posy )
	}
*/
/*
 ABOUT MOVES
			
	suspend fun rotate(actor:ActorBasic,move:String,pauseTime:Int=PauseTime){
		when( move ){
			"a" -> rotateLeft(actor, pauseTime)
			"d" -> rotateRight(actor, pauseTime)
			"l" -> rotateLeft90( actor )
		    "r" -> rotateRight90( actor )
			else -> println("rotate $move unknown")
		}
  	}
 	suspend fun rotateRight(actor : ActorBasic, pauseTime : Int = PauseTime){
 		actor.forward("modelChange", "modelChange(robot,d)", "resourcemodel")
 		doPlannedMove(actor, "d" )	    //update map
		delay( pauseTime.toLong() )
	}
 	suspend fun rotateRight90(actor : ActorBasic ){
 		actor.forward("modelChange", "modelChange(robot,r)", "resourcemodel")
		delay( 800 )
 		doPlannedMove(actor, "r" )	    //update map
 	}
 	suspend fun rotateRight90tuning(actor : ActorBasic ){
 		actor.forward("modelChange", "modelChange(robot,r)", "resourcemodel")
		println("TUNING .... ")
 		readLine()
 		doPlannedMove(actor, "r" )	    //update map
 	}
	suspend fun rotateLeft(actor : ActorBasic, pauseTime : Int = PauseTime){
		actor.forward("modelChange", "modelChange(robot,a)", "resourcemodel")
 		doPlannedMove(actor, "a" )	    //update map	
		delay( pauseTime.toLong() )
	}
	suspend fun rotateLeft90( actor : ActorBasic ){
//		actor.forward("modelChange", "modelChange(robot,l)", "resourcemodel")
		println("rotateLeft90 .... ")
		actor.forward("cmd", "cmd(l)", "basicrobot")
		delay( 800 )
 		doPlannedMove(actor, "l" )	    //update map	
 	}
	suspend fun rotateLeft90tuning( actor : ActorBasic ){
//		actor.forward("modelChange", "modelChange(robot,l)", "resourcemodel")
		actor.forward("cmd", "cmd(l)", "basicrobot")
		println("TUNING .... ")
 		readLine()
		//delay( 800 )
 		doPlannedMove(actor, "l" )	    //update map	
 	}
 	suspend fun moveAhead(actor:ActorBasic, stepTime:Int, pauseTime:Int = PauseTime, dest:String ="resourcemodel"){
		println("moveUtils moveAhead stepTime=$stepTime")
//		actor.forward("modelChange", "modelChange(robot,w)", dest)
		actor.forward("cmd", "cmd(w)", "basicrobot")
		delay( stepTime.toLong() )
		actor.forward("modelChange", "modelChange(robot,h)", dest)
		doPlannedMove(actor, "w" )	//update map	
		delay( pauseTime.toLong() )
	} 
	suspend fun attemptTomoveAhead(actor:ActorBasic,stepTime:Int, dest:String ="basicrobot"){
 		//println("moveUtils attemptTomoveAhead stepTime=$stepTime")
		actor.request("step", "step(${stepTime})", dest)
   	}
	fun updateMapAfterAheadOk(actor : ActorBasic ){
		doPlannedMove(actor  , "w")
	}
	suspend fun backToCompensate(actor : ActorBasic, stepTime : Int, pauseTime : Int = PauseTime){
		println("moveUtils backToCompensate stepTime=$stepTime")
//		actor.forward("modelChange", "modelChange(robot,s)", "resourcemodel")
//		delay( stepTime.toLong() )
//		actor.forward("modelChange", "modelChange(robot,h)", "resourcemodel")
//		delay( pauseTime.toLong() )
		actor.forward("cmd", "cmd(s)", "basicrobot")
		delay( stepTime.toLong() )
		actor.forward("cmd", "cmd(h)", "basicrobot")
		delay( pauseTime.toLong() )
		
   	}
*/	
}
