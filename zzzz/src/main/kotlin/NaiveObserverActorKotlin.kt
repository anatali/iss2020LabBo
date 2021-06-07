import it.unibo.actor0.ActorBasicKotlin
import it.unibo.actor0.ApplMessage
import kotlinx.coroutines.CoroutineScope
import org.json.JSONObject

class NaiveObserverActorKotlin(name: String, scope:CoroutineScope)
                                     : ActorBasicKotlin(name, scope) {

    override suspend fun handleInput(info : ApplMessage){
        println("$name  | msgJson=$info   " )
        /*
        var msgJsonStr = info.msgContent
        val msgJson = JSONObject(msgJsonStr)
        println("$name  | msgJson=$msgJson   " )*/
    }
}