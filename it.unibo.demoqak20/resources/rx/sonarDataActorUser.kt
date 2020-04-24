package rx
import it.unibo.kactor.*
import alice.tuprolog.*
import rx.sonaractorfilter
import rx.sonardsh
import java.io.File

/*
 * ----------------------------------------------------------------------------------------------
 * ----------------------------------------------------------------------------------------------
 */

class sonarDataActorUser( name : String ) : ActorBasic( name ){  
    init{
		File("${name}_MsLog.txt").delete()  //clear
		println("$tt $name | start in ctx=${this.context}")
		val dsh = sonardsh("sonardsh", this)
		this.subscribe(dsh)		//set the start object of data-stream pipes
		//defines a filter
		val filter   = rx.sonaractorfilter("filter", this)
		dsh.subscribe( filter )
	}		  		      
 
 
    override suspend fun actorBody(msg : ApplMessage){
        //println("	--- sonarDataActorUser | received  msg= $msg "  ) //msg.msgContent()=cmd(X)
		println("$tt $name | received  $msg "  )
		File("${name}_MsgLog.txt").appendText("${msg}\n")
    }
}