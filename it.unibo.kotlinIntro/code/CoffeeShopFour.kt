//From https://proandroiddev.com/kotlin-coroutines-channels-csp-android-db441400965f

import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeUnit.MILLISECONDS
import kotlin.system.measureTimeMillis
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

// Introduce the Espresso Machine. This is a shared resource for both the baristas to access to pull
// espresso shots from and steam the milk. This also means we can perform the two tasks asynchronously.
fun main(args: Array<String>) = runBlocking {
    val orders = listOf(Menu.Cappuccino(CoffeeBean.Regular, Milk.Whole),
                        Menu.Cappuccino(CoffeeBean.Premium, Milk.Breve),
                        Menu.Cappuccino(CoffeeBean.Regular, Milk.NonFat),
                        Menu.Cappuccino(CoffeeBean.Decaf, Milk.Whole),
                        Menu.Cappuccino(CoffeeBean.Regular, Milk.NonFat),
                        Menu.Cappuccino(CoffeeBean.Decaf, Milk.NonFat))
    log(orders)
    
    val espressoMachine = EspressoMachine(this)
    val ordersChannel = produce(CoroutineName("cashier")) {
        for (o in orders) { send(o) }
    }
    
    val t = measureTimeMillis {
        coroutineScope {
    		launch(CoroutineName("barista-1")) { makeCoffee(ordersChannel, espressoMachine) }
            launch(CoroutineName("barista-2")) { makeCoffee(ordersChannel, espressoMachine) }
        }
    }
    // we need to shutdown the machine which terminates the actors launched internally
    espressoMachine.shutdown()
    println("Execution time: $t ms")
}

private suspend fun makeCoffee(orders: ReceiveChannel<Menu>, espressoMachine: EspressoMachine) {
    for (o in orders) {
        log("Processing order: $o")
        when (o) {
            is Menu.Cappuccino -> {
                val groundBeans = grindCoffeeBeans(o.beans())
                // Without async, the pull espresso shot operation would cause this function to suspend execution
                // and then execute the next line after it completes. Using async here and with steam milk means
                // we can do these two operations asynchronously. We can then call '.await' on the these two objects
                // which will suspend execution until both of the operations complete.
                coroutineScope {
                    val espressoShotDeferred = async { espressoMachine.pullEspressoShot(groundBeans) }
                    val steamedMilkDeferred = async { espressoMachine.steamMilk(o.milk()) }
                    val cappuccino = makeCappuccino(o, espressoShotDeferred.await(), steamedMilkDeferred.await())
                    log("Serve: $cappuccino")
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

private suspend fun makeCappuccino(order: Menu.Cappuccino, espressoShot: Espresso, milk: Milk.SteamedMilk): Beverage.Cappuccino {
    log("Combining ingredients")
    delay(5)
    return Beverage.Cappuccino(order, espressoShot, milk)
}
