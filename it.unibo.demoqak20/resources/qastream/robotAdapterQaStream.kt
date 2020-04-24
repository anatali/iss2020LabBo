package qastream
import it.unibo.kactor.*
import alice.tuprolog.*
import rx.sonaractorfilter
import rx.sonardsh

/*
 * ----------------------------------------------------------------------------------------------
 * transforms a cmd:cmd(M) into a command M  to the concrete robot specified in the file
 * basicRobotConfig.pl.
 * To achieve its goal, the actor creates and uses an object itunibo.robot.robotSupport
 * that works as a factory/facade for the specific concrete robot to be used.
 * This support is created by giving to it a data-stream handler (dsh) of class sonaractorfilter
 * in order to allow the creation of data-stream pipes
 * ----------------------------------------------------------------------------------------------
 */

class robotAdapterQaStream( name : String ) : ActorBasic( name ){  
    init{
		println("	--- robotAdapterQaStream | start in ctx=${this.context}")
		val sol1 = pengine.solve( "consult('basicRobotConfig.pl')." )
		if( ! sol1.isSuccess() ){
			println("	--- robotAdapterQaStream | ERROR in basicRobotConfig.pl")
		}else{
	 		val sol2 = pengine.solve( "robot(R, PORT)." )
	 		if( sol2.isSuccess() ){
				val host =  sol2.getVarValue("R").toString()
				val port =  sol2.getVarValue("PORT").toString()
				println("	--- robotAdapterQaStream | USING $host : $port")
				val dsh = sonardsh("sonardsh", this)
				this.subscribe(dsh)		//set the start object of data-stream pipes
				//defines a filter
				val filter   = rx.sonaractorfilter("filter", this)
				dsh.subscribe( filter )
				//gives the end of the PIPE to the robotSupport
				//filter is the endpipe. It propagates to subscribers set by robotSupport.
				//filter owns this and uses it to emit obstacle events
//				itunibo.robot.robotSupport.create( this,host,port,filter  )   
			}
		}		  		      
    }
 
    override suspend fun actorBody(msg : ApplMessage){
        //println("	--- robotAdapterQaStream | received  msg= $msg "  ) //msg.msgContent()=cmd(X)
		sysUtil.traceprintln(" $tt $name | received  $msg "  ) //msg.msgContent()=cmd(X)
		//robotAdapterQaStream receives the events raised by the actors in its context
		if( msg.isEvent() ) return			
		val move = (Term.createTerm(msg.msgContent()) as Struct).getArg(0).toString()
//		itunibo.robot.robotSupport.move( move  )
    }
}