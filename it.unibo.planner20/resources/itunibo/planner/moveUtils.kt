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
import java.io.File

object moveUtils{
    private val actions : List<Action>? = null
    private var existPlan = false

	private var mapDims   : Pair<Int,Int> = Pair(0,0)
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
	
	 
}
