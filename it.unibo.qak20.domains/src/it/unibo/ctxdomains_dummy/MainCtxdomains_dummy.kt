/* Generated by AN DISI Unibo */ 
package it.unibo.ctxdomains_dummy
import it.unibo.kactor.QakContext
import it.unibo.kactor.sysUtil
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
	QakContext.createContexts(
	        "localhost", this, "domainssubsys.pl", "sysRules.pl"
	)
}

