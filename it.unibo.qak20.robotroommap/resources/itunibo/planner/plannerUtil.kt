package itunibo.planner

import java.util.ArrayList
import aima.core.agent.Action
import aima.core.search.framework.SearchAgent
import aima.core.search.framework.problem.GoalTest
import aima.core.search.framework.problem.Problem
import aima.core.search.framework.qsearch.GraphSearch
import aima.core.search.uninformed.BreadthFirstSearch
import java.io.PrintWriter
import java.io.FileWriter
import java.io.ObjectOutputStream
import java.io.FileOutputStream
import java.io.ObjectInputStream
import java.io.FileInputStream
import itunibo.planner.model.RobotState
import itunibo.planner.model.Functions
import itunibo.planner.model.RobotState.Direction
import itunibo.planner.model.RobotAction
import itunibo.planner.model.RoomMap
import itunibo.planner.model.Box

object plannerUtil { 
    private var initialState: RobotState? = null
	private var actions: List<Action>?    = null
	
	//private val RoomMap.getRoomMap() = RoomMap.getRoomMap()
	
    private var curPos    : Pair<Int,Int> = Pair(0,0)
	private var curGoal   : Pair<Int,Int> = Pair(0,0)
	private var direction = "downDir"
	
	private var actionSequence : Iterator<Action>? = null


/*
 * ------------------------------------------------
 * PLANNING
 * ------------------------------------------------
 */
    private var search: BreadthFirstSearch? = null
    var goalTest: GoalTest = Functions()		//init
    private var timeStart: Long = 0

    @Throws(Exception::class)
    fun initAI() {
         initialState = RobotState(0, 0, RobotState.Direction.DOWN)
        search       = BreadthFirstSearch(GraphSearch())
       println("plannerUtil initAI done")
    }

	
	var currentGoalApplicable = true;
	
    fun resetGoal( x: Int, y: Int) {
        try {
//			var canMove = RoomMap.getRoomMap().canMove( x,y-1, initialState!!.direction  );
            println("resetGoal $x,$y while robot in cell: ${getPosX()},${getPosY()} direction=${getDirection()} ") //canMove=$canMove
            
			if( RoomMap.getRoomMap().isObstacle(x,y) ) {
				println("ATTEMPT TO GO INTO AN OBSTACLE ")
				currentGoalApplicable = false
 				resetActions()
				return
			}else currentGoalApplicable = true
            
			RoomMap.getRoomMap().put(x, y, Box(false, true, false))  //set dirty

			goalTest = GoalTest { state  : Any ->
                val robotState = state as RobotState
				(robotState.x == x && robotState.y == y)
            }
			moveUtils.showMap()
         } catch (e: Exception) {
             //e.printStackTrace()
        }
    }
	
	fun resetActions(){
		actions = listOf<Action>()
	}
	fun existActions() : Boolean{
		//println("existActions ${actions!!.size}")
		return actions!!.size>0   
	}
	
	
    fun getActions() : List<Action>{
        return actions!!
    }
 
    @Throws(Exception::class)
    fun doPlan(): List<Action>? {
        //var actions: List<Action>?
		
		if( ! currentGoalApplicable ){
			println("plannerUtil doPlan cannot go into an obstacle")
			actions = listOf<Action>()
			return actions		//empty list
		} 
		
        val searchAgent: SearchAgent
        //println("plannerUtil doPlan newProblem (A) $goalTest" );
		val problem = Problem(initialState, Functions(), Functions(), goalTest, Functions())
		
		
        //println("plannerUtil doPlan newProblem (A) search " );
        searchAgent = SearchAgent(problem, search!!)
        actions  = searchAgent.actions
		
		println("plannerUtil doPlan actions=$actions")
		
        if (actions == null || actions!!.isEmpty()) {
            println("plannerUtil doPlan NO MOVES !!!!!!!!!!!! $actions!!"   )
            if (!RoomMap.getRoomMap().isClean) RoomMap.getRoomMap().setObstacles()
            //actions = ArrayList()
            return null
        } else if (actions!![0].isNoOp) {
            println("plannerUtil doPlan NoOp")
            return null
        }
		
        //println("plannerUtil doPlan actions=$actions")
		setActionMoveSequence()
        return actions
    }
	
/*
 ABOUT THE ROBOT POSITION
*/		
	
	fun atHome() : Boolean{
		return curPos.first == 0 && curPos.second == 0
	}
	
