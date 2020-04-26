package it.unibo.kactor

import kotlinx.coroutines.launch
import org.eclipse.californium.core.server.resources.CoapExchange
import kotlinx.coroutines.delay

/*
 * ----------------------------------------------------------------------------------------------
 * Temporary actor that makes a qak request and waits for a reply to a qak request
 * ----------------------------------------------------------------------------------------------
 */

class CoapToActor( name : String, val exchange: CoapExchange,
                   val owner: ActorBasic, val extmsg : ApplMessage) : ActorBasic( name ){
var answer = "noanswer" 	
 	init{
        this.context = owner.context
        context!!.addInternalActor( this )
		sysUtil.traceprintln("$tt $name| CREATED in ctx=${context!!.name} exchange=${exchange.getSourceAddress()}")
//        exchange.respond( "CoapToActor ${answer}" )
// 		scope.launch{ request( extmsg.msgId(), extmsg.msgContent(), owner) }
		scope.launch{ autoMsg("start", "start") }
     }

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
     override suspend fun actorBody(msg : ApplMessage){
		if( msg.msgId() == "start") {
	        sysUtil.traceprintln("$tt $name | map PUT in request to ${owner.name}:  $extmsg "  )
 			request( extmsg.msgId(), extmsg.msgContent(), owner)
		} 
        if( msg.isReply() ){
  	        sysUtil.traceprintln("$tt $name | PUT response: $msg exchange=${exchange.getSourceAddress()}"  )
 			answer = msg.toString().replace(name,owner.name)
			owner.updateResourceRep( answer )
//			exchange.respond( ... ) //DOES NOT WORK
//            context!!.removeInternalActor( this )
         }
	}
}