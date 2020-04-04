package it.unibo.kactor

import kotlinx.coroutines.launch
import org.eclipse.californium.core.server.resources.CoapExchange

/*
 * ----------------------------------------------------------------------------------------------
 * Temporary actor that makes a qak request and waits for a reply to a qak request
 * ----------------------------------------------------------------------------------------------
 */

class CoapToActor( name : String, val exchange: CoapExchange,
                   val owner: ActorBasic, val msg : ApplMessage) : ActorBasic( name ){
    init{
        this.context = owner.context
        context!!.addInternalActor( this )
		//println(" $tt $name| START in ctx=${context!!.name}")
        scope.launch{ request( msg.msgId(), msg.msgContent(), owner) }
     }

     override suspend fun actorBody(msg : ApplMessage){
        //println(" $tt $name | received  $msg "  ) //msg.msgContent() is the answer
        if( msg.isReply() ){    //defensive
            //println(" $tt $name | respond  ${msg.msgContent()} "  ) //msg.msgContent() is the answer
            exchange.respond( msg.toString() )
            context!!.removeInternalActor( this )
            //println(" $tt $name | ENDS   "  )
        }
	}
}