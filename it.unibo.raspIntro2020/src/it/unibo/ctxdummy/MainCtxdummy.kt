/* Generated by AN DISI Unibo */ 
package it.unibo.ctxdummy
import it.unibo.kactor.QakContext
import it.unibo.kactor.sysUtil
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
	QakContext.createContexts(
	        "192.168.1.14", this, "sonarobserver.pl", "sysRules.pl"
	)
}
