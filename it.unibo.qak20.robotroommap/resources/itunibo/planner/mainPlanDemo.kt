package itunibo.planner

import aima.core.agent.Action


object mainPlanDemo {
    fun demo() {
        println("===== demo")

        try {
			plannerUtil.startTimer()

			plannerUtil.initAI()
            //plannerUtil.cleanQa()
            println("===== initial map")
            moveUtils.showMap()
            doSomeMOve()
            println("===== map after some move")
            moveUtils.showMap()
            val actions = plannerUtil.doPlan()
            println("===== plan actions: " + actions!!)
			
            executeMoves( )			
            println("===== map after plan")
            moveUtils.showMap()
 

            //plannerUtil.cell0DirtyForHome()
            plannerUtil.setGoal(0,0);
			plannerUtil.doPlan()
            executeMoves( )
            println("===== map after plan for home")
            moveUtils.showMap()

			plannerUtil.getDuration()
		
		} catch (e: Exception) {
            //e.printStackTrace()
        }

    }

    @Throws(Exception::class)
    internal fun doSomeMOve() {
        plannerUtil.doMove("w")
        plannerUtil.doMove("a")
        plannerUtil.doMove("w")
        plannerUtil.doMove("w")
        plannerUtil.doMove("d")
        plannerUtil.doMove("w")
        plannerUtil.doMove("a")
        plannerUtil.doMove("obstacleOnRight")
    }


    @Throws(Exception::class)
    internal fun executeMoves( ) {
        var move = plannerUtil.getNextPlannedMove()
        while ( move.length > 0 ) {
            plannerUtil.doMove( move )
			move = plannerUtil.getNextPlannedMove()
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        mainPlanDemo.demo()
    }

}
