// See https://try.kotlinlang.org

sealed class CoffeeBean {
    abstract fun price(): Float
    
    object Premium: CoffeeBean() {
        override fun price() = 1.00f
        override fun toString() = "premium"
    }
    
    object Regular: CoffeeBean() {
        override fun price() = 0.00f
        override fun toString() = "regular"
    }
    
    object Decaf: CoffeeBean() {
        override fun price() = 0.50f
        override fun toString() = "decaf"
    }
    
    data class GroundBeans(val coffeeBean: CoffeeBean): CoffeeBean() {
        override fun price() = 0.00f
        override fun toString() = "ground $coffeeBean"
    }
}

sealed class Milk {
    abstract fun price(): Float
    
    object Whole: Milk() {
        override fun price() = 0.00f

        override fun toString() = "whole milk"
    }
    
    object NonFat: Milk() {
        override fun price() = 0.00f

        override fun toString() = "non-fat milk"
    }
    
    object Breve: Milk() {
        override fun price() = 1.00f

        override fun toString() = "breve"
    }
    
    data class SteamedMilk(val milk: Milk): Milk() {
        override fun price() = 0.00f
        
        override fun toString() = "steamed milk"
    }
}

data class Espresso(val beans: CoffeeBean.GroundBeans)

sealed class Menu {
    abstract fun price(): Float
    
    abstract fun beans(): CoffeeBean
    
    abstract fun milk(): Milk
    
    data class Cappuccino(val beans: CoffeeBean, val milk: Milk): Menu() {
        override fun price() = 3.50f + beans.price() + milk.price()
        
        override fun beans() = beans
        
        override fun milk() = milk
        
        override fun toString() = "cappuccino: beans=$beans milk=$milk price=$${price().format(2)}"
    }
}

sealed class Beverage {
    data class Cappuccino(val order: Menu.Cappuccino, val espressoShot: Espresso, val steamedMilk: Milk.SteamedMilk): Beverage()
}
