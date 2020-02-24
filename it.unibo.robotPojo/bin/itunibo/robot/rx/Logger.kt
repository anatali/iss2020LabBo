package itunibo.robot.rx

 
import it.unibo.kactor.ApplMessage
import alice.tuprolog.Struct
import alice.tuprolog.Term
import java.io.PrintWriter
import java.io.FileWriter
import java.io.ObjectOutputStream
import java.io.FileOutputStream
import it.unibo.kactor.ActorBasic

class Logger(name : String) : ActorBasic(name){
	var pw : PrintWriter
	
 	init{   pw = PrintWriter( FileWriter(name+".txt") ) }
    
	override suspend fun actorBody(msg: ApplMessage) {
 		val vStr  = (Term.createTerm( msg.msgContent() ) as Struct).getArg(0).toString()
        //println("   $name |  handles msg= $msg  vStr=$vStr")
		elabData( vStr )
		emitLocalStreamEvent(msg.msgId(),"sonar($vStr)")	//wec could change the event ...
	}
 
 	protected suspend fun elabData(data : String ){
		println("	-------------------------------------------- $name data=$data")
		saveData( data)
	}
	
	fun saveData(   data : String )   {		
  		pw.append( "$data\n " )
		pw.flush()
    }

}