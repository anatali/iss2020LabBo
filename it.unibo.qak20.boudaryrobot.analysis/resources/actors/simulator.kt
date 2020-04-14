package actors
import it.unibo.kactor.*
import kotlinx.coroutines.delay

class simulator( name : String ) : ActorBasic( name ){
    
    init{
		println("		--- simulator | start")
    }

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
    override suspend fun actorBody(msg : ApplMessage){
        println("		--- simulator | received  msg= $msg "  )  
		//robotAdapterQa receives the events raised by the actors in its context: we discard them		
		if( msg.msgId().equals( "simulateobstacle" )){
			delay( 800 )
			println("		--- simulator | emits collision"  )  
			emit("collision", "collision(wall,false)")
		}
     }
}