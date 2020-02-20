// See https://try.kotlinlang.org
 
import java.util.concurrent.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.selects.*
import kotlin.system.measureTimeMillis

// this extends the behavior of CoffeeShopFour to merge the completed coffee orders
@kotlinx.coroutines.ExperimentalCoroutinesApi
fun main(args: Array<String>) = runBlocking(CoroutineName("main")) {    
    val orders = listOf(Menu.Cappuccino(CoffeeBean.Regular, Milk.Whole),
                        Menu.Cappuccino(CoffeeBean.Premium, Milk.Breve),
                        Menu.Cappuccino(CoffeeBean.Regular, Milk.NonFat),
                        Menu.Cappuccino(CoffeeBean.Decaf, Milk.Whole),
                        Menu.Cappuccino(CoffeeBean.Regular, Milk.NonFat),
                        Menu.Cappuccino(CoffeeBean.Decaf, Milk.NonFat))
    log(orders)

    val espressoMachine = EspressoMachine(this)
    val t = measureTimeMillis {
        // we now turn this in to a pipeline
        // orders go into either coffee chan a or b (to be processed by one of the two baristas)
        // the result of these will get merged to be output here
        val ordersChannel = processOrders(orders)
        val coffeeChannelA = makeCoffee("barista-1", ordersChannel, espressoMachine)
        val coffeeChannelB = makeCoffee("barista-2", ordersChannel, espressoMachine)

        // as of right now there's no 'onReceiveOrClosed' operator so we need to track this manually
        // if the coffeeChannel[A|B] was closed, then onReceiveOrNull is fired on each loop rather
        // than suspending
        // this switches on receive from the two baristas, when an order arrives, we print it here
        var isBaristaOneActive = true
        var isBaristaTwoActive = true
        while (isBaristaOneActive || isBaristaTwoActive) {
            select<Unit> {
                if (isBaristaOneActive) {
                    coffeeChannelA.onReceiveOrNull { v ->
                        if (coffeeChannelA.isClosedForReceive) isBaristaOneActive = false
                        if (v != null) log("Serve: $v")
                    }
                }
                if (isBaristaTwoActive) {
                    coffeeChannelB.onReceiveOrNull { v ->
                        if (coffeeChannelB.isClosedForReceive) isBaristaTwoActive = false
                        if (v != null) log("Serve: $v")
                    }
                }
            }
        }
        espressoMachine.shutdown()
    }
    println("Execution time: $t ms")
}

// convert this to a producer of orders
private fun CoroutineScope.processOrders(orders: List<Menu>) = produce(CoroutineName("cashier")) {
    for (o in orders) send(o)
}

// convert this to a producer of completed coffee orders
private fun CoroutineScope.makeCoffee(tag: String, orders: ReceiveChannel<Menu>, espressoMachine: EspressoMachine) = produce(CoroutineName(tag)) {
    for (o in orders) {
        log("Processing order: $o")
        when (o) {
            is Menu.Cappuccino -> {
                val groundBeans = grindCoffeeBeans(o.beans())
                coroutineScope {
                    val espressoShotDeferred = async { espressoMachine.pullEspressoShot(groundBeans) }
                    val steamedMilkDeferred = async { espressoMachine.steamMilk(o.milk()) }
                    val cappuccino = makeCappuccino(o, espressoShotDeferred.await(), steamedMilkDeferred.await())
                    send(cappuccino)
                }
            }
        }
    }
}

private suspend fun grindCoffeeBeans(beans: CoffeeBean): CoffeeBean.GroundBeans {
    log("Grinding beans")
    delay(30)
    return CoffeeBean.GroundBeans(beans)
}

private suspend fun pullEspressoShot(groundBeans: CoffeeBean.GroundBeans): Espresso {
    log("Pulling espresso shot")
    delay(20)
    return Espresso(groundBeans)
}

private suspend fun steamMilk(milk: Milk): Milk.SteamedMilk {
    log("Steaming milk")
    delay(10)
    return Milk.SteamedMilk(milk)
}

private suspend fun makeCappuccino(order: Menu.Cappuccino, espressoShot: Espresso, milk: Milk.SteamedMilk): Beverage.Cappuccino {
    log("Combining ingredients")
    delay(5)
    return Beverage.Cappuccino(order, espressoShot, milk)
}