	fun showCurrentRobotState(){
		println("===================================================")
		moveUtils.showMap()
		direction = plannerUtil.getDirection()
		println("RobotPos=(${curPos.first}, ${curPos.second})  direction=$direction") //in map($MaxX,$MaxY)
		println("===================================================")
	}
	

/*
 */
	fun setActionMoveSequence(){
		if( actions != null ) {
			 actionSequence = actions!!.iterator()
		}
	}
	
	fun getNextPlannedMove() : String{
		if( actionSequence == null ) return ""
		else if( actionSequence!!.hasNext()) return actionSequence!!.next().toString()
				else return ""
	}

/*
* ------------------------------------------------
* MAP UPDATE
* ------------------------------------------------
*/	
    fun getPosX() : Int{ return initialState!!.x }
    fun getPosY() : Int{ return initialState!!.y }
     
 	fun updateMap( move: String ){
		doMove( move )
		setPosition( )
 	}
	
	fun updateMapObstacleOnCurrentDirection(   ){
		doMove( direction )
		setPosition( )
	}
	
	fun setPosition( ){
		direction     =  getDirection()
		val posx      =  getPosX()
		val posy      =  getPosY()
		curPos        = Pair( posx,posy )
	}
	
	
    fun doMove(move: String) {
        val x = initialState!!.x 
        val y = initialState!!.y
       // println("plannerUtil: doMove move=$move  dir=$dir x=$x y=$y dimMapX=$dimMapx dimMapY=$dimMapy")
       try {
            when (move) {
                "w" -> {
                    RoomMap.getRoomMap().put(x, y, Box(false, false, false)) //clean the cell
                    initialState = Functions().result(initialState!!, RobotAction(RobotAction.FORWARD)) as RobotState
                    RoomMap.getRoomMap().put(initialState!!.x, initialState!!.y, Box(false, false, true))
                }
                "s" -> {
                    initialState = Functions().result(initialState!!, RobotAction(RobotAction.BACKWARD)) as RobotState
                    RoomMap.getRoomMap().put(initialState!!.x, initialState!!.y, Box(false, false, true))
                }
                "a"  -> {
                    initialState = Functions().result(initialState!!, RobotAction(RobotAction.TURNLEFT)) as RobotState
                    RoomMap.getRoomMap().put(initialState!!.x, initialState!!.y, Box(false, false, true))
                }
                "l" -> {
                    initialState = Functions().result(initialState!!, RobotAction(RobotAction.TURNLEFT)) as RobotState
                    RoomMap.getRoomMap().put(initialState!!.x, initialState!!.y, Box(false, false, true))
                }
                "d" -> {
                    initialState = Functions().result(initialState!!, RobotAction(RobotAction.TURNRIGHT)) as RobotState
                    RoomMap.getRoomMap().put(initialState!!.x, initialState!!.y, Box(false, false, true))
                }
                "r" -> {
                    initialState = Functions().result(initialState!!, RobotAction(RobotAction.TURNRIGHT)) as RobotState
                    RoomMap.getRoomMap().put(initialState!!.x, initialState!!.y, Box(false, false, true))
                }
                "c"    //forward and  clean
                -> {
                    RoomMap.getRoomMap().put(x, y, Box(false, false, false))
                    initialState = Functions().result(initialState!!, RobotAction(RobotAction.FORWARD)) as RobotState
                    RoomMap.getRoomMap().put(initialState!!.x, initialState!!.y, Box(false, false, true))
                }
				//Box(boolean isObstacle, boolean isDirty, boolean isRobot)
                "rightDir" -> RoomMap.getRoomMap().put(x + 1, y, Box(true, false, false)) 
                "leftDir"  -> RoomMap.getRoomMap().put(x - 1, y, Box(true, false, false))
                "upDir"    -> RoomMap.getRoomMap().put(x, y - 1, Box(true, false, false))
                "downDir"  -> RoomMap.getRoomMap().put(x, y + 1, Box(true, false, false))

		   }//when
        } catch (e: Exception) {
            println("plannerUtil doMove: ERROR:" + e.message)
        }
    }
     
	
	
/*
 * ---------------------------------------------------------
 * ---------------------------------------------------------
 */
    fun setGoalInit() {
        goalTest = Functions()
    }

