/* Generated by AN DISI Unibo */ 
package it.unibo.ctx0
import it.unibo.kactor.QakContext
import it.unibo.kactor.sysUtil
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
	QakContext.createContexts(
	        "localhost", this, "sys0.pl", "sysRules.pl"
	)
}

