package usingCoap

import org.eclipse.californium.core.CoapClient
import org.eclipse.californium.core.CoapResponse
import org.eclipse.californium.core.coap.MediaTypeRegistry
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.delay

object basicrobotCoapClient {

    private val client = CoapClient()

    private fun sendToServer(move: String) {
        val uriStr = "coap://192.168.1.8/$move"
        client.uri = uriStr
        val arg = "{ \"activationCode\"  :  \"ACTIVATION_CODE\" }"
        println("USING: $uriStr")
        val res = client.put(arg, MediaTypeRegistry.TEXT_PLAIN)
        println("RESPONSE=" + res.responseText)
    }

    fun h() {  sendToServer("h") }
    fun w() { sendToServer("w")  }
    fun s() { sendToServer("s")  }
    fun r() { sendToServer("r")  }
    fun l() { sendToServer("l")  }
    fun x() { sendToServer("x")  }
    fun z() { sendToServer("z")  }


}

    fun main( ) = runBlocking  {
        basicrobotCoapClient.w()
        delay(1000)
        basicrobotCoapClient.s()
        delay(1000)
        basicrobotCoapClient.h()
    }