	fun setGoal( x: String, y: String) {
		setGoal( Integer.parseInt(x), Integer.parseInt(y))
	}	
	fun planForGoal( x: String, y: String) {
		//look at the cell x,y
		var targetCell = false
		val vx = Integer.parseInt(x)
		val vy = Integer.parseInt(y)
		try{
			targetCell = RoomMap.getRoomMap().isNotExplored(vx,vy)
						 || RoomMap.getRoomMap().isObstacle(vx,vy)
			println("planForGoal targetCell=$targetCell")				  
		}catch( e : Exception ){
			println("WARNING: target cell is unknown")
		}
		resetGoal(vx,vy)		
		doPlan()   
 	}	
 
	
	fun setPlanForNextDirty() {
		val rmap = RoomMap.getRoomMap()
		for( i in 0..moveUtils.getMapDimX( )-1 ){
			for( j in 0..moveUtils.getMapDimY( )-1 ){
				//System.out.println( ""+ i + "," + j + " -> " + rmap.isDirty(i,j)   );
				if( rmap.isDirty(i,j)  ){
					resetGoal( i,j )
					doPlan() 
					return
				}
			}
		}
 	}
	
	
	//Box(boolean isObstacle, boolean isDirty, boolean isRobot)
    fun setGoal( x: Int, y: Int, cleanGoalCell : Boolean = true) {
        try {
            println("setGoal $x,$y while robot in cell: ${getPosX()},${getPosY()} direction=${getDirection()}")	
            if( cleanGoalCell ) RoomMap.getRoomMap().put(x, y, Box(false, true, false))
			//initialState = RobotState(getPosX(), getPosY(), initialState!!.direction ) 
            goalTest = GoalTest { state  : Any ->
                val robotState = state as RobotState
				(robotState.x == x && robotState.y == y)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun startTimer() {
        timeStart = System.currentTimeMillis()
    }
	
    fun getDuration() : Int{
        val duration = (System.currentTimeMillis() - timeStart).toInt()
		println("DURATION = $duration")
		return duration
    }
	
	fun getDirection() : String{
 		val direction = initialState!!.direction 
		when( direction ){
			Direction.UP    -> return "upDir"
			Direction.RIGHT -> return "rightDir"
			Direction.LEFT  -> return "leftDir"
			Direction.DOWN  -> return "downDir"
			else            -> return "unknownDir"
 		}
  	}
	
		fun wallFound(){
 		 val dimMapx = RoomMap.getRoomMap().getDimX()
		 val dimMapy = RoomMap.getRoomMap().getDimY()
		 val dir = initialState!!.getDirection()
		 val x   = initialState!!.getX()
		 val y   = initialState!!.getY()
		 setObstacleWall( dir,x,y )
 		 println("pallnerUtil wallFound dir=$dir  x=$x  y=$y dimMapX=$dimMapx dimMapY=$dimMapy");
		 doMove( dir.toString() )  //set cell
 		 if( dir == Direction.UP)    setWallRight(dimMapx,dimMapy,x,y)
		 if( dir == Direction.RIGHT) setWallDown(dimMapx,dimMapy,x,y)  
	}
 	fun setObstacleWall(  dir: Direction, x:Int, y:Int){
		if( dir == Direction.DOWN  ){ RoomMap.getRoomMap().put(x, y + 1, Box(true, false, false)) }
		if( dir == Direction.RIGHT ){ RoomMap.getRoomMap().put(x + 1, y, Box(true, false, false)) }
//			Direction.DOWN  -> RoomMap.getRoomMap().put(x, y + 1, Box(true, false, false))
//			//Direction.UP    -> RoomMap.getRoomMap().put(x, y - 1, Box(true, false, false)) //NO X
//			//Direction.LEFT  -> RoomMap.getRoomMap().put(x - 1, y, Box(true, false, false)) //NO X
//			Direction.RIGHT -> RoomMap.getRoomMap().put(x + 1, y, Box(true, false, false)) 
// 		}
	}
	fun setWallDown(dimMapx: Int,dimMapy: Int,x: Int,y: Int ){
		 var k   = 0
		 while( k < dimMapx ) {
			RoomMap.getRoomMap().put(k, y+1, Box(true, false, false))
			k++
		 }		
	}
	
	fun setWallRight(dimMapx: Int,dimMapy: Int, x: Int,y: Int){
 		 var k   = 0
		 while( k < dimMapy ) {
			RoomMap.getRoomMap().put(x+1, k, Box(true, false, false))
			k++
		 }		
	}


/*
 * Direction

    fun rotateDirection() {
        //println("before rotateDirection: " + initialState.getDirection() );
        initialState = Functions().result(initialState!!, RobotAction(RobotAction.TURNLEFT)) as RobotState
        initialState = Functions().result(initialState!!, RobotAction(RobotAction.TURNLEFT)) as RobotState
        //println("after  rotateDirection: " + initialState.getDirection() );
        //update the kb
        val x = initialState!!.x
        val y = initialState!!.y
        val newdir = initialState!!.direction.toString().toLowerCase() + "Dir"
    }
	
 
    fun setObstacles(   ){
		RoomMap.getRoomMap().setObstacles()
 	}
	
	
	
  */
}